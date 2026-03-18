import java.io.*;
import java.util.*;

// Reservation model (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// Wrapper class to persist full system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    List<Reservation> bookings;
    Map<String, Integer> inventory;

    public SystemState(List<Reservation> bookings, Map<String, Integer> inventory) {
        this.bookings = bookings;
        this.inventory = inventory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state to file
    public void save(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load state from file
    public SystemState load() {
        File file = new File(FILE_NAME);

        // Handle missing file
        if (!file.exists()) {
            System.out.println("No previous state found. Starting fresh.");
            return getDefaultState();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            SystemState state = (SystemState) ois.readObject();
            System.out.println("System state restored successfully.");
            return state;

        } catch (Exception e) {
            // Handle corrupted file
            System.out.println("Error loading state. Starting with safe defaults.");
            return getDefaultState();
        }
    }

    // Default safe state
    private SystemState getDefaultState() {
        List<Reservation> bookings = new ArrayList<>();
        Map<String, Integer> inventory = new HashMap<>();

        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);

        return new SystemState(bookings, inventory);
    }
}

// Main class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        PersistenceService persistenceService = new PersistenceService();

        // STEP 1: Load previous state
        SystemState state = persistenceService.load();

        List<Reservation> bookings = state.bookings;
        Map<String, Integer> inventory = state.inventory;

        System.out.println("\n--- Current System State ---");

        System.out.println("Bookings:");
        for (Reservation r : bookings) {
            System.out.println(r);
        }

        System.out.println("\nInventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // STEP 2: Simulate new booking
        System.out.println("\nAdding new booking...");

        Reservation newBooking = new Reservation("RES301", "Alice", "Standard");
        bookings.add(newBooking);

        // Update inventory safely
        inventory.put("Standard", inventory.getOrDefault("Standard", 0) - 1);

        // STEP 3: Save updated state
        persistenceService.save(new SystemState(bookings, inventory));

        System.out.println("\nUpdated state saved. Restart program to see recovery.");
    }
}