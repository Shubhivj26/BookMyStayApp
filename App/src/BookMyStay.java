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

        void display() {
            System.out.println("Guest: " + guestName + " requested " + roomType + " room");
        }
    }

    static class BookingRequestQueue {

        Queue<Reservation> queue = new LinkedList<>();

        void addRequest(Reservation r) {
            queue.offer(r);
        }

        void displayRequests() {
            System.out.println("Booking Requests in Queue:");
            System.out.println();

            for (Reservation r : queue) {
                r.display();
            }
        }
    }

    public static void main(String[] args) {

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        bookingQueue.addRequest(new Reservation("Alice", "Single"));
        bookingQueue.addRequest(new Reservation("Bob", "Double"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite"));

        bookingQueue.displayRequests();
    }
}