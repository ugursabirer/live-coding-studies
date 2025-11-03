package advancedLevel.problem02HotelReservation.observer;

import advancedLevel.problem02HotelReservation.models.Reservation;

public class LoggerObserver implements ReservationObserver {
    @Override
    public void onReservationCreated(Reservation reservation) {
        System.out.println("LOG: [CREATE] " + reservation);
    }

    @Override
    public void onReservationConfirmed(Reservation reservation) {
        System.out.println("LOG: [CONFIRM] " + reservation);
    }

    @Override
    public void onReservationCancelled(Reservation reservation) {
        System.out.println("LOG: [CANCEL] " + reservation);
    }
}
