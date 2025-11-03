package advancedLevel.problem02HotelReservation.models;

public class DeluxeRoom extends Room {
    public DeluxeRoom(String roomNumber) {
        super(roomNumber, RoomType.DELUXE, 1000.0, 3);
        setupAmenities();
    }

    @Override
    protected void setupAmenities() {
        amenities.add("WiFi");
        amenities.add("Smart TV");
        amenities.add("Klima");
        amenities.add("Minibar");
        amenities.add("Balkon");
    }
}
