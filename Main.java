import java.util.ArrayList;
import java.util.Scanner;

// IT-2307 Mertay, Alikhan, Yeskendir

class User {
    private String name;
    private int age;
    private int balance;
    private ArrayList<Ticket> orderHistory;

    public User(String name, int age, int balance) {
        this.name = name;
        this.age = age;
        this.balance = balance;
        this.orderHistory = new ArrayList<>();
    }

    public int getBalance() {
        return balance;
    }

    public int getAge() {
        return age;
    }

    public ArrayList<Ticket> getOrderHistory() {
        return orderHistory;
    }

    public void addToOrderHistory(Ticket ticket) {
        orderHistory.add(ticket);
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }
}

class Movie {
    private static int movieIdCounter = 1;
    private int id;
    private String movieName;
    private String genre;
    private int ageRestriction;
    private String author;
    private int cost;

    public Movie(String movieName, String genre, int ageRestriction, String author, int cost) {
        this.id = movieIdCounter++;
        this.movieName = movieName;
        this.genre = genre;
        this.ageRestriction = ageRestriction;
        this.author = author;
        this.cost = cost;
    }

    public int getAgeRestriction() {
        return ageRestriction;
    }

    public String getMovieNames() {
        return id + ") " + movieName;
    }

    public String getMovieInfo() {
        return id + ") " + movieName + " (" + ageRestriction + "+) - " + genre + " by " + author + " - Cost: $" + cost;
    }

    public String getMovieName() {
        return movieName;
    }

    public int getCost() {
        return cost;
    }
}

class Ticket {
    private static int ticketIdCounter = 1;
    private int id;
    private String movieName;
    private int price;

    public Ticket(String movieName, int price) {
        this.id = ticketIdCounter++;
        this.movieName = movieName;
        this.price = price;
    }

    public String getMovieName() {
        return movieName;
    }

    public int getPrice() {
        return price;
    }
}

class CinemaSystem {
    private ArrayList<User> users;
    private ArrayList<Movie> movies;
    private ArrayList<Ticket> soldTickets;

    public CinemaSystem() {
        this.users = new ArrayList<>();
        this.movies = new ArrayList<>();
        this.soldTickets = new ArrayList<>();
        initializeMovies();
    }

    private void initializeMovies() {
        movies.add(new Movie("Prisoners", "Drama", 18, "Denis Villeneuve", 500));
        movies.add(new Movie("The Hunger Games", "Action", 12, "Gary Ross", 300));
        movies.add(new Movie("Batman", "Action", 13, "Tim Burton", 600));
        movies.add(new Movie("Inception", "Sci-Fi", 15, "Christopher Nolan", 400));
        movies.add(new Movie("Joker", "Drama", 18, "Todd Phillips", 700));
        movies.add(new Movie("Star Wars - Revenge of the Sith", "Sci-Fi", 12, "George Lucas", 1000));
        movies.add(new Movie("I Want to Eat Your Pancreas", "Animation", 15, "Shinichirô Ushijima", 200));
        movies.add(new Movie("Kung Fu Panda", "Animation", 7, "Mark Osborne, John Stevenson", 0));
    }

    public void addUser(User user) {
        users.add(user);
        System.out.println("User added successfully.");
    }

    public void addCashToUser(String userName, int amount) {
        User user = getUserByName(userName);
        if (user != null) {
            user.setBalance(user.getBalance() + amount);
            System.out.println("Cash added!\nNew balance: $" + user.getBalance());
        } else {
            System.out.println("Invalid user name.");
        }
    }

    public void showAllMovies() {
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
        } else {
            System.out.println("Available Movies:");
            for (Movie movie : movies) {
                System.out.println(movie.getMovieNames());
            }
        }
    }

    public void displayMoviesWithDetails() {
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
        } else {
            System.out.println("All Movies with Details:");
            for (Movie movie : movies) {
                System.out.println(movie.getMovieInfo());
            }
        }
    }

    public void BuyTicket(String userName, String movieName) {
        User user = getUserByName(userName);
        if (user != null) {
            Movie selectedMovie = getMovieByName(movieName);
            if (selectedMovie != null && isValidMovieName(movieName)) {
                if (user.getAge() >= selectedMovie.getAgeRestriction()) {
                    int movieCost = selectedMovie.getCost();
                    if (user.getBalance() >= movieCost) {
                        user.setBalance(user.getBalance() - movieCost);
                        user.addToOrderHistory(new Ticket(selectedMovie.getMovieName(), movieCost));
                        System.out.println("Movie's ticket added to user's basket successfully! Remaining balance: $" + user.getBalance());
                    } else {
                        System.out.println("Unable to buy movie's ticket. Insufficient balance.");
                    }
                } else {
                    System.out.println("Unable to buy movie's ticket. Age restriction not met.");
                }
            } else {
                System.out.println("Invalid movie name.");
            }
        } else {
            System.out.println("Invalid user name.");
        }
    }

    public void CancelTicket(String userName, String movieName) {
        User user = getUserByName(userName);
        if (user != null) {
            Movie selectedMovie = getMovieByName(movieName);
            if (selectedMovie != null) {
                Ticket canceledTicket = user.getOrderHistory().stream()
                        .filter(ticket -> ticket.getMovieName().equalsIgnoreCase(movieName))
                        .findFirst()
                        .orElse(null);

                if (canceledTicket != null) {
                    user.getOrderHistory().remove(canceledTicket);
                    user.setBalance(user.getBalance() + canceledTicket.getPrice());
                    System.out.println("Ticket canceled and Cash refunded.\nNew balance: $" + user.getBalance());
                } else {
                    System.out.println("Ticket not found in user's basket.");
                }
            } else {
                System.out.println("Invalid movie name.");
            }
        } else {
            System.out.println("Invalid user name.");
        }
    }

    public void displayUserMovies(String userName) {
        User user = getUserByName(userName);
        if (user != null) {
            if (!user.getOrderHistory().isEmpty()) {
                System.out.println("User's Ticket");
                for (Ticket ticket : user.getOrderHistory()) {
                    System.out.println(ticket.getMovieName());
                }
            } else {
                System.out.println("User has no tickets.");
            }
        } else {
            System.out.println("Invalid user name.");
        }
    }

    private Movie getMovieByName(String movieName) {
        for (Movie movie : movies) {
            if (movie.getMovieName().equalsIgnoreCase(movieName)) {
                return movie;
            }
        }
        return null;
    }

    private User getUserByName(String userName) {
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(userName)) {
                return user;
            }
        }
        return null;
    }

    private boolean isValidMovieName(String movieName) {
        return movies.stream().anyMatch(movie -> movie.getMovieName().equalsIgnoreCase(movieName));
    }
}
public class Main {
    public static void main(String[] args) {
            CinemaSystem cinemaSystem = new CinemaSystem();
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("");
                System.out.println("Choose available functions:");
                System.out.println("1) To add a new user.");
                System.out.println("2) To add cash to a user.");
                System.out.println("3) To show all available movies.");
                System.out.println("4) To display all movies with details.");
                System.out.println("5) To buy a movie ticket.");
                System.out.println("6) To cancel a movie ticket and refund cash.");
                System.out.println("7) To display a user's tickets list.");

                try {
                    int choice = Integer.parseInt(scanner.nextLine());

                    switch (choice) {
                        case 1:
                            System.out.print("Enter user name: ");
                            String userName = scanner.nextLine();
                            System.out.print("Enter user age: ");
                            int userAge = Integer.parseInt(scanner.nextLine());
                            System.out.print("Enter user balance: ");
                            int userBalance = Integer.parseInt(scanner.nextLine());
                            cinemaSystem.addUser(new User(userName, userAge, userBalance));
                            break;
                        case 2:
                            System.out.print("Enter user name: ");
                            String cashUserName = scanner.nextLine();
                            System.out.print("Enter amount to add: ");
                            int amount = Integer.parseInt(scanner.nextLine());
                            cinemaSystem.addCashToUser(cashUserName, amount);
                            break;
                        case 3:
                            cinemaSystem.showAllMovies();
                            break;
                        case 4:
                            cinemaSystem.displayMoviesWithDetails();
                            break;
                        case 5:
                            System.out.print("Enter user name: ");
                            String movieUserName = scanner.nextLine();
                            System.out.print("Enter movie name: ");
                            String movieName5 = scanner.nextLine();
                            cinemaSystem.BuyTicket(movieUserName, movieName5);
                            break;
                        case 6:
                            System.out.print("Enter user name: ");
                            String movieUserName6 = scanner.nextLine();
                            System.out.print("Enter movie name: ");
                            String movieName6 = scanner.nextLine();
                            cinemaSystem.CancelTicket(movieUserName6, movieName6);
                            break;
                        case 7:
                            System.out.print("Enter user name: ");
                            String movieUserName7 = scanner.nextLine();
                            cinemaSystem.displayUserMovies(movieUserName7);
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a valid option from 1 to 7.");
                            break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.nextLine();
                }
            }
        }
    }

