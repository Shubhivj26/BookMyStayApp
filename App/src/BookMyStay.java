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

        void display(String type, int available) {
            System.out.println(type + " Room:");
            System.out.println("Beds: " + beds);
            System.out.println("Size: " + size + " sqft");
            System.out.println("Price per night: " + price);
            System.out.println("Available: " + available);
            System.out.println();
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
            return inventory.getOrDefault(roomType, 0);
        }
    }

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        System.out.println("Available Rooms");
        System.out.println();

        if (inventory.getAvailability("Single") > 0) {
            single.display("Single", inventory.getAvailability("Single"));
        }

        if (inventory.getAvailability("Double") > 0) {
            doubleRoom.display("Double", inventory.getAvailability("Double"));
        }

        if (inventory.getAvailability("Suite") > 0) {
            suite.display("Suite", inventory.getAvailability("Suite"));
        }
    }
}