package advancedLevel.problem02HotelReservation.observer;

import advancedLevel.problem02HotelReservation.models.Reservation;

public class EmailNotificationObserver implements ReservationObserver {
    @Override
    public void onReservationCreated(Reservation reservation) {
        System.out.println("EMAIL: Sayın " + reservation.getGuestName() +
                ", rezervasyonunuz oluşturuldu. ID: " + reservation.getReservationId().substring(0, 8));
    }

    @Override
    public void onReservationConfirmed(Reservation reservation) {
        System.out.println("EMAIL: Sayın " + reservation.getGuestName() +
                ", rezervasyonunuz onaylandı! Giriş: " + reservation.getCheckInDate());
    }

    @Override
    public void onReservationCancelled(Reservation reservation) {
        System.out.println("EMAIL: Sayın " + reservation.getGuestName() +
                ", rezervasyonunuz iptal edildi.");
    }
}
