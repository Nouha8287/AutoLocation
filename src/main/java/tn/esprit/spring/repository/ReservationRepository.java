package tn.esprit.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import tn.esprit.spring.entity.Reservation;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    // Réservations qui commencent après dateDebut ET se terminent avant dateFin (bornes exclues).
    // Pour inclure les bornes : findByDateDebutGreaterThanEqualAndDateFinLessThanEqual
    List<Reservation> findByDateDebutGreaterThanAndDateFinLessThan(LocalDate dateDebut, LocalDate dateFin);
}
