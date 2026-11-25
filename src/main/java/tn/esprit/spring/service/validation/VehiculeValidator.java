package tn.esprit.spring.service.validation;

import org.springframework.stereotype.Component;
import tn.esprit.spring.entity.Vehicule;

import java.math.BigDecimal;

/**
 * SRP : unique responsabilité = vérifier les règles de validation métier d'un véhicule.
 * Une nouvelle règle (format d'immatriculation, tarif plafond...) ne modifie que cette classe.
 */
@Component
public class VehiculeValidator {

    public void valider(Vehicule vehicule) {
        if (vehicule == null) {
            throw new IllegalArgumentException("Le véhicule est obligatoire");
        }
        validerTarif(vehicule.getTarifJournalier());
    }

    public void validerTarif(BigDecimal tarifJournalier) {
        if (tarifJournalier == null || tarifJournalier.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le tarif doit être positif");
        }
    }
}
