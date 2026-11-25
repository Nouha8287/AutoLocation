package tn.esprit.spring.service.journalisation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * SRP : centralise la journalisation.
 * Remplace les System.out.println dispersés : changer de format, de niveau
 * ou d'outil de log ne touche qu'à cette classe.
 */
@Service
public class JournalisationService {

    private static final Logger LOGGER = LoggerFactory.getLogger("AutoLoc");

    public void info(String message) {
        LOGGER.info(message);
    }

    public void erreur(String message) {
        LOGGER.error(message);
    }
}
