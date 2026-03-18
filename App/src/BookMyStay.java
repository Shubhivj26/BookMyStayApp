import java.util.*;

// Booking Request Model
class BookingRequest {
    String guestName;
    String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Thread-safe Booking Queue
class BookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
        notifyAll();
    }

    public synchronized BookingRequest getRequest() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return queue.poll();
    }
}

// Shared Inventory (critical resource)
class RoomInventory {

    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Standard", 2);
        rooms.put("Deluxe", 1);
    }

    // Critical Section (Thread-Safe)
    public synchronized boolean allocateRoom(String roomType) {

        int available = rooms.getOrDefault(roomType, 0);

        if (available > 0) {
            // Simulate processing delay (to expose race conditions if unsynchronized)
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            rooms.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void display() {
        System.out.println("\nFinal Inventory:");
        for (Map.Entry<String, Integer> entry : rooms.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Worker Thread (Concurrent Booking Processor)
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest request = queue.getRequest();

            boolean success = inventory.allocateRoom(request.roomType);

            if (success) {
                System.out.println(Thread.currentThread().getName() +
                        " SUCCESS: " + request.guestName +
                        " booked " + request.roomType);
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " FAILED: No " + request.roomType +
                        " room for " + request.guestName);
            }

            // Stop condition (for demo)
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
        }
    }
}

// Main Class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) throws InterruptedException {

        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();

        // Create worker threads
        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);
        BookingProcessor t3 = new BookingProcessor(queue, inventory);

        t1.setName("Processor-1");
        t2.setName("Processor-2");
        t3.setName("Processor-3");

        t1.start();
        t2.start();
        t3.start();

        // Simulate concurrent guest requests
        queue.addRequest(new BookingRequest("Alice", "Standard"));
        queue.addRequest(new BookingRequest("Bob", "Standard"));
        queue.addRequest(new BookingRequest("Charlie", "Standard")); // should fail (only 2 available)

        queue.addRequest(new BookingRequest("David", "Deluxe"));
        queue.addRequest(new BookingRequest("Eve", "Deluxe")); // should fail (only 1 available)

        // Allow processing time
        Thread.sleep(2000);

        // Stop threads
        t1.interrupt();
        t2.interrupt();
        t3.interrupt();

        inventory.display();
    }
}