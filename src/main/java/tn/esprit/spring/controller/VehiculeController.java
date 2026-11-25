package tn.esprit.spring.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entity.StatutVehicule;
import tn.esprit.spring.entity.Vehicule;
import tn.esprit.spring.service.Interface.IVehiculeService;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Gestion des véhicules")
@RestController
@AllArgsConstructor
@RequestMapping("/VehiculeController")
public class VehiculeController {
    IVehiculeService iVehiculeService;

    // ======================= CRUD =======================

    @Operation(description = "Ajouter les véhicules")
    @PostMapping("/Add")
    public Vehicule creer(@RequestBody Vehicule vehicule) {
        return iVehiculeService.creer(vehicule);
    }

    @Operation(description = "Rechercher les véhicules")
    @GetMapping("/SearchByID/{id}")
    public Vehicule obtenirParId(@PathVariable Long id) {
        return iVehiculeService.obtenirParId(id);
    }

    @Operation(description = "Lister les véhicules")
    @GetMapping("/ListerTous")
    public List<Vehicule> listerTous() {
        return iVehiculeService.listerTous();
    }

    @Operation(description = "Modifier les véhicules")
    @PutMapping("/Update")
    public Vehicule mettreAJour(@RequestBody Vehicule vehicule) {
        return iVehiculeService.mettreAJour(vehicule);
    }

    @Operation(description = "Supprimer les véhicules")
    @DeleteMapping("/Delete/{id}")
    public void supprimer(@PathVariable Long id) {
        iVehiculeService.supprimer(id);
    }

    // =================== Affectations ===================

    @PutMapping("/Affect/{idAgence}/{idVehicule}")
    public Vehicule affecterVehiculeAgence(@PathVariable Long idAgence, @PathVariable Long idVehicule) {
        return iVehiculeService.affecterVehiculeAgence(idAgence, idVehicule);
    }

    @PutMapping("/desaffectEquipement/{idVehicule}/{idEquipement}")
    public Vehicule desaffecterEquipementVehicule(@PathVariable Long idVehicule, @PathVariable Long idEquipement) {
        return iVehiculeService.desaffecterEquipementVehicule(idVehicule, idEquipement);
    }

    @PutMapping("/affect/{idVehicule}/{idEquipement}")
    public Vehicule affecterVehiculeEquipement(@PathVariable Long idVehicule, @PathVariable Long idEquipement) {
        return iVehiculeService.affecterVehiculeEquipement(idVehicule, idEquipement);
    }

    // ================= Requêtes keyword =================

    @Operation(description = "1. Véhicules d'une marque")
    @GetMapping("/listVehicules/{marque}")
    public List<Vehicule> getByMarque(@PathVariable String marque) {
        return iVehiculeService.getByMarque(marque);
    }

    @Operation(description = "2. Marque contenant un mot")
    @GetMapping("/listV/{mot}")
    public List<Vehicule> getMarque(@PathVariable String mot) {
        return iVehiculeService.getMarque(mot);
    }

    @Operation(description = "3. Marque commençant par une valeur")
    @GetMapping("/listVal/{val}")
    public List<Vehicule> getValeurMarque(@PathVariable String val) {
        return iVehiculeService.getValeurMarque(val);
    }

    @Operation(description = "4. Marque se terminant par une valeur")
    @GetMapping("/listValeurEnd/{valeur}")
    public List<Vehicule> getLastValeurMarque(@PathVariable String valeur) {
        return iVehiculeService.getLastValeurMarque(valeur);
    }

    @Operation(description = "5. Marque sans tenir compte de la casse")
    @GetMapping("/listVe/{marque}")
    public List<Vehicule> getMarqueSansCasse(@PathVariable String marque) {
        return iVehiculeService.getMarqueSansCasse(marque);
    }

    @Operation(description = "6. Véhicules par modèle")
    @GetMapping("/listVeh/{modele}")
    public List<Vehicule> getModele(@PathVariable String modele) {
        return iVehiculeService.getModele(modele);
    }

    @Operation(description = "7. Existence d'une immatriculation")
    @GetMapping("/listsVeh/{immatriculation}")
    public Boolean vehiculeExist(@PathVariable String immatriculation) {
        return iVehiculeService.vehiculeExist(immatriculation);
    }

    @Operation(description = "8. Nombre de véhicules d'une marque")
    @GetMapping("/VehiculeMarque/{marque}")
    public Long conterVehicule(@PathVariable String marque) {
        return iVehiculeService.conterVehicule(marque);
    }

    @Operation(description = "9. Tarif supérieur à une valeur")
    @GetMapping("/Vehicule/{tarifJournalier}")
    public List<Vehicule> tarifVehicule(@PathVariable BigDecimal tarifJournalier) {
        return iVehiculeService.tarifVehicule(tarifJournalier);
    }

    @Operation(description = "10. Tarif inférieur à une valeur")
    @GetMapping("/VehiculeLess/{tarifJournalier}")
    public List<Vehicule> tarifVehiculeMoins(@PathVariable BigDecimal tarifJournalier) {
        return iVehiculeService.tarifVehiculeMoins(tarifJournalier);
    }

    @Operation(description = "11. Tarif supérieur ou égal à une valeur")
    @GetMapping("/VehiculeMoreEqual/{tarifJournalier}")
    public List<Vehicule> tarifVehiculeMoreEqual(@PathVariable BigDecimal tarifJournalier) {
        return iVehiculeService.tarifVehiculeMoreEqual(tarifJournalier);
    }

    @Operation(description = "12. Tarif compris entre deux valeurs")
    @GetMapping("/VehiculeEntre/{min}/{max}")
    public List<Vehicule> tarifVehiculeEntre(@PathVariable BigDecimal min, @PathVariable BigDecimal max) {
        return iVehiculeService.tarifVehiculeEntre(min, max);
    }

    @Operation(description = "13. Triés par tarif croissant")
    @GetMapping("/TriTarifAsc")
    public List<Vehicule> tarifVehiculeAsc() {
        return iVehiculeService.tarifVehiculeAsc();
    }

    @Operation(description = "14. Triés par tarif décroissant")
    @GetMapping("/TriTarifDesc")
    public List<Vehicule> tarifVehiculeDesc() {
        return iVehiculeService.tarifVehiculeDesc();
    }

    @Operation(description = "15. Véhicules d'une marque triés par tarif croissant")
    @GetMapping("/MarqueTriTarif/{marque}")
    public List<Vehicule> marqueTrieeParTarif(@PathVariable String marque) {
        return iVehiculeService.marqueTrieeParTarif(marque);
    }

    @Operation(description = "16. Véhicules d'une marque dont le tarif dépasse une valeur")
    @GetMapping("/MarqueTarifSup/{marque}/{tarif}")
    public List<Vehicule> marqueEtTarifSuperieur(@PathVariable String marque, @PathVariable BigDecimal tarif) {
        return iVehiculeService.marqueEtTarifSuperieur(marque, tarif);
    }

    @Operation(description = "17. Véhicules d'une marque OU d'un modèle")
    @GetMapping("/MarqueOuModele/{marque}/{modele}")
    public List<Vehicule> marqueOuModele(@PathVariable String marque, @PathVariable String modele) {
        return iVehiculeService.marqueOuModele(marque, modele);
    }

    @Operation(description = "18. Marque dans une liste : ?marques=Renault,Peugeot")
    @GetMapping("/MarquesIn")
    public List<Vehicule> marquesDansListe(@RequestParam List<String> marques) {
        return iVehiculeService.marquesDansListe(marques);
    }

    @Operation(description = "19. Marque hors d'une liste : ?marques=Renault,Peugeot")
    @GetMapping("/MarquesNotIn")
    public List<Vehicule> marquesHorsListe(@RequestParam List<String> marques) {
        return iVehiculeService.marquesHorsListe(marques);
    }

    @Operation(description = "20. Véhicules par statut (DISPONIBLE, LOUE, MAINTENANCE)")
    @GetMapping("/Statut/{statut}")
    public List<Vehicule> parStatut(@PathVariable StatutVehicule statut) {
        return iVehiculeService.parStatut(statut);
    }

    @Operation(description = "21. Véhicules DISPONIBLES triés par tarif croissant")
    @GetMapping("/Disponibles")
    public List<Vehicule> disponiblesTriesParTarif() {
        return iVehiculeService.disponiblesTriesParTarif();
    }

    @Operation(description = "22. Véhicules appartenant à une agence")
    @GetMapping("/Agence/{idAgence}")
    public List<Vehicule> parAgence(@PathVariable Long idAgence) {
        return iVehiculeService.parAgence(idAgence);
    }

    @Operation(description = "23. Véhicules d'une agence d'une ville donnée")
    @GetMapping("/AgenceVille/{ville}")
    public List<Vehicule> parVilleAgence(@PathVariable String ville) {
        return iVehiculeService.parVilleAgence(ville);
    }

    @Operation(description = "24. Véhicules d'une agence dont le nom contient une valeur")
    @GetMapping("/AgenceNom/{valeur}")
    public List<Vehicule> parNomAgenceContenant(@PathVariable String valeur) {
        return iVehiculeService.parNomAgenceContenant(valeur);
    }

    @Operation(description = "25. Véhicules d'une agence et d'une marque données")
    @GetMapping("/AgenceMarque/{idAgence}/{marque}")
    public List<Vehicule> parAgenceEtMarque(@PathVariable Long idAgence, @PathVariable String marque) {
        return iVehiculeService.parAgenceEtMarque(idAgence, marque);
    }

    @Operation(description = "26. Véhicules sans agence")
    @GetMapping("/SansAgence")
    public List<Vehicule> sansAgence() {
        return iVehiculeService.sansAgence();
    }

    @Operation(description = "27. Véhicules affectés à une agence")
    @GetMapping("/AvecAgence")
    public List<Vehicule> affectesAUneAgence() {
        return iVehiculeService.affectesAUneAgence();
    }

    @Operation(description = "28. Nombre de véhicules d'une agence")
    @GetMapping("/CompterAgence/{idAgence}")
    public Long compterParAgence(@PathVariable Long idAgence) {
        return iVehiculeService.compterParAgence(idAgence);
    }

    @Operation(description = "29. L'agence possède-t-elle des véhicules ?")
    @GetMapping("/AgenceAVehicules/{idAgence}")
    public Boolean agencePossedeVehicules(@PathVariable Long idAgence) {
        return iVehiculeService.agencePossedeVehicules(idAgence);
    }
}
