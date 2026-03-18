import java.util.*;

// Represents a confirmed reservation
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private double totalCost;

    public Reservation(String reservationId, String guestName, String roomType, double totalCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.totalCost = totalCost;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getTotalCost() {
        return totalCost;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType +
                ", Total Cost: ₹" + totalCost;
    }
}

// Maintains booking history
class BookingHistory {

    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    // Retrieve all reservations
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations); // return copy to protect data
    }
}

// Generates reports from booking history
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("=== Booking History ===");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummaryReport(List<Reservation> reservations) {
        int totalBookings = reservations.size();
        double totalRevenue = 0.0;

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : reservations) {
            totalRevenue += r.getTotalCost();

            roomTypeCount.put(
                    r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("\n=== Booking Summary Report ===");
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: ₹" + totalRevenue);

        System.out.println("Room Type Distribution:");
        for (Map.Entry<String, Integer> entry : roomTypeCount.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Main class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        // Initialize components
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("RES101", "Alice", "Deluxe", 5000);
        Reservation r2 = new Reservation("RES102", "Bob", "Suite", 8000);
        Reservation r3 = new Reservation("RES103", "Charlie", "Standard", 3000);

        // Add to history (in order)
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Admin retrieves booking history
        List<Reservation> storedReservations = history.getAllReservations();

        // Display all bookings
        reportService.displayAllBookings(storedReservations);

        // Generate summary report
        reportService.generateSummaryReport(storedReservations);
    }
}