package advancedLevel.problem02HotelReservation.models;

import java.util.ArrayList;
import java.util.List;

public abstract class Room {
    protected String roomNumber;
    protected RoomType roomType;
    protected double pricePerNight;
    protected int maxGuests;
    protected List<String> amenities;

    public Room(String roomNumber, RoomType roomType, double pricePerNight, int maxGuests) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.amenities = new ArrayList<>();
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    protected abstract void setupAmenities();

    @Override
    public String toString() {
        return String.format("%s Room #%s - %.2f TL/gece (Max %d kişi) - Özellikler: %s", roomType, roomNumber, pricePerNight, maxGuests, amenities);
    }
}
