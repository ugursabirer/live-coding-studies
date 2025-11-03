package advancedLevel.problem02HotelReservation.models;

public class StandartRoom extends Room {
    public StandartRoom(String roomNumber) {
        super(roomNumber, RoomType.STANDARD, 500.0, 2);
        setupAmenities();
    }

    @Override
    protected void setupAmenities() {
        amenities.add("WiFi");
        amenities.add("TV");
        amenities.add("Klima");
    }
}
