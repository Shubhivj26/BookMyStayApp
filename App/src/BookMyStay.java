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

    public static void main(String[] args) {

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        System.out.println("Hotel Room Initialization");
        System.out.println();

        single.display("Single", singleAvailable);
        doubleRoom.display("Double", doubleAvailable);
        suite.display("Suite", suiteAvailable);
    }
}