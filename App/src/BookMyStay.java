import java.util.HashMap;

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

    static class SingleRoom extends Room {
        SingleRoom() {
            super(1, 250, 1500.0);
        }
    }

    static class DoubleRoom extends Room {
        DoubleRoom() {
            super(2, 400, 2500.0);
        }
    }

    static class SuiteRoom extends Room {
        SuiteRoom() {
            super(3, 750, 5000.0);
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
            return inventory.get(roomType);
        }

        void updateAvailability(String roomType, int count) {
            inventory.put(roomType, count);
        }

        void displayInventory() {
            System.out.println("Hotel Room Inventory");
            System.out.println();

            for (String type : inventory.keySet()) {
                System.out.println(type + " Rooms Available: " + inventory.get(type));
            }
        }
    }

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        inventory.displayInventory();
    }
}