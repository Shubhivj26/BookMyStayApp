import java.util.*;

// Custom Exception
class CancellationException extends Exception {
    public CancellationException(String message) {
        super(message);
    }
}

// Reservation model
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isActive;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isActive = true;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }
    public boolean isActive() { return isActive; }

    public void cancel() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", RoomType: " + roomType +
                ", RoomID: " + roomId +
                ", Status: " + (isActive ? "ACTIVE" : "CANCELLED");
    }
}

// Inventory system
class RoomInventory {

    private Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Standard", 1);
        availability.put("Deluxe", 1);
    }

    public void increment(String roomType) {
        availability.put(roomType, availability.getOrDefault(roomType, 0) + 1);
    }

    public void display() {
        System.out.println("Inventory स्थिति:");
        for (Map.Entry<String, Integer> entry : availability.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Booking storage (acts like history + active records)
class BookingStore {

    private Map<String, Reservation> reservations;

    public BookingStore() {
        reservations = new HashMap<>();
    }

    public void addReservation(Reservation r) {
        reservations.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return reservations.get(id);
    }

    public void displayAll() {
        for (Reservation r : reservations.values()) {
            System.out.println(r);
        }
    }
}

// Cancellation service with rollback
class CancellationService {

    private BookingStore store;
    private RoomInventory inventory;

    // Stack for rollback tracking
    private Stack<String> rollbackStack;

    public CancellationService(BookingStore store, RoomInventory inventory) {
        this.store = store;
        this.inventory = inventory;
        this.rollbackStack = new Stack<>();
    }

    public void cancelBooking(String reservationId) {

        try {
            Reservation r = store.getReservation(reservationId);

            // Validate existence
            if (r == null) {
                throw new CancellationException("Reservation not found: " + reservationId);
            }

            // Validate active status
            if (!r.isActive()) {
                throw new CancellationException("Booking already cancelled: " + reservationId);
            }

            // Step 1: Push roomId to rollback stack
            rollbackStack.push(r.getRoomId());

            // Step 2: Restore inventory
            inventory.increment(r.getRoomType());

            // Step 3: Mark booking cancelled
            r.cancel();

            System.out.println("Cancellation successful for " + reservationId);

        } catch (CancellationException e) {
            System.out.println("Cancellation failed: " + e.getMessage());
        }
    }

    public void displayRollbackStack() {
        System.out.println("Rollback Stack (recent releases): " + rollbackStack);
    }
}

// Main class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        // Setup
        BookingStore store = new BookingStore();
        RoomInventory inventory = new RoomInventory();
        CancellationService service = new CancellationService(store, inventory);

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("RES201", "Alice", "Standard", "S1");
        Reservation r2 = new Reservation("RES202", "Bob", "Deluxe", "D1");

        store.addReservation(r1);
        store.addReservation(r2);

        System.out.println("=== Initial Bookings ===");
        store.displayAll();
        inventory.display();

        System.out.println();

        // Valid cancellation
        service.cancelBooking("RES201");

        // Duplicate cancellation
        service.cancelBooking("RES201");

        // Invalid ID
        service.cancelBooking("RES999");

        System.out.println();

        System.out.println("=== Final State ===");
        store.displayAll();
        inventory.display();

        System.out.println();
        service.displayRollbackStack();
    }
}