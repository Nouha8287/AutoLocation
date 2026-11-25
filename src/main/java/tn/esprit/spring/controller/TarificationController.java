package tn.esprit.spring.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entity.Vehicule;
import tn.esprit.spring.service.Interface.IVehiculeService;
import tn.esprit.spring.service.tarification.TarificationService;

import java.util.Set;

@Tag(name = "Tarification des locations")
@RestController
@AllArgsConstructor
@RequestMapping("/TarificationController")
public class TarificationController {

    private final TarificationService tarificationService;
    private final IVehiculeService vehiculeService;

    @Operation(description = "Types de tarif disponibles")
    @GetMapping("/types")
    public Set<String> typesDisponibles() {
        return tarificationService.typesDisponibles();
    }

    @Operation(description = "Prix d'une location : /prix/{idVehicule}?type=WEEKEND&jours=3")
    @GetMapping("/prix/{idVehicule}")
    public double calculerPrix(@PathVariable Long idVehicule,
                               @RequestParam(defaultValue = "STANDARD") String type,
                               @RequestParam int jours) {
        Vehicule vehicule = vehiculeService.obtenirParId(idVehicule);
        if (vehicule == null) {
            throw new IllegalArgumentException("Véhicule introuvable : " + idVehicule);
        }
        if (vehicule.getTarifJournalier() == null) {
            throw new IllegalArgumentException("Le véhicule n'a pas de tarif journalier");
        }
        return tarificationService.calculerPrix(type, vehicule.getTarifJournalier().doubleValue(), jours);
    }
}
