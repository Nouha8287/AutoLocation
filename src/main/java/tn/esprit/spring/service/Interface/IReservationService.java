package tn.esprit.spring.service.Interface;

import tn.esprit.spring.entity.CategorieVehicule;
import tn.esprit.spring.entity.Reservation;

import java.time.LocalDate;

import java.util.List;

public interface IReservationService {
    Reservation creer(Reservation reservation);

    Reservation obtenirParId(Long id);

    List<Reservation> listerTous();

    Reservation mettreAJour(Reservation reservation);

    void supprimer(Long id);

    Reservation ajouterContartReservation(Reservation r);
    Reservation desaffecterReservationVehicule(Long idReservation);

    void affecterVehiculeReservation(String matricule, String marque, CategorieVehicule cv,
                                     LocalDate dateDebut, LocalDate dateFin);
}
