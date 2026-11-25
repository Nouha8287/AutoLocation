package tn.esprit.spring.service.tarification;

import java.util.Set;

/** Levée quand le type de tarif demandé ne correspond à aucune stratégie. */
public class TarificationInconnueException extends IllegalArgumentException {

    public TarificationInconnueException(String type, Set<String> typesDisponibles) {
        super("Type de tarif inconnu : '" + type + "'. Types disponibles : " + typesDisponibles);
    }
}
