package tn.esprit.spring.service.tarification;

import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Contexte du patron Strategy.
 * Spring injecte une Map<nomDuBean, StrategieTarification> contenant toutes les
 * implémentations annotées @Component. Ce service ne connaît aucune formule et
 * n'est JAMAIS modifié quand une nouvelle politique est ajoutée (OCP).
 */
@Service
public class TarificationService {

    private final Map<String, StrategieTarification> strategies;

    public TarificationService(Map<String, StrategieTarification> strategies) {
        this.strategies = strategies;
    }

    public double calculerPrix(String typeTarif, double tarifJournalier, int nombreJours) {
        if (nombreJours <= 0) {
            throw new IllegalArgumentException("Le nombre de jours doit être positif");
        }
        if (tarifJournalier <= 0) {
            throw new IllegalArgumentException("Le tarif doit être positif");
        }
        StrategieTarification strategie = trouverStrategie(typeTarif);
        double prix = strategie.calculer(tarifJournalier, nombreJours);
        return Math.round(prix * 100.0) / 100.0; // arrondi au centime
    }

    public Set<String> typesDisponibles() {
        return new TreeSet<>(strategies.keySet());
    }

    private StrategieTarification trouverStrategie(String typeTarif) {
        if (typeTarif == null || typeTarif.isBlank()) {
            throw new TarificationInconnueException(String.valueOf(typeTarif), typesDisponibles());
        }
        StrategieTarification strategie = strategies.get(typeTarif.trim().toUpperCase(Locale.ROOT));
        if (strategie == null) {
            throw new TarificationInconnueException(typeTarif, typesDisponibles());
        }
        return strategie;
    }
}
