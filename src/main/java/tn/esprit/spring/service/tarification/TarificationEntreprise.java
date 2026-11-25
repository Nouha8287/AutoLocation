package tn.esprit.spring.service.tarification;

import org.springframework.stereotype.Component;

/**
 * Nouvelle politique (étape 4) : tarif entreprise, réduction de 15 %.
 * Ajoutée SANS modifier TarificationService : Spring l'enregistre
 * automatiquement dans la Map des stratégies sous la clé "ENTREPRISE".
 */
@Component("ENTREPRISE")
public class TarificationEntreprise implements StrategieTarification {

    private static final double REDUCTION = 0.15;

    @Override
    public double calculer(double tarifJournalier, int jours) {
        return tarifJournalier * jours * (1 - REDUCTION);
    }
}
