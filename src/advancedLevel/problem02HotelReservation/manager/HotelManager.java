package advancedLevel.problem02HotelReservation.manager;

import advancedLevel.problem02HotelReservation.models.Reservation;
import advancedLevel.problem02HotelReservation.models.ReservationStatus;
import advancedLevel.problem02HotelReservation.models.Room;
import advancedLevel.problem02HotelReservation.observer.ReservationObserver;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HotelManager {
    private static volatile HotelManager instance;

    private final Map<String, Room> rooms;
    private final Map<String, Reservation> reservations;
    private final List<ReservationObserver> observers;

    public HotelManager() {
        this.rooms = new ConcurrentHashMap<>();
        this.reservations = new ConcurrentHashMap<>();
        this.observers = new ArrayList<>();
    }

    public static HotelManager getInstance() {
        if (instance == null) {
            synchronized (HotelManager.class) {
                if (instance == null) {
                    instance = new HotelManager();
                }
            }
        }
        return instance;
    }

    public void addObserver(ReservationObserver observer) {
        observers.add(observer);
        System.out.println("Observer eklendi: " + observer.getClass().getSimpleName());
    }

    public void removeObserver(ReservationObserver observer) {
        observers.remove(observer);
    }

    private void notifyReservationCreated(Reservation reservation) {
        for (ReservationObserver observer : observers) {
            observer.onReservationCreated(reservation);
        }
    }

    private void notifyReservationConfirmed(Reservation reservation) {
        for (ReservationObserver observer : observers) {
            observer.onReservationConfirmed(reservation);
        }
    }

    private void notifyReservationCancelled(Reservation reservation) {
        for (ReservationObserver observer : observers) {
            observer.onReservationCancelled(reservation);
        }
    }

    public void addRoom(Room room) {
        rooms.put(room.getRoomNumber(), room);
        System.out.println("Oda eklendi: " + room);
    }

    public Room getRoom(String roomNumber) {
        return rooms.get(roomNumber);
    }

    public void createReservation(Reservation reservation) {
        if (!isRoomAvailable(reservation.getRoom(), reservation.getCheckInDate(), reservation.getCheckOutDate())) {
            System.out.println("Oda bu tarihler için müsait değil!");
            return;
        }

        reservations.put(reservation.getReservationId(), reservation);
        System.out.println("Rezervasyon oluşturuldu: " + reservation);

        notifyReservationCreated(reservation);
    }

    public void confirmReservation(String reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null) {
            System.out.println("Rezervasyon bulunamadı!");
            return;
        }

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            System.out.println("Sadece PENDING durumdaki rezervasyonlar onaylanabilir!");
            return;
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        System.out.println("Rezervasyon onaylandı: " + reservationId.substring(0, 8));

        notifyReservationConfirmed(reservation);
    }

    public void cancelReservation(String reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null) {
            System.out.println("Rezervasyon bulunamadı!");
            return;
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            System.out.println("Rezervasyon zaten iptal edilmiş!");
            return;
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        System.out.println("Rezervasyon iptal edildi: " + reservationId.substring(0, 8));

        notifyReservationCancelled(reservation);
    }

    public boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        for (Reservation reservation : reservations.values()) {
            if (reservation.getStatus() == ReservationStatus.CANCELLED) {
                continue;
            }

            if (!reservation.getRoom().getRoomNumber().equals(room.getRoomNumber())) {
                continue;
            }

            boolean noOverlap = checkOut.isBefore(reservation.getCheckInDate()) ||
                    checkOut.isEqual(reservation.getCheckInDate()) ||
                    checkIn.isAfter(reservation.getCheckOutDate()) ||
                    checkIn.isEqual(reservation.getCheckOutDate());

            if (!noOverlap) {
                return false;
            }
        }

        return true;
    }

    public void displayAllReservations() {
        System.out.println("\nTÜM REZERVASYONLAR:");
        if (reservations.isEmpty()) {
            System.out.println("Henüz rezervasyon yok.");
        } else {
            reservations.values().forEach(System.out::println);
        }
    }

    public void displayAllRooms() {
        System.out.println("\nTÜM ODALAR:");
        rooms.values().forEach(System.out::println);
    }
}
