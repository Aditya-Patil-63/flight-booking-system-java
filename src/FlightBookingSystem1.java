import java.util.*; 

import java.text.SimpleDateFormat; 

import java.text.ParseException; 

 

abstract class User { 

    protected String username; 

    protected String password; 

 

    public User(String username, String password) { 

        this.username = username; 

        this.password = password; 

    } 

 

    public abstract void showOptions(); 

 

    public String getUsername() { 

        return username; 

    } 

} 

 

class Customer extends User { 

    public Customer(String username, String password) { 

        super(username, password); 

    } 

 

    @Override 

    public void showOptions() { 

        System.out.println("1. Search Flights\n2. Book Flight\n3. Cancel Booking\n4. View Bookings\n5. Sort Flights"); 

    } 

} 

 

class Admin extends User { 

    public Admin(String username, String password) { 

        super(username, password); 

    } 

 

    @Override 

    public void showOptions() { 

        System.out.println("1. Add Flight\n2. Remove Flight\n3. View All Flights\n4. Update Flight Price\n5. Update Flight Seats\n6. Search Flight by ID"); 

    } 

} 

 

abstract class Flight { 

    protected String flightId, origin, destination, date; 

    protected int seatsAvailable; 

    protected double basePrice; 

 

    public Flight(String flightId, String origin, String destination, String date, int seatsAvailable, double basePrice) { 

        this.flightId = flightId; 

        this.origin = origin; 

        this.destination = destination; 

        this.date = date; 

        this.seatsAvailable = seatsAvailable; 

        this.basePrice = basePrice; 

    } 

 

    public boolean bookSeat(int numberOfSeats) { 

        if (seatsAvailable >= numberOfSeats) { 

            seatsAvailable -= numberOfSeats; 

            return true; 

        } 

        return false; 

    } 

 

    public void addSeats(int number) { 

        this.seatsAvailable += number; 

    } 

 

    public void updatePrice(double price) { 

        this.basePrice = price; 

    } 

 

    public String getDetails() { 

        return flightId + ": " + origin + " to " + destination + " on " + date + ", Available Seats: " + seatsAvailable + ", Price: $" + basePrice; 

    } 

 

    public String getFlightId() { 

        return flightId; 

    } 

 

    public String getDestination() { 

        return destination; 

    } 

 

    public String getDate() { 

        return date; 

    } 

 

    public int getSeatsAvailable() { 

        return seatsAvailable; 

    } 

 

    public double getBasePrice() { 

        return basePrice; 

    } 

 

    public abstract String getType(); 

} 

 

class DomesticFlight extends Flight { 

    public DomesticFlight(String flightId, String origin, String destination, String date, int seatsAvailable, double basePrice) { 

        super(flightId, origin, destination, date, seatsAvailable, basePrice); 

    } 

 

    @Override 

    public String getType() { 

        return "Domestic"; 

    } 

} 

 

class InternationalFlight extends Flight { 

    public InternationalFlight(String flightId, String origin, String destination, String date, int seatsAvailable, double basePrice) { 

        super(flightId, origin, destination, date, seatsAvailable, basePrice); 

    } 

 

    @Override 

    public String getType() { 

        return "International"; 

    } 

} 

 

class FlightManager { 

    private List<Flight> flights = new ArrayList<>(); 

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); 

 

    public boolean addFlight(Flight flight) { 

        for (Flight f : flights) { 

            if (f.getFlightId().equals(flight.getFlightId())) { 

                return false; 

            } 

        } 

        flights.add(flight); 

        return true; 

    } 

 

    public void removeFlight(String flightId) { 

        flights.removeIf(f -> f.getFlightId().equals(flightId)); 

    } 

 

    public void listFlights() { 

        for (Flight f : flights) { 

            System.out.println(f.getDetails()); 

        } 

    } 

 

    public Flight searchFlight(String flightId) { 

        for (Flight f : flights) { 

            if (f.getFlightId().equals(flightId)) return f; 

        } 

        return null; 

    } 

 

    public List<Flight> filterFlights(String destination, String date, String type) { 

        List<Flight> filtered = new ArrayList<>(); 

        for (Flight f : flights) { 

            if ((destination.isEmpty() || f.getDestination().equalsIgnoreCase(destination)) && 

                    (date.isEmpty() || f.getDate().equalsIgnoreCase(date)) && 

                    (type.isEmpty() || f.getType().equalsIgnoreCase(type))) { 

                filtered.add(f); 

            } 

        } 

        return filtered; 

    } 

 

    public void sortFlightsByDate() { 

        flights.sort(Comparator.comparing(f -> { 

            try { 

                return sdf.parse(f.getDate()); 

            } catch (ParseException e) { 

                return new Date(0); 

            } 

        })); 

        listFlights(); 

    } 

 

    public void sortFlightsBySeats() { 

        flights.sort(Comparator.comparingInt(Flight::getSeatsAvailable)); 

        listFlights(); 

    } 

} 

 

class Booking { 

    private static int bookingCounter = 1; 

    private final int bookingId; 

    private final String username; 

    private final Flight flight; 

    private final int seatCount; 

    private final String timestamp; 

    private final double price; 

 

    public Booking(String username, Flight flight, int seatCount) { 

        this.username = username; 

        this.flight = flight; 

        this.seatCount = seatCount; 

        this.bookingId = bookingCounter++; 

        this.timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()); 

        this.price = seatCount * flight.getBasePrice(); 

    } 

 

    public String getBookingDetails() { 

        return "Booking ID: " + bookingId + ", User: " + username + ", Flight: " + flight.getFlightId() + 

                ", Seats: " + seatCount + ", Time: " + timestamp + ", Total Price: $" + price; 

    } 

 

    public String getUsername() { 

        return username; 

    } 

 

    public int getBookingId() { 

        return bookingId; 

    } 

} 

 

class Payment { 

    public static boolean processPayment(String method) { 

        List<String> validMethods = Arrays.asList("credit", "debit", "upi"); 

        return validMethods.contains(method.toLowerCase()); 

    } 

} 

 

class BookingManager { 

    private List<Booking> bookings = new ArrayList<>(); 

 

    public void bookFlight(String username, Flight flight, String method, int passengers) { 

        if (passengers <= 0 || flight.getSeatsAvailable() < passengers) { 

            System.out.println("Invalid or insufficient seats."); 

            return; 

        } 

        if (flight.bookSeat(passengers)) { 

            if (Payment.processPayment(method)) { 

                bookings.add(new Booking(username, flight, passengers)); 

                System.out.println("Booking successful!"); 

            } else { 

                flight.addSeats(passengers); 

                System.out.println("Payment failed. Booking cancelled."); 

            } 

        } 

    } 

 

    public void cancelBooking(int bookingId) { 

        bookings.removeIf(b -> b.getBookingId() == bookingId); 

    } 

 

    public void viewBookings(String username) { 

        for (Booking b : bookings) { 

            if (b.getUsername().equals(username)) { 

                System.out.println(b.getBookingDetails()); 

            } 

        } 

    } 

} 

 

public class FlightBookingSystem1 { 

    public static void main(String[] args) { 

        Scanner scanner = new Scanner(System.in); 

        FlightManager flightManager = new FlightManager(); 

        BookingManager bookingManager = new BookingManager(); 

 

        flightManager.addFlight(new DomesticFlight("F101", "Delhi", "Mumbai", "10-04-2025", 5, 150.0)); 

        flightManager.addFlight(new InternationalFlight("F202", "Delhi", "Dubai", "12-04-2025", 2, 500.0)); 

        flightManager.addFlight(new DomesticFlight("F301", "Mumbai", "Bangalore", "15-04-2025", 10, 180.0)); 

        flightManager.addFlight(new DomesticFlight("F302", "Chennai", "Hyderabad", "16-04-2025", 8, 120.0)); 

        flightManager.addFlight(new DomesticFlight("F303", "Kolkata", "Patna", "14-04-2025", 6, 100.0)); 

        flightManager.addFlight(new InternationalFlight("F401", "Mumbai", "London", "20-04-2025", 3, 750.0)); 

        flightManager.addFlight(new InternationalFlight("F402", "Delhi", "New York", "25-04-2025", 5, 1100.0)); 

        flightManager.addFlight(new DomesticFlight("F501", "Pune", "Goa", "13-04-2025", 12, 90.0)); 

        flightManager.addFlight(new DomesticFlight("F502", "Ahmedabad", "Jaipur", "17-04-2025", 9, 140.0)); 

        flightManager.addFlight(new InternationalFlight("F601", "Chennai", "Singapore", "18-04-2025", 4, 600.0)); 

        flightManager.addFlight(new InternationalFlight("F602", "Hyderabad", "Bangkok", "19-04-2025", 7, 650.0)); 

 

        try { 

            System.out.print("Enter username: "); 

            String username = scanner.nextLine(); 

            System.out.print("Enter password: "); 

            String password = scanner.nextLine(); 

            System.out.print("Are you admin? (yes/no): "); 

            boolean isAdmin = scanner.nextLine().equalsIgnoreCase("yes"); 

 

            User user = isAdmin ? new Admin(username, password) : new Customer(username, password); 

 

            while (true) { 

                System.out.println("\n-------------"); 

                user.showOptions(); 

                System.out.print("Choose option (or 0 to exit): "); 

                int choice = scanner.nextInt(); 

                scanner.nextLine(); 

 

                if (choice == 0) break; 

 

                if (user instanceof Admin) { 

                    switch (choice) { 

                        case 1: 

                            try { 

                                System.out.print("Flight ID: "); 

                                String id = scanner.nextLine(); 

                                if (!id.startsWith("F")) { 

                                    throw new IllegalArgumentException("Flight ID must start with 'F'."); 

                                } 

                                System.out.print("Origin: "); 

                                String origin = scanner.nextLine(); 

                                System.out.print("Destination: "); 

                                String dest = scanner.nextLine(); 

                                System.out.print("Date (dd-MM-yyyy): "); 

                                String date = scanner.nextLine(); 

                                System.out.print("Seats: "); 

                                int seats = scanner.nextInt(); 

                                System.out.print("Price: $"); 

                                double price = scanner.nextDouble(); 

                                scanner.nextLine(); 

                                System.out.print("Type (domestic/international): "); 

                                String type = scanner.nextLine(); 

System.out.print("Flight added Successfully \n"); 

 

                                Flight flight = type.equalsIgnoreCase("domestic") ? 

                                        new DomesticFlight(id, origin, dest, date, seats, price) : 

                                        new InternationalFlight(id, origin, dest, date, seats, price); 

                                flightManager.addFlight(flight); 

                            } catch (IllegalArgumentException e) { 

                                System.out.println("Error: " + e.getMessage()); 

                            } 

                            break; 

                        case 2: 

                            System.out.print("Flight ID to remove: "); 

                            String removeId = scanner.nextLine(); 

                            flightManager.removeFlight(removeId); 

    System.out.print("Flight removed Successfully \n"); 

                            break; 

                        case 3: 

                            flightManager.listFlights(); 

                            break; 

                        case 4: 

                            System.out.print("Flight ID to update price: "); 

                            String flightIdPrice = scanner.nextLine(); 

                            Flight updatePriceFlight = flightManager.searchFlight(flightIdPrice); 

                            if (updatePriceFlight != null) { 

                                System.out.print("New Price: $"); 

                                double newPrice = scanner.nextDouble(); 

                                scanner.nextLine(); 

                                if (newPrice == updatePriceFlight.getBasePrice()) { 

                                    System.out.println("Error: This price is already set for the flight."); 

                                } else { 

                                    updatePriceFlight.updatePrice(newPrice); 

                                    System.out.println("Price updated."); 

                                } 

                            } else { 

                                System.out.println("Flight not found."); 

                            } 

                            break; 

                        case 5: 

                            System.out.print("Flight ID to update seats: "); 

                            String flightIdSeats = scanner.nextLine(); 

                            Flight updateSeatsFlight = flightManager.searchFlight(flightIdSeats); 

                            if (updateSeatsFlight != null) { 

                                System.out.print("Seats (Booking cancelled): "); 

                                int moreSeats = scanner.nextInt(); 

                                scanner.nextLine(); 

                                updateSeatsFlight.addSeats(moreSeats); 

                                System.out.println("Seats updated."); 

                            } else { 

                                System.out.println("Flight not found."); 

                            } 

                            break; 

                        case 6: 

                            System.out.print("Enter Flight ID to search: "); 

                            String searchId = scanner.nextLine(); 

                            Flight searchedFlight = flightManager.searchFlight(searchId); 

                            if (searchedFlight != null) { 

                                System.out.println("Flight Found: " + searchedFlight.getDetails()); 

                            } else { 

                                System.out.println("Flight not found."); 

                            } 

                            break; 

                    } 

                } else if (user instanceof Customer) { 

                    switch (choice) { 

                        case 1: 

                            flightManager.listFlights(); 

                            break; 

                        case 2: 

                            System.out.print("Enter Flight ID to book: "); 

                            String flightId = scanner.nextLine(); 

                            Flight f = flightManager.searchFlight(flightId); 

                            if (f != null) { 

                                System.out.print("Number of passengers: "); 

                                int passengers = scanner.nextInt(); 

                                scanner.nextLine(); 

                                System.out.print("Payment method (credit/debit/upi): "); 

                                String method = scanner.nextLine(); 

                                bookingManager.bookFlight(user.getUsername(), f, method, passengers); 

                            } else { 

                                System.out.println("Flight not found."); 

                            } 

                            break; 

                        case 3: 

                            System.out.print("Enter Booking ID to cancel: "); 

                            int bId = scanner.nextInt(); 

                            scanner.nextLine(); 

                            bookingManager.cancelBooking(bId); 

                            System.out.print("Booking Cancelled"); 

                            break; 

                        case 4: 

                            bookingManager.viewBookings(user.getUsername()); 

                            break; 

                        case 5: 

                            System.out.print("Sort by (date/seats): "); 

                            String sortOpt = scanner.nextLine(); 

                            if (sortOpt.equalsIgnoreCase("date")) flightManager.sortFlightsByDate(); 

                            else if (sortOpt.equalsIgnoreCase("seats")) flightManager.sortFlightsBySeats(); 

                            break; 

                    } 

                } 

            } 

        } catch (Exception e) { 

            System.out.println("System Error: " + e.getMessage()); 

        } finally { 

            scanner.close(); 

            System.out.println("Thank you for using Flight Booking System."); 

        } 

    } 

} 
