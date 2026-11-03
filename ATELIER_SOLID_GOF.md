# Atelier : Améliorer la conception d'AutoLoc avec GoF et SOLID

## Organisation du code livré

| Élément | Emplacement |
|---|---|
| Requêtes keyword 1 → 29 | `repository/VehiculeRepository`, `service/Interface/IVehiculeService`, `service/Impl/VehiculeServiceIMPL`, `controller/VehiculeController` |
| Service refactorisé (SRP) | `service/Impl/VehiculeServiceIMPL` (méthode `creer`) |
| Validation | `service/validation/VehiculeValidator` |
| Journalisation | `service/journalisation/JournalisationService` |
| Strategy | `service/tarification/StrategieTarification` + `TarificationStandard`, `TarificationWeekend`, `TarificationLongueDuree`, `TarificationEntreprise` |
| Contexte Strategy | `service/tarification/TarificationService` |
| Exposition REST | `controller/TarificationController` (`GET /api/TarificationController/prix/{idVehicule}?type=WEEKEND&jours=3`) |
| Gestion des erreurs | `controller/GestionErreurs` (HTTP 400 au lieu de 500) |
| Test unitaire | `src/test/.../TarificationServiceTest` |

> Dans le projet, la méthode `ajouter()` de l'énoncé correspond à `creer()`.

---

## Partie A — Identifier les problèmes de conception

**1. Responsabilités prises en charge par `VehiculeService`**

- Gestion métier / persistance des véhicules (`repository.save`).
- Validation des données (tarif positif, nombre de jours positif).
- Journalisation (`System.out.println`).
- Calcul du prix d'une location (`calculerTarif`).

Soit quatre responsabilités dans une seule classe.

**2. Pourquoi ces responsabilités évoluent indépendamment ?**

Chacune change pour une raison et à l'initiative d'un acteur différent :
- les **règles de validation** changent quand le métier ajoute une contrainte (format d'immatriculation, tarif plafond…) ;
- la **journalisation** change pour des raisons techniques (passer de la console à SLF4J/Logback, fichier, ELK, niveau de log) ;
- la **gestion des véhicules** change quand le cycle de vie CRUD évolue ;
- le **calcul du tarif** change quand la politique commerciale évolue.

Les mélanger oblige à modifier (et retester) la même classe pour des motifs sans rapport, avec un risque de régression sur le reste.

**3. Principe SOLID concerné**

Le **SRP** (*Single Responsibility Principle*) : une classe ne doit avoir qu'une seule raison de changer.
Le calcul tarifaire relève en plus de l'**OCP** (traité en partie C).

**4. Répartition proposée**

| Classe | Responsabilité unique |
|---|---|
| `VehiculeServiceIMPL` | Orchestrer les opérations métier sur les véhicules |
| `VehiculeValidator` | Vérifier les règles de validation |
| `JournalisationService` | Centraliser les traces |
| `TarificationService` + stratégies | Calculer le prix d'une location |
| `VehiculeRepository` | Accéder aux données |

---

## Partie B — Appliquer SRP et DIP

### B.1 SRP — refactorisation de `ajouter()` / `creer()`

```java
@Override
public Vehicule creer(Vehicule vehicule) {
    vehiculeValidator.valider(vehicule);                       // validation déléguée
    Vehicule sauvegarde = vehiculeRepository.save(vehicule);   // persistance déléguée
    journalisationService.info("Ajout du véhicule " + sauvegarde.getImmatriculation()); // log délégué
    return sauvegarde;
}
```

La méthode ne fait plus qu'**orchestrer** : elle ne sait ni comment on valide, ni comment on journalise, ni comment on persiste.
`VehiculeValidator` utilise `BigDecimal.compareTo` car `tarifJournalier` est un `BigDecimal` dans l'entité (`<= 0` n'est pas applicable à un `BigDecimal`).
`JournalisationService` remplace `System.out.println` par SLF4J (fourni par Spring Boot).

### B.2 DIP

**1. Rôle de `VehiculeRepository`**

C'est la **couche d'accès aux données** : une interface (contrat) qui hérite de `CrudRepository<Vehicule, Long>` et déclare les requêtes (`findByMarque`, `countByAgenceIdAgence`…).
On n'écrit aucune implémentation : Spring Data JPA génère au démarrage un proxy qui traduit les noms de méthodes en requêtes JPQL et délègue à Hibernate.

**2. Pourquoi dépendre d'un contrat plutôt que créer soi-même la persistance ?**

Si le service faisait `new VehiculeDaoMySQL()` ou ouvrait lui-même une connexion JDBC, le module de haut niveau (règles métier) dépendrait d'un détail de bas niveau (MySQL, JDBC).
En dépendant de l'interface :
- on peut changer de SGBD ou de technologie de persistance sans toucher au service ;
- on peut tester le service avec un faux repository (mock) ;
- le service reste centré sur le métier.

C'est exactement le DIP : *les modules de haut niveau et de bas niveau dépendent tous deux d'abstractions*.

**3. Comment Spring injecte le repository**

Dans le projet :

```java
@Service
@AllArgsConstructor
public class VehiculeServiceIMPL implements IVehiculeService {
    private final VehiculeRepository vehiculeRepository;
    private final EquipementRepository equipementRepository;
    private final AgenceRepository agenceRepository;
    private final VehiculeValidator vehiculeValidator;
    private final JournalisationService journalisationService;
```

1. Au démarrage, `@SpringBootApplication` déclenche le scan des composants de `tn.esprit.spring` et de ses sous-packages.
2. Spring Data détecte `VehiculeRepository` et enregistre dans le conteneur un bean proxy qui l'implémente.
3. `@Service` fait de `VehiculeServiceIMPL` un bean.
4. Lombok `@AllArgsConstructor` génère un constructeur prenant toutes les dépendances ; la classe n'ayant qu'un constructeur, Spring l'utilise automatiquement (pas besoin de `@Autowired`) et y passe les beans correspondant à chaque type : c'est l'**injection par constructeur**.

Même mécanisme dans `VehiculeController`, qui reçoit `IVehiculeService` (l'interface, pas `VehiculeServiceIMPL`).

**4. Lien entre DIP et DI**

- Le **DIP** est un **principe de conception** : dépendre d'abstractions et non d'implémentations concrètes.
- La **DI** est une **technique** qui permet de l'appliquer : un tiers (le conteneur Spring) crée les objets et fournit à chaque classe les implémentations de ses dépendances, au lieu qu'elle les instancie avec `new`.

Le DIP dit *de quoi* dépendre, la DI dit *qui fournit* la dépendance. Dans AutoLoc : le contrôleur dépend de `IVehiculeService`, le service de `VehiculeRepository`, et Spring branche les implémentations.

---

## Partie C — GoF Strategy et OCP

### Code

```java
public interface StrategieTarification {
    double calculer(double tarifJournalier, int jours);
}

@Component("STANDARD")
public class TarificationStandard implements StrategieTarification {
    public double calculer(double tarifJournalier, int jours) { return tarifJournalier * jours; }
}

@Component("WEEKEND")
public class TarificationWeekend implements StrategieTarification {
    private static final double REDUCTION = 0.10;
    public double calculer(double tarifJournalier, int jours) { return tarifJournalier * jours * (1 - REDUCTION); }
}

@Component("LONGUE_DUREE")
public class TarificationLongueDuree implements StrategieTarification {
    private static final double REDUCTION = 0.20;
    public double calculer(double tarifJournalier, int jours) { return tarifJournalier * jours * (1 - REDUCTION); }
}
```

Service de calcul (contexte) :

```java
@Service
public class TarificationService {
    private final Map<String, StrategieTarification> strategies;

    public TarificationService(Map<String, StrategieTarification> strategies) {
        this.strategies = strategies;   // Spring injecte TOUS les beans StrategieTarification, clé = nom du bean
    }

    public double calculerPrix(String typeTarif, double tarifJournalier, int nombreJours) {
        if (nombreJours <= 0) throw new IllegalArgumentException("Le nombre de jours doit être positif");
        if (tarifJournalier <= 0) throw new IllegalArgumentException("Le tarif doit être positif");
        StrategieTarification strategie = trouverStrategie(typeTarif);
        return Math.round(strategie.calculer(tarifJournalier, nombreJours) * 100.0) / 100.0;
    }
    ...
}
```

### Étape 4 — `TarificationEntreprise`

```java
@Component("ENTREPRISE")
public class TarificationEntreprise implements StrategieTarification {
    private static final double REDUCTION = 0.15;
    public double calculer(double tarifJournalier, int jours) { return tarifJournalier * jours * (1 - REDUCTION); }
}
```

**Aucune ligne de `TarificationService` n'a été modifiée** : Spring ajoute automatiquement le nouveau bean dans la `Map` sous la clé `ENTREPRISE`.

Vérification (tarif 100, 3 jours) : STANDARD 300 · WEEKEND 270 · LONGUE_DUREE 240 · ENTREPRISE 255.

### Questions

**1. Problème résolu par Strategy**

Il évite le gros bloc `if / else if / switch` sur le type de tarif dans une même méthode.
Il permet de **choisir à l'exécution** un algorithme parmi une famille d'algorithmes interchangeables, derrière une interface commune.

**2. Pourquoi isoler chaque formule dans une classe ?**

- chaque classe a une seule raison de changer (SRP) ;
- une formule se teste isolément ;
- modifier la remise week-end ne peut pas casser la longue durée ;
- le code est lisible : le nom de la classe documente la politique.

**3. Application de l'OCP**

Le `TarificationService` est **fermé à la modification** (il ne contient aucune formule ni aucun `switch`) et l'application est **ouverte à l'extension** : ajouter une politique = ajouter une classe annotée `@Component("NOM")`. L'ajout de `TarificationEntreprise` en est la démonstration.

**4. Type de tarif inexistant**

Le service ne doit ni renvoyer `null` ni appliquer silencieusement le tarif standard (le client paierait un prix qu'il n'a pas demandé).
Il lève une exception métier explicite, `TarificationInconnueException`, dont le message liste les types disponibles.
`GestionErreurs` (`@RestControllerAdvice`) la transforme en **HTTP 400** :

```json
{ "erreur": "Type de tarif inconnu : 'ETUDIANT'. Types disponibles : [ENTREPRISE, LONGUE_DUREE, STANDARD, WEEKEND]" }
```

Le type est aussi normalisé (`trim` + majuscules) pour accepter `weekend` comme `WEEKEND`.

> Remarque : l'interface imposée par l'énoncé utilise `double`. Pour de l'argent, `BigDecimal` (déjà utilisé dans l'entité) serait plus rigoureux ; l'arrondi au centime dans le service limite ici les erreurs de virgule flottante.

---

## Partie D — Factory, ISP et LSP

### Situation 1 — Factory

**Problème résolu** : sans fabrique, chaque appelant écrit `if (format == PDF) new FacturePdf() else new FactureExcel()`. Ce choix est dupliqué partout et tout nouveau format (CSV, HTML) oblige à modifier tous les appelants.

**Solution** :

```java
public interface Facture {
    byte[] generer(Contrat contrat);
}

public class FacturePdf implements Facture { ... }
public class FactureExcel implements Facture { ... }

public enum FormatFacture { PDF, EXCEL }

@Component
public class FactureFactory {
    public Facture creerFacture(FormatFacture format) {
        return switch (format) {
            case PDF   -> new FacturePdf();
            case EXCEL -> new FactureExcel();
        };
    }
}
```

**Méthode exposée** : `Facture creerFacture(FormatFacture format)`.

**Avantages de la centralisation** :
- un seul endroit connaît les classes concrètes ;
- les clients manipulent l'abstraction `Facture` (DIP) ;
- ajouter un format ne touche que la fabrique et la nouvelle classe ;
- la création (paramétrage, dépendances) est cohérente et testable.

Variante Spring proche de la partie C : déclarer `FacturePdf`/`FactureExcel` en `@Component` et injecter une `Map<String, Facture>` dans la fabrique, ce qui la ferme elle aussi à la modification.

### Situation 2 — ISP

**Principe concerné** : l'**ISP** (*Interface Segregation Principle*) — un client ne doit pas dépendre de méthodes qu'il n'utilise pas.
`GestionVehicule` est une interface « obèse » : un composant de consultation est couplé à l'export de rapports et aux notifications, et une implémentation en lecture seule devrait coder des méthodes inutiles.

**Découpage proposé** :

```java
public interface ConsultationVehicule {
    Vehicule consulter(Long id);
}

public interface GestionVehiculeEcriture {
    Vehicule ajouter(Vehicule vehicule);
    Vehicule modifier(Long id, Vehicule vehicule);
    void supprimer(Long id);
}

public interface ExportRapport {
    void exporterRapport();
}

public interface NotificationService {
    void envoyerNotification();
}
```

Le composant de consultation ne dépend plus que de `ConsultationVehicule`.
Une classe peut implémenter plusieurs de ces interfaces si nécessaire (`class VehiculeServiceIMPL implements ConsultationVehicule, GestionVehiculeEcriture`).

> Dans AutoLoc, `IVehiculeService` présente le même défaut : CRUD, affectations et 29 recherches dans un seul contrat. Un découpage `IVehiculeCrudService` / `IVehiculeRechercheService` / `IVehiculeAffectationService` serait l'application directe de l'ISP.

### Situation 3 — LSP

**Pourquoi c'est une violation** : le **LSP** (*Liskov Substitution Principle*) exige qu'un objet d'une sous-classe puisse remplacer un objet de la classe de base **sans que le code appelant ne casse**.
Si la classe de base promet une opération et que la sous-classe lève systématiquement `UnsupportedOperationException`, le code écrit pour `Vehicule` plante dès qu'il reçoit cette sous-classe : la sous-classe ne respecte pas le contrat (elle renforce les préconditions — « ne jamais appeler cette méthode »). L'héritage est alors mal choisi.

Exemple : `Vehicule` expose `louer()` ; `VehiculeDeService` (véhicule interne non louable) hérite de `Vehicule` et redéfinit `louer()` en levant l'exception.

**Solutions** :
- ne mettre dans la classe de base **que** ce qui est vrai pour tous les sous-types ;
- extraire la capacité dans une interface dédiée (`Louable` avec `louer()`), implémentée seulement par les véhicules réellement louables ; le code de location manipule `Louable`, jamais un objet incapable de l'être ;
- ou préférer la **composition** / un attribut (`statut`, `louable`) à l'héritage quand la différence n'est qu'un état.

Ainsi tout objet reçu par le code appelant honore le contrat attendu.

---

## Synthèse des principes et patrons utilisés

| Principe / patron | Application dans AutoLoc |
|---|---|
| **S**RP | `VehiculeServiceIMPL` orchestre ; `VehiculeValidator` valide ; `JournalisationService` journalise ; chaque stratégie porte une seule formule |
| **O**CP | Nouvelle politique tarifaire = nouvelle classe, `TarificationService` inchangé (preuve : `TarificationEntreprise`) |
| **L**SP | Toutes les stratégies sont substituables via `StrategieTarification` ; capacités optionnelles extraites en interfaces (`Louable`) plutôt que de lever `UnsupportedOperationException` |
| **I**SP | Découpage des interfaces obèses (`GestionVehicule`, et à terme `IVehiculeService`) en contrats spécialisés |
| **D**IP | Contrôleur → `IVehiculeService`, service → `VehiculeRepository`, tarification → `StrategieTarification` ; implémentations fournies par l'injection Spring (DI par constructeur) |
| **Strategy** (GoF, comportemental) | Famille d'algorithmes de tarification interchangeables, sélectionnés à l'exécution via une `Map` injectée |
| **Factory** (GoF, création) | `FactureFactory.creerFacture(format)` centralise l'instanciation des factures PDF / Excel |

En résumé : l'application garde exactement les mêmes fonctionnalités CRUD, mais chaque classe a un rôle clair, dépend d'abstractions, et les évolutions prévisibles (nouvelles règles tarifaires, nouveaux formats de facture) se font **par ajout de classes et non par modification** du code existant.
