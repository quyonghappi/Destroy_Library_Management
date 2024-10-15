package com.library;

import java.time.LocalDate;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.library.config.APIConfig;
import com.library.config.GoogleBooksAPIClient;
import com.library.models.Document;
import com.library.models.Library;
import com.library.models.User;

public class LibraryManagementApplication {
    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            // show menu
            System.out.println("---------Welcome to Library Management Application-----------");
            System.out.println("[0] Exit");
            System.out.println("[1] Add Document");
            System.out.println("[2] Remove Document");
            System.out.println("[3] Update Document");
            System.out.println("[4] Find Document");
            System.out.println("[5] Display Document");
            System.out.println("[6] Add User");
            System.out.println("[7] Borrow Document");
            System.out.println("[8] Return Document");
            System.out.println("[9] Display User Info");

            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid option!");
                System.out.print("Enter your choice: ");
                scanner.next();
            }

            choice = scanner.nextInt();

            switch (choice) {
                case 0:
                    System.out.println("Goodbye!");
                    break;
                case 1:
                    scanner.nextLine();
                    addDocument(scanner, library);
                    break;
                case 2:
                    scanner.nextLine();
                    removeDocument(scanner, library);
                    break;
                case 3:
                    updateDocument(scanner, library);
                    break;
                case 4:
                    scanner.nextLine();
                    findDocument(scanner, library);
                    break;
                case 5:
                    library.displayDocuments();
                    break;
                case 6:
                    scanner.nextLine();
                    addUserRead(scanner, library);
                    break;
                case 7:
                    scanner.nextLine();
                    borrowDocument(scanner, library);
                    break;
                case 8:
                    scanner.nextLine();
                    returnDocument(scanner, library);
                    break;
                case 9:
                    library.displayUserReads();
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 0);
        scanner.close();
    }

    private static void addDocument(Scanner scanner, Library library) {
        System.out.print("Enter ISBN: ");
        String ISBN = scanner.nextLine();
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter categoryId: ");
        int categoryId = scanner.nextInt();
        System.out.print("Enter authorId: ");
        int authorId = scanner.nextInt();
        System.out.print("Enter publisherId: ");
        int publisherId = scanner.nextInt();
        System.out.print("Enter publicationYear: ");
        int publicationYear = scanner.nextInt();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter description: ");
        scanner.nextLine();
        String description = scanner.nextLine();
        System.out.print("Enter location: ");
        String location = scanner.nextLine();

        library.addDocument((Document) new Document(ISBN, title, categoryId, authorId, publisherId, publicationYear, quantity, description, location));
    }
    private static void removeDocument(Scanner scanner, Library library) {
        System.out.print("Enter isbn: ");
        String isbn = scanner.nextLine();
        library.removeDocument(isbn);
    }
    private static void updateDocument(Scanner scanner, Library library) {
        System.out.print("Enter isbn: ");
        String isbn = scanner.nextLine();
        scanner.nextLine();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        library.updateDocument(isbn, quantity);
    }
    private static void findDocument(Scanner scanner, Library library) {
        System.out.print("Enter isbn: ");
        String isbn = scanner.nextLine();
        library.findDocument(isbn);
    }
    private static void borrowDocument(Scanner scanner, Library library) {
        System.out.print("Enter userReadId: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        library.borrowDocument(userId, title);
    }
    private static void returnDocument(Scanner scanner, Library library) {
        System.out.print("Enter userReadId: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter isbn: ");
        String ISBN = scanner.nextLine();
        library.returnDocument(userId, ISBN);
    }
    private static void addUserRead(Scanner scanner, Library library) {
        System.out.print("Enter userIdRead: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter full_name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter user_name: ");
        String userName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter role: ");
        String role = scanner.nextLine();
        System.out.print("Enter accountStatus: ");
        String accountStatus = scanner.nextLine();
        System.out.print("Enter joinDate: ");
        LocalDate joinDate = LocalDate.parse(scanner.nextLine());
        library.addUserRead(new User(userId, fullName, userName, email, password, phoneNumber, role, accountStatus, joinDate));
    }
}

