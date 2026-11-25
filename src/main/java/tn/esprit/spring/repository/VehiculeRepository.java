package tn.esprit.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entity.CategorieVehicule;
import tn.esprit.spring.entity.StatutVehicule;
import tn.esprit.spring.entity.Vehicule;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VehiculeRepository extends CrudRepository<Vehicule, Long> {

    // 1. Véhicules d'une marque
    List<Vehicule> findByMarque(String m);

    // 2. Marque contenant un mot
    List<Vehicule> findByMarqueContaining(String mot);

    // 3. Marque commençant par une valeur
    List<Vehicule> findByMarqueStartingWith(String val);

    // 4. Marque se terminant par une valeur
    List<Vehicule> findByMarqueEndingWith(String valeur);

    // 5. Marque sans tenir compte de la casse
    List<Vehicule> findByMarqueIgnoreCase(String marque);

    // 6. Par modèle
    List<Vehicule> findByModele(String modele);

    // 7. Existence d'une immatriculation
    Boolean existsByImmatriculation(String immatriculation);

    // 8. Nombre de véhicules d'une marque
    Long countByMarque(String marque);

    // 9. Tarif > valeur
    List<Vehicule> findByTarifJournalierGreaterThan(BigDecimal tarifJournalier);

    // 10. Tarif < valeur
    List<Vehicule> findByTarifJournalierLessThan(BigDecimal tarifJournalier);

    // 11. Tarif >= valeur
    List<Vehicule> findByTarifJournalierGreaterThanEqual(BigDecimal tarifJournalier);

    // 12. Tarif compris entre deux valeurs (bornes incluses)
    List<Vehicule> findByTarifJournalierBetween(BigDecimal min, BigDecimal max);

    // 13. Triés par tarif croissant
    List<Vehicule> findAllByOrderByTarifJournalierAsc();

    // 14. Triés par tarif décroissant
    List<Vehicule> findAllByOrderByTarifJournalierDesc();

    // 15. Véhicules d'une marque triés par tarif croissant
    List<Vehicule> findByMarqueOrderByTarifJournalierAsc(String marque);

    // 16. Véhicules d'une marque dont le tarif dépasse une valeur
    List<Vehicule> findByMarqueAndTarifJournalierGreaterThan(String marque, BigDecimal tarifJournalier);

    // 17. Véhicules d'une marque OU d'un modèle
    List<Vehicule> findByMarqueOrModele(String marque, String modele);

    // 18. Marque figurant dans une liste
    List<Vehicule> findByMarqueIn(List<String> marques);

    // 19. Marque ne figurant pas dans une liste
    List<Vehicule> findByMarqueNotIn(List<String> marques);

    // 20. Par statut
    List<Vehicule> findByStatut(StatutVehicule statut);

    // 21. Par statut trié par tarif croissant (appelé avec DISPONIBLE)
    List<Vehicule> findByStatutOrderByTarifJournalierAsc(StatutVehicule statut);

    // 22. Véhicules appartenant à une agence (navigation vehicule.agence.idAgence)
    List<Vehicule> findByAgenceIdAgence(Long idAgence);

    // 23. Véhicules appartenant à une agence d'une ville donnée
    List<Vehicule> findByAgenceVille(String ville);

    // 24. Véhicules d'une agence dont le nom contient une valeur
    List<Vehicule> findByAgenceNomContaining(String valeur);

    // 25. Véhicules d'une agence et d'une marque données
    List<Vehicule> findByAgenceIdAgenceAndMarque(Long idAgence, String marque);

    // 26. Véhicules sans agence
    List<Vehicule> findByAgenceIsNull();

    // 27. Véhicules affectés à une agence
    List<Vehicule> findByAgenceIsNotNull();

    // 28. Nombre de véhicules d'une agence
    Long countByAgenceIdAgence(Long idAgence);

    // 29. Une agence possède-t-elle des véhicules ?
    Boolean existsByAgenceIdAgence(Long idAgence);

    // Véhicule identifié par son immatriculation, sa marque et sa catégorie
    Vehicule findByImmatriculationAndMarqueAndCategorie(String immatriculation, String marque, CategorieVehicule categorie);
}
