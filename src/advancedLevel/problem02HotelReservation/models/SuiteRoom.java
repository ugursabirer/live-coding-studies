package advancedLevel.problem02HotelReservation.models;

public class SuiteRoom extends Room {
    public SuiteRoom(String roomNumber) {
        super(roomNumber, RoomType.SUITE, 2500.0, 4);
        setupAmenities();
    }

    @Override
    protected void setupAmenities() {
        amenities.add("WiFi");
        amenities.add("Smart TV");
        amenities.add("Klima");
        amenities.add("Minibar");
        amenities.add("Jakuzi");
        amenities.add("Deniz Manzarası");
        amenities.add("Oturma Odası");
    }
}
