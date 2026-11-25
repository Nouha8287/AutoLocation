package tn.esprit.spring.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entity.CategorieVehicule;
import tn.esprit.spring.entity.Reservation;
import tn.esprit.spring.service.Interface.IReservationService;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Gestion des Reservations")
@RestController
@AllArgsConstructor
@RequestMapping("/ReservationController")
public class ReservationController {
    IReservationService iReservationService;

    @PutMapping("/desaffectVehicule/{idReservation}")
    public Reservation desaffecterReservationVehicule(@PathVariable Long idReservation) {
        return iReservationService.desaffecterReservationVehicule(idReservation);
    }

    @Operation(description = "Affecter un véhicule aux réservations d'une période. Dates au format AAAA-MM-JJ")
    @PutMapping("/affectReservationVehicule/{matricule}/{marque}/{cv}/{dateDebut}/{dateFin}")
    public void affecterVehiculeReservation(@PathVariable String matricule,
                                            @PathVariable String marque,
                                            @PathVariable CategorieVehicule cv,
                                            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
                                            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        iReservationService.affecterVehiculeReservation(matricule, marque, cv, dateDebut, dateFin);
    }

    @PostMapping("/addContrat")
    public Reservation ajouterContartReservation(@RequestBody Reservation r) {
        return iReservationService.ajouterContartReservation(r);
    }

    @Operation(description = "Ajouter les Reservations")
    @PostMapping("/Add")
    public Reservation creer(@RequestBody Reservation reservation) {
        return iReservationService.creer(reservation);
    }

    @Operation(description = "Recherhcer les Reservations")
    @GetMapping("/SearchByID/{id}")
    public Reservation obtenirParId(@PathVariable Long id) {
        return iReservationService.obtenirParId(id);
    }

    @Operation(description = "Lister les Reservations")
    @GetMapping("/ListerTous")
    public List<Reservation> listerTous() {
        return iReservationService.listerTous();
    }

    @Operation(description = "Modifier les Reservations")
    @PutMapping("/Update")
    public Reservation mettreAJour(@RequestBody Reservation reservation) {
        return iReservationService.mettreAJour(reservation);
    }


    @Operation(description = "Supprimer les Reservations")
    @DeleteMapping("/Delete/{id}")
    public void supprimer(@PathVariable Long id) {
        iReservationService.supprimer(id);
    }
}
