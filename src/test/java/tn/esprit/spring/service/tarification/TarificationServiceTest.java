package tn.esprit.spring.service.tarification;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/** Test unitaire sans contexte Spring : on construit la Map à la main. */
class TarificationServiceTest {

    private final TarificationService service = new TarificationService(Map.of(
            "STANDARD", new TarificationStandard(),
            "WEEKEND", new TarificationWeekend(),
            "LONGUE_DUREE", new TarificationLongueDuree(),
            "ENTREPRISE", new TarificationEntreprise()
    ));

    @Test
    void calculeChaquePolitique() {
        assertEquals(300.0, service.calculerPrix("STANDARD", 100, 3), 0.001);
        assertEquals(270.0, service.calculerPrix("WEEKEND", 100, 3), 0.001);
        assertEquals(240.0, service.calculerPrix("LONGUE_DUREE", 100, 3), 0.001);
        assertEquals(255.0, service.calculerPrix("ENTREPRISE", 100, 3), 0.001);
    }

    @Test
    void typeInsensibleALaCasse() {
        assertEquals(270.0, service.calculerPrix("weekend", 100, 3), 0.001);
    }

    @Test
    void typeInconnuLeveUneException() {
        assertThrows(TarificationInconnueException.class, () -> service.calculerPrix("ETUDIANT", 100, 3));
    }

    @Test
    void nombreDeJoursInvalide() {
        assertThrows(IllegalArgumentException.class, () -> service.calculerPrix("STANDARD", 100, 0));
    }
}
