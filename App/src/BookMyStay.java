import java.util.*;

// Custom Exception for invalid booking scenarios
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Represents a simple inventory system
class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();

        // Initial room setup
        roomAvailability.put("Standard", 2);
        roomAvailability.put("Deluxe", 1);
        roomAvailability.put("Suite", 0);
    }

    // Validate and reserve room
    public void reserveRoom(String roomType) throws InvalidBookingException {

        // Validate room type
        if (!roomAvailability.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        int available = roomAvailability.get(roomType);

        // Validate availability
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }

        // Guard against negative inventory
        roomAvailability.put(roomType, available - 1);
    }

    public void displayInventory() {
        System.out.println("Current Room Availability:");
        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Booking service with validation
class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void bookRoom(String guestName, String roomType) {

        try {
            // Input validation
            if (guestName == null || guestName.trim().isEmpty()) {
                throw new InvalidBookingException("Guest name cannot be empty");
            }

            // Attempt booking
            inventory.reserveRoom(roomType);

            System.out.println("Booking successful for " + guestName + " in " + roomType + " room.");

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
}

// Main class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Initial inventory
        inventory.displayInventory();
        System.out.println();

        // Valid booking
        bookingService.bookRoom("Alice", "Standard");

        // Invalid room type
        bookingService.bookRoom("Bob", "Premium");

        // No availability case
        bookingService.bookRoom("Charlie", "Suite");

        // Empty guest name
        bookingService.bookRoom("", "Deluxe");

        // Another valid booking
        bookingService.bookRoom("David", "Deluxe");

        // Attempt overbooking
        bookingService.bookRoom("Eve", "Deluxe");

        System.out.println();
        inventory.displayInventory();
    }
}