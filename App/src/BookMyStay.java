import java.util.*;

public class BookMyStayApp {

    static abstract class Room {
        int beds;
        int size;
        double price;

        Room(int beds, int size, double price) {
            this.beds = beds;
            this.size = size;
            this.price = price;
        }
    }

    static class Reservation {
        String guestName;
        String roomType;

        Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    static class RoomInventory {

        HashMap<String, Integer> inventory = new HashMap<>();

        RoomInventory() {
            inventory.put("Single", 5);
            inventory.put("Double", 3);
            inventory.put("Suite", 2);
        }

        int getAvailability(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }

        void decrement(String roomType) {
            inventory.put(roomType, inventory.get(roomType) - 1);
        }
    }

    static class BookingQueue {

        Queue<Reservation> queue = new LinkedList<>();

        void addRequest(Reservation r) {
            queue.offer(r);
        }

        Reservation getNext() {
            return queue.poll();
        }

        boolean hasRequests() {
            return !queue.isEmpty();
        }
    }

    static class RoomAllocationService {

        HashMap<String, Set<String>> allocatedRooms = new HashMap<>();
        Set<String> usedRoomIds = new HashSet<>();

        void allocate(Reservation r, RoomInventory inventory) {

            if (inventory.getAvailability(r.roomType) <= 0) {
                System.out.println("No rooms available for " + r.roomType);
                return;
            }

            String roomId;
            do {
                roomId = r.roomType.substring(0, 1).toUpperCase() + (100 + new Random().nextInt(900));
            } while (usedRoomIds.contains(roomId));

            usedRoomIds.add(roomId);

            allocatedRooms.putIfAbsent(r.roomType, new HashSet<>());
            allocatedRooms.get(r.roomType).add(roomId);

            inventory.decrement(r.roomType);

            System.out.println("Reservation Confirmed");
            System.out.println("Guest: " + r.guestName);
            System.out.println("Room Type: " + r.roomType);
            System.out.println("Room ID: " + roomId);
            System.out.println();
        }
    }

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();
        RoomAllocationService service = new RoomAllocationService();

        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Double"));
        queue.addRequest(new Reservation("Charlie", "Suite"));

        while (queue.hasRequests()) {
            Reservation r = queue.getNext();
            service.allocate(r, inventory);
        }
    }
}