package advancedLevel.problem02HotelReservation.observer;

import advancedLevel.problem02HotelReservation.models.Reservation;

public interface ReservationObserver {
    void onReservationCreated(Reservation reservation);
    void onReservationConfirmed(Reservation reservation);
    void onReservationCancelled(Reservation reservation);
}
