package tn.esprit.spring.service.tarification;

/**
 * GoF Strategy : contrat commun à toutes les politiques tarifaires.
 */
public interface StrategieTarification {
    double calculer(double tarifJournalier, int jours);
}
