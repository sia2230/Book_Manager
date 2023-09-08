import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int nextBookId = 1;

    private int bookId;
    private String bookName;
    private String writerName;
    private double price;
    private int quantity;

    public Book(String bookName, String writerName, double price, int quantity) {
        this.bookId = nextBookId++;
        this.bookName = bookName;
        this.writerName = writerName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public String getWriterName() {
        return writerName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void reduceQuantity(int quantityToSubtract) {
        if (quantityToSubtract <= quantity) {
            quantity -= quantityToSubtract;
            System.out.println("Quantity reduced successfully.\n");
        } else {
            System.out.println("Insufficient quantity. Cannot reduce beyond available quantity.\n");
        }
    }

    @Override
    public String toString() {
        return "Book [bookId=" + bookId + ", bookName=" + bookName + ", writerName=" + writerName +
                ", price=" + price + ", quantity=" + quantity + "]";
    }
}

class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int nextUserId = 1;

    private int userId;
    private String password;
    private String role;

    public User(String password, String role) {
        this.userId = nextUserId++;
        this.password = password;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", role=" + role + "]";
    }
}

class IssueBook implements Serializable {
    private static final long serialVersionUID = 1L;

    private int bookId;
    private int userId;

    public IssueBook(int bookId, int userId) {
        this.bookId = bookId;
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "IssueBook [bookId=" + bookId + ", userId=" + userId + "]";
    }
}

public class Main {
    private static final String USER_DETAILS_FILE = "userDetails.txt";
    private static final String BOOK_DETAILS_FILE = "bookDetails.txt";
    private static final String ISSUE_BOOK_FILE = "issueBook.txt";
    private static final String ADMIN_USERNAME = "Shivani";
    private static final String ADMIN_PASSWORD = "1234";

    private static ArrayList<Book> books = new ArrayList<>();
    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<IssueBook> issuedBooks = new ArrayList<>();
    private static boolean isAdminLoggedIn = false;

    public static void main(String[] args) {
        loadDataFromFiles();

        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("1. Add a book");
            System.out.println("2. Display all books");
            System.out.println("3. Delete a book");
            System.out.println("4. Reduce quantity of a book");
            System.out.println("5. Search book by name");
            System.out.println("6. Search book by writer");
            System.out.println("7. Admin Login");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    if (isAdminLoggedIn) {
                        // Admin is logged in, allow adding a book
                        System.out.print("Enter book name: ");
                        String bookName = scanner.nextLine();

                        System.out.print("Enter writer name: ");
                        String writerName = scanner.nextLine();

                        System.out.print("Enter price: ");
                        double price = scanner.nextDouble();

                        System.out.print("Enter quantity: ");
                        int quantity = scanner.nextInt();

                        Book book = new Book(bookName, writerName, price, quantity);
                        books.add(book);
                        System.out.println("Book added successfully!\n");
                    } else {
                        System.out.println("You must be logged in as an admin to add a book.\n");
                    }
                    break;

                case 2:
                    displayAllBooks();
                    break;

                case 3:
                    if (books.isEmpty()) {
                        System.out.println("No books found.\n");
                    } else {
                        System.out.print("Enter the book ID to delete: ");
                        int book;
                        int bookIdToDelete = scanner.nextInt();

                        boolean found = false;
                        Iterator<Book> iterator = books.iterator();
                        while (iterator.hasNext()) {
                            Book b = iterator.next();
                            if (b.getBookId() == bookIdToDelete) {
                                iterator.remove();
                                found = true;
                                break;
                            }
                        }

                        if (found) {
                            System.out.println("Book deleted successfully!\n");
                        } else {
                            System.out.println("Book not found.\n");
                        }
                    }
                    break;

                case 4:
                    if (books.isEmpty()) {
                        System.out.println("No books found.\n");
                    } else {
                        System.out.print("Enter the book ID to reduce quantity: ");
                        int bookIdToReduce = scanner.nextInt();

                        boolean found = false;
                        for (Book b : books) {
                            if (b.getBookId() == bookIdToReduce) {
                                System.out.print("Enter the quantity to reduce: ");
                                int quantityToSubtract = scanner.nextInt();
                                b.reduceQuantity(quantityToSubtract);
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            System.out.println("Book not found.\n");
                        }
                    }
                    break;

                case 5:
                    if (books.isEmpty()) {
                        System.out.println("No books found.\n");
                    } else {
                        scanner.nextLine(); // Consume newline character

                        System.out.print("Enter the book name to search: ");
                        String searchBookName = scanner.nextLine();

                        boolean found = false;
                        for (Book b : books) {
                            if (b.getBookName().toLowerCase().contains(searchBookName.toLowerCase())) {
                                System.out.println("Book details:");
                                System.out.println(b);
                                found = true;
                            }
                        }

                        if (!found) {
                            System.out.println("Book absent.\n");
                        }
                    }
                    break;

                case 6:
                    if (books.isEmpty()) {
                        System.out.println("No books found.\n");
                    } else {
                        scanner.nextLine(); // Consume newline character

                        System.out.print("Enter the writer name to search: ");
                        String searchWriterName = scanner.nextLine();

                        boolean found = false;
                        for (Book b : books) {
                            if (b.getWriterName().toLowerCase().contains(searchWriterName.toLowerCase())) {
                                System.out.println("Book details:");
                                System.out.println(b);
                                found = true;
                            }
                        }

                        if (!found) {
                            System.out.println("No books by the specified writer found\n");
                        }
                    }
                    break;

                case 7:
                    // Admin login functionality
                    System.out.print("Enter admin username: ");
                    String adminUsername = scanner.nextLine();
                    System.out.print("Enter admin password: ");
                    String adminPassword = scanner.nextLine();

                    if (adminUsername.equals(ADMIN_USERNAME) && adminPassword.equals(ADMIN_PASSWORD)) {
                        isAdminLoggedIn = true;
                        System.out.println("Admin login successful.\n");
                    } else {
                        System.out.println("Admin login failed. Invalid credentials.\n");
                    }
                    break;

                case 8:
                    saveDataToFiles();
                    System.out.println("Exiting the program.");
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a valid option.\n");
                    break;
            }
        } while (choice != 8);

        scanner.close();
    }

    private static void loadDataFromFiles() {
        try (ObjectInputStream bookInput = new ObjectInputStream(new FileInputStream(BOOK_DETAILS_FILE));
             ObjectInputStream userInput = new ObjectInputStream(new FileInputStream(USER_DETAILS_FILE));
             ObjectInputStream issueBookInput = new ObjectInputStream(new FileInputStream(ISSUE_BOOK_FILE))) {

            books = (ArrayList<Book>) bookInput.readObject();
            users = (ArrayList<User>) userInput.readObject();
            issuedBooks = (ArrayList<IssueBook>) issueBookInput.readObject();

        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions if necessary, e.g., if the files don't exist yet
            e.printStackTrace();
        }
    }

    private static void saveDataToFiles() {
        try (ObjectOutputStream bookOutput = new ObjectOutputStream(new FileOutputStream(BOOK_DETAILS_FILE));
             ObjectOutputStream userOutput = new ObjectOutputStream(new FileOutputStream(USER_DETAILS_FILE));
             ObjectOutputStream issueBookOutput = new ObjectOutputStream(new FileOutputStream(ISSUE_BOOK_FILE))) {

            bookOutput.writeObject(books);
            userOutput.writeObject(users);
            issueBookOutput.writeObject(issuedBooks);

        } catch (IOException e) {
            // Handle exceptions if necessary
            e.printStackTrace();
        }
    }

    private static void displayAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books found.\n");
        } else {
            System.out.println("List of all books:");
            for (Book book : books) {
                System.out.println(book);
            }
            System.out.println();
        }
    }
}
