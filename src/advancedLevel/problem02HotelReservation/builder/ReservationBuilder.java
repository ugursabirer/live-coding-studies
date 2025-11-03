package advancedLevel.problem02HotelReservation.builder;

import advancedLevel.problem02HotelReservation.models.Reservation;
import advancedLevel.problem02HotelReservation.models.Room;

import java.time.LocalDate;

public class ReservationBuilder {
    private String guestName;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public ReservationBuilder setGuestName(String guestName) {
        this.guestName = guestName;
        return this;
    }

    public ReservationBuilder setRoom(Room room) {
        this.room = room;
        return this;
    }

    public ReservationBuilder setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
        return this;
    }

    public ReservationBuilder setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
        return this;
    }

    public Reservation build() {
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new IllegalArgumentException("Misafir adı boş olamaz!");
        }

        if (room == null) {
            throw new IllegalArgumentException("Oda seçilmeli!");
        }

        if (checkInDate == null || checkOutDate == null) {
            throw new IllegalArgumentException("Tarihler belirtilmeli!");
        }

        if (checkInDate.isAfter(checkOutDate) || checkInDate.isEqual(checkOutDate)) {
            throw new IllegalArgumentException("Check-out tarihi check-in'den sonra olmalı!");
        }

        if (checkInDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Geçmiş tarihe rezervasyon yapılamaz!");
        }

        return new Reservation(guestName, room, checkInDate, checkOutDate);
    }
}
