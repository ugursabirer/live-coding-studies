package advancedLevel.problem02HotelReservation;

import advancedLevel.problem02HotelReservation.builder.ReservationBuilder;
import advancedLevel.problem02HotelReservation.factory.RoomFactory;
import advancedLevel.problem02HotelReservation.manager.HotelManager;
import advancedLevel.problem02HotelReservation.models.Reservation;
import advancedLevel.problem02HotelReservation.models.Room;
import advancedLevel.problem02HotelReservation.models.RoomType;
import advancedLevel.problem02HotelReservation.observer.EmailNotificationObserver;
import advancedLevel.problem02HotelReservation.observer.LoggerObserver;
import advancedLevel.problem02HotelReservation.observer.SMSNotificationObserver;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("🏨 OTEL REZERVASYON SİSTEMİ\n");

        HotelManager manager = HotelManager.getInstance();

        manager.addObserver(new EmailNotificationObserver());
        manager.addObserver(new SMSNotificationObserver());
        manager.addObserver(new LoggerObserver());

        System.out.println("\n--- ODALARI OLUŞTUR ---");
        RoomFactory factory = new RoomFactory();
        Room standard101 = factory.createRoom(RoomType.STANDARD, "101");
        Room deluxe201 = factory.createRoom(RoomType.DELUXE, "201");
        Room suite301 = factory.createRoom(RoomType.SUITE, "301");

        manager.addRoom(standard101);
        manager.addRoom(deluxe201);
        manager.addRoom(suite301);

        manager.displayAllRooms();

        System.out.println("\n--- REZERVASYON 1 OLUŞTUR ---");
        Reservation res1 = new ReservationBuilder()
                .setGuestName("Ahmet Yılmaz")
                .setRoom(deluxe201)
                .setCheckInDate(LocalDate.of(2025, 11, 10))
                .setCheckOutDate(LocalDate.of(2025, 11, 15))
                .build();

        manager.createReservation(res1);

        System.out.println("\n--- REZERVASYON 2 OLUŞTUR (AYNI ODA - FARKLI TARİH) ---");
        Reservation res2 = new ReservationBuilder()
                .setGuestName("Ayşe Demir")
                .setRoom(deluxe201)
                .setCheckInDate(LocalDate.of(2025, 11, 20))
                .setCheckOutDate(LocalDate.of(2025, 11, 25))
                .build();

        manager.createReservation(res2);

        System.out.println("\n--- REZERVASYON 3 OLUŞTUR (ÇAKIŞMA TESTİ!) ---");
        Reservation res3 = new ReservationBuilder()
                .setGuestName("Mehmet Kaya")
                .setRoom(deluxe201)
                .setCheckInDate(LocalDate.of(2025, 11, 12))
                .setCheckOutDate(LocalDate.of(2025, 11, 17))
                .build();

        manager.createReservation(res3);

        System.out.println("\n--- REZERVASYON ONAYLA ---");
        manager.confirmReservation(res1.getReservationId());

        System.out.println("\n--- REZERVASYON İPTAL ET ---");
        manager.cancelReservation(res2.getReservationId());

        System.out.println("\n--- ŞİMDİ res3'Ü TEKRAR DENE (res1 hala aktif, başarısız olmalı) ---");
        manager.createReservation(res3);

        System.out.println("\n--- res1'İ İPTAL ET ---");
        manager.cancelReservation(res1.getReservationId());

        System.out.println("\n--- ARTIK res3 OLUŞTURULUR (çakışma yok) ---");
        manager.createReservation(res3);

        System.out.println("\n--- SON DURUM ---");
        manager.displayAllReservations();
    }
}
