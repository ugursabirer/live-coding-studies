package advancedLevel.problem02HotelReservation.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Reservation {
    private String reservationId;
    private String guestName;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;
    private ReservationStatus status;

    public Reservation(String guestName, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        this.reservationId = UUID.randomUUID().toString();
        this.guestName = guestName;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = calculateTotalPrice();
        this.status = ReservationStatus.PENDING;
    }

    private double calculateTotalPrice() {
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return nights * room.getPricePerNight();
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Rezervasyon[%s] - %s - %s - %s to %s - %.2f TL - Status: %s",
                reservationId.substring(0, 8),
                guestName,
                room.getRoomType(),
                checkInDate,
                checkOutDate,
                totalPrice,
                status);
    }
}
