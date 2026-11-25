package tn.esprit.spring.service.Impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entity.CategorieVehicule;
import tn.esprit.spring.entity.Reservation;
import tn.esprit.spring.entity.Vehicule;
import tn.esprit.spring.repository.ReservationRepository;
import tn.esprit.spring.repository.VehiculeRepository;
import tn.esprit.spring.service.Interface.IReservationService;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationServiceIMPL implements IReservationService {

    ReservationRepository reservationRepository;
    VehiculeRepository vehiculeRepository;

    @Transactional
    @Override
    public void affecterVehiculeReservation(String matricule, String marque, CategorieVehicule cv,
                                            LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début doit précéder la date de fin");
        }

        // Données
        Vehicule vehicule = vehiculeRepository.findByImmatriculationAndMarqueAndCategorie(matricule, marque, cv);
        if (vehicule == null) {
            throw new IllegalArgumentException("Aucun véhicule " + matricule + " / " + marque + " / " + cv);
        }
        List<Reservation> reservations =
                reservationRepository.findByDateDebutGreaterThanAndDateFinLessThan(dateDebut, dateFin);

        // Affectation (côté propriétaire de la relation : Reservation.vehicule)
        for (Reservation reservation : reservations) {
            reservation.setVehicule(vehicule);
            reservationRepository.save(reservation);
        }
    }

    @Transactional
    @Override
    public Reservation desaffecterReservationVehicule(Long idReservation) {
       Reservation reservation = reservationRepository.findById(idReservation).orElse(null);
       assert reservation != null;
       reservation.setVehicule(null);
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation ajouterContartReservation(Reservation r) {
        return reservationRepository.save(r);


    }

    @Override
    public Reservation creer(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation obtenirParId(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    @Override
    public List<Reservation> listerTous() {
        return (List<Reservation>) reservationRepository.findAll();
    }

    @Override
    public Reservation mettreAJour(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public void supprimer(Long id) {
reservationRepository.deleteById(id);
    }
}
