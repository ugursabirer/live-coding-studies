package advancedLevel.problem02HotelReservation.observer;

import advancedLevel.problem02HotelReservation.models.Reservation;

public class SMSNotificationObserver implements ReservationObserver {
    @Override
    public void onReservationCreated(Reservation reservation) {
        System.out.println("SMS: Rezervasyon oluşturuldu. Tutar: " +
                reservation.getTotalPrice() + " TL");
    }

    @Override
    public void onReservationConfirmed(Reservation reservation) {
        System.out.println("SMS: Rezervasyon onaylandı. " +
                reservation.getRoom().getRoomType() + " #" + reservation.getRoom().getRoomNumber());
    }

    @Override
    public void onReservationCancelled(Reservation reservation) {
        System.out.println("SMS: Rezervasyon iptal edildi. İade işlemi başlatıldı.");
    }
}
