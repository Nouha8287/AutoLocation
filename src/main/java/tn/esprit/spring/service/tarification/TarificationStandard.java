package tn.esprit.spring.service.tarification;

import org.springframework.stereotype.Component;

/** Tarif standard : tarif journalier × nombre de jours. */
@Component("STANDARD")
public class TarificationStandard implements StrategieTarification {

    @Override
    public double calculer(double tarifJournalier, int jours) {
        return tarifJournalier * jours;
    }
}
