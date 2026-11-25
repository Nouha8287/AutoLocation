package tn.esprit.spring.service.Interface;

import tn.esprit.spring.entity.StatutVehicule;
import tn.esprit.spring.entity.Vehicule;

import java.math.BigDecimal;
import java.util.List;

public interface IVehiculeService {

    // ---------- CRUD ----------
    Vehicule creer(Vehicule vehicule);

    Vehicule obtenirParId(Long id);

    List<Vehicule> listerTous();

    Vehicule mettreAJour(Vehicule vehicule);

    void supprimer(Long id);

    // ---------- Affectations ----------
    Vehicule affecterVehiculeEquipement(Long idVehicule, Long idEquipement);

    Vehicule desaffecterEquipementVehicule(Long idVehicule, Long idEquipement);

    Vehicule affecterVehiculeAgence(Long idAgence, Long idVehicule);

    // ---------- Requêtes keyword ----------
    List<Vehicule> getByMarque(String marque);                                   // 1

    List<Vehicule> getMarque(String mot);                                        // 2

    List<Vehicule> getValeurMarque(String val);                                  // 3

    List<Vehicule> getLastValeurMarque(String valeur);                           // 4

    List<Vehicule> getMarqueSansCasse(String marque);                            // 5

    List<Vehicule> getModele(String modele);                                     // 6

    Boolean vehiculeExist(String immatriculation);                               // 7

    Long conterVehicule(String marque);                                          // 8

    List<Vehicule> tarifVehicule(BigDecimal tarifJournalier);                    // 9

    List<Vehicule> tarifVehiculeMoins(BigDecimal tarifJournalier);               // 10

    List<Vehicule> tarifVehiculeMoreEqual(BigDecimal tarifJournalier);           // 11

    List<Vehicule> tarifVehiculeEntre(BigDecimal min, BigDecimal max);           // 12

    List<Vehicule> tarifVehiculeAsc();                                           // 13

    List<Vehicule> tarifVehiculeDesc();                                          // 14

    List<Vehicule> marqueTrieeParTarif(String marque);                           // 15

    List<Vehicule> marqueEtTarifSuperieur(String marque, BigDecimal tarif);      // 16

    List<Vehicule> marqueOuModele(String marque, String modele);                 // 17

    List<Vehicule> marquesDansListe(List<String> marques);                       // 18

    List<Vehicule> marquesHorsListe(List<String> marques);                       // 19

    List<Vehicule> parStatut(StatutVehicule statut);                             // 20

    List<Vehicule> disponiblesTriesParTarif();                                   // 21

    List<Vehicule> parAgence(Long idAgence);                                     // 22

    List<Vehicule> parVilleAgence(String ville);                                 // 23

    List<Vehicule> parNomAgenceContenant(String valeur);                         // 24

    List<Vehicule> parAgenceEtMarque(Long idAgence, String marque);              // 25

    List<Vehicule> sansAgence();                                                 // 26

    List<Vehicule> affectesAUneAgence();                                         // 27

    Long compterParAgence(Long idAgence);                                        // 28

    Boolean agencePossedeVehicules(Long idAgence);                               // 29
}
