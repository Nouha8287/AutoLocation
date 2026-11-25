package tn.esprit.spring.service.Impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entity.Agence;
import tn.esprit.spring.entity.Equipement;
import tn.esprit.spring.entity.StatutVehicule;
import tn.esprit.spring.entity.Vehicule;
import tn.esprit.spring.repository.AgenceRepository;
import tn.esprit.spring.repository.EquipementRepository;
import tn.esprit.spring.repository.VehiculeRepository;
import tn.esprit.spring.service.Interface.IVehiculeService;
import tn.esprit.spring.service.journalisation.JournalisationService;
import tn.esprit.spring.service.validation.VehiculeValidator;

import java.math.BigDecimal;
import java.util.List;

/**
 * Gestion des opérations métier sur les véhicules (SRP).
 * La validation est déléguée à VehiculeValidator et la journalisation
 * à JournalisationService. Toutes les dépendances sont des abstractions
 * injectées par Spring via le constructeur (DIP / DI).
 */
@Service
@AllArgsConstructor
public class VehiculeServiceIMPL implements IVehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final EquipementRepository equipementRepository;
    private final AgenceRepository agenceRepository;
    private final VehiculeValidator vehiculeValidator;
    private final JournalisationService journalisationService;

    // ======================= CRUD =======================

    @Override
    public Vehicule creer(Vehicule vehicule) {
        vehiculeValidator.valider(vehicule);
        Vehicule sauvegarde = vehiculeRepository.save(vehicule);
        journalisationService.info("Ajout du véhicule " + sauvegarde.getImmatriculation());
        return sauvegarde;
    }

    @Override
    public Vehicule obtenirParId(Long id) {
        return vehiculeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Vehicule> listerTous() {
        return (List<Vehicule>) vehiculeRepository.findAll();
    }

    @Override
    public Vehicule mettreAJour(Vehicule vehicule) {
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public void supprimer(Long id) {
        vehiculeRepository.deleteById(id);
    }

    // =================== Affectations ===================

    @Transactional
    @Override
    public Vehicule affecterVehiculeAgence(Long idAgence, Long idVehicule) {
        Agence agence = agenceRepository.findById(idAgence).orElse(null);
        Vehicule vehicule = vehiculeRepository.findById(idVehicule).orElse(null);
        assert vehicule != null;
        vehicule.setAgence(agence);
        return vehiculeRepository.save(vehicule);
    }

    @Transactional
    @Override
    public Vehicule desaffecterEquipementVehicule(Long idVehicule, Long idEquipement) {
        Vehicule vehicule = vehiculeRepository.findById(idVehicule).orElse(null);
        Equipement equipement = equipementRepository.findById(idEquipement).orElse(null);
        assert vehicule != null;
        vehicule.getEquipements().remove(equipement);
        return vehiculeRepository.save(vehicule);
    }

    @Transactional
    @Override
    public Vehicule affecterVehiculeEquipement(Long idVehicule, Long idEquipement) {
        Vehicule vehicule = vehiculeRepository.findById(idVehicule).orElse(null);
        Equipement equipement = equipementRepository.findById(idEquipement).orElse(null);
        assert vehicule != null;
        vehicule.getEquipements().add(equipement);
        return vehiculeRepository.save(vehicule);
    }

    // ================= Requêtes keyword =================

    @Override public List<Vehicule> getByMarque(String marque)          { return vehiculeRepository.findByMarque(marque); }
    @Override public List<Vehicule> getMarque(String mot)               { return vehiculeRepository.findByMarqueContaining(mot); }
    @Override public List<Vehicule> getValeurMarque(String val)         { return vehiculeRepository.findByMarqueStartingWith(val); }
    @Override public List<Vehicule> getLastValeurMarque(String valeur)  { return vehiculeRepository.findByMarqueEndingWith(valeur); }
    @Override public List<Vehicule> getMarqueSansCasse(String marque)   { return vehiculeRepository.findByMarqueIgnoreCase(marque); }
    @Override public List<Vehicule> getModele(String modele)            { return vehiculeRepository.findByModele(modele); }
    @Override public Boolean vehiculeExist(String immatriculation)      { return vehiculeRepository.existsByImmatriculation(immatriculation); }
    @Override public Long conterVehicule(String marque)                 { return vehiculeRepository.countByMarque(marque); }

    @Override public List<Vehicule> tarifVehicule(BigDecimal t)         { return vehiculeRepository.findByTarifJournalierGreaterThan(t); }
    @Override public List<Vehicule> tarifVehiculeMoins(BigDecimal t)    { return vehiculeRepository.findByTarifJournalierLessThan(t); }
    @Override public List<Vehicule> tarifVehiculeMoreEqual(BigDecimal t){ return vehiculeRepository.findByTarifJournalierGreaterThanEqual(t); }
    @Override public List<Vehicule> tarifVehiculeEntre(BigDecimal min, BigDecimal max) { return vehiculeRepository.findByTarifJournalierBetween(min, max); }
    @Override public List<Vehicule> tarifVehiculeAsc()                  { return vehiculeRepository.findAllByOrderByTarifJournalierAsc(); }
    @Override public List<Vehicule> tarifVehiculeDesc()                 { return vehiculeRepository.findAllByOrderByTarifJournalierDesc(); }

    @Override public List<Vehicule> marqueTrieeParTarif(String marque)  { return vehiculeRepository.findByMarqueOrderByTarifJournalierAsc(marque); }
    @Override public List<Vehicule> marqueEtTarifSuperieur(String marque, BigDecimal tarif) { return vehiculeRepository.findByMarqueAndTarifJournalierGreaterThan(marque, tarif); }
    @Override public List<Vehicule> marqueOuModele(String marque, String modele) { return vehiculeRepository.findByMarqueOrModele(marque, modele); }
    @Override public List<Vehicule> marquesDansListe(List<String> marques) { return vehiculeRepository.findByMarqueIn(marques); }
    @Override public List<Vehicule> marquesHorsListe(List<String> marques) { return vehiculeRepository.findByMarqueNotIn(marques); }

    @Override public List<Vehicule> parStatut(StatutVehicule statut)    { return vehiculeRepository.findByStatut(statut); }
    @Override public List<Vehicule> disponiblesTriesParTarif()          { return vehiculeRepository.findByStatutOrderByTarifJournalierAsc(StatutVehicule.DISPONIBLE); }

    @Override public List<Vehicule> parAgence(Long idAgence)            { return vehiculeRepository.findByAgenceIdAgence(idAgence); }
    @Override public List<Vehicule> parVilleAgence(String ville)        { return vehiculeRepository.findByAgenceVille(ville); }
    @Override public List<Vehicule> parNomAgenceContenant(String valeur){ return vehiculeRepository.findByAgenceNomContaining(valeur); }
    @Override public List<Vehicule> parAgenceEtMarque(Long idAgence, String marque) { return vehiculeRepository.findByAgenceIdAgenceAndMarque(idAgence, marque); }
    @Override public List<Vehicule> sansAgence()                        { return vehiculeRepository.findByAgenceIsNull(); }
    @Override public List<Vehicule> affectesAUneAgence()                { return vehiculeRepository.findByAgenceIsNotNull(); }
    @Override public Long compterParAgence(Long idAgence)               { return vehiculeRepository.countByAgenceIdAgence(idAgence); }
    @Override public Boolean agencePossedeVehicules(Long idAgence)      { return vehiculeRepository.existsByAgenceIdAgence(idAgence); }
}
