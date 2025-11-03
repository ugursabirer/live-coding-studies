package advancedLevel.problem02HotelReservation.factory;

import advancedLevel.problem02HotelReservation.models.*;

public class RoomFactory {
    public Room createRoom(RoomType type, String roomNumber) {
        switch (type) {
            case STANDARD:
                return new StandartRoom(roomNumber);
            case DELUXE:
                return new DeluxeRoom(roomNumber);
            case SUITE:
                return new SuiteRoom(roomNumber);
            default:
                throw new IllegalArgumentException("Geçersiz oda tipi: " + type);
        }
    }

    public Room createRoom(RoomType type) {
        String randomNumber = String.valueOf((int) (Math.random() * 900) + 100);
        return createRoom(type, randomNumber);
    }
}
