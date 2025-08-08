package com.libraryManagementSystem.constants;

public interface Log {
    String BOOK_NOT_FOUND = "Book not found with id: %s";
    String USER_NOT_FOUND = "User not found with id: %s";
    String BOOK_ISSUED = "Book issued successfully to user: %s";
    String BOOK_RETURNED = "Book returned successfully by user: %s";
    String USER_CREATED = "User created successfully with username: %s";
    String USER_DELETED = "User deleted successfully with id: %s";
    String BOOK_SAVED = "Book saved successfully with id: %s";
    String BOOK_DELETED = "Book deleted successfully with id: %s";
    String BOOK_UPDATED = "Book updated successfully with id: %s";
    String USER_UPDATED = "User updated successfully with id: %s";
    String USER_SIGNUP = "User signed up with role: %s and username: %s";
    String USER_FETCHED_ID = "User fetched successfully with id: %s";
    String ALL_USERS_FETCHED = "All users fetched successfully";
    String BOOK_FETCHED = "Book fetched successfully with id: %s";
    String ALL_BOOKS_FETCHED = "All books fetched successfully";
    String USER_FETCHED_USERNAME = "User fetched successfully with username: %s";
    String BOOK_ISSUE_FAILED = "Failed to issue book with id: %s" ;
    String BOOK_RETURN_FAILED = "Failed to return book with id: %s";
    String AUTHENTICATION_ERROR = "Authentication failed for user: %s";
    String BOOK_NOT_AVAILABLE = "Book with id: %s is not available for issue";
    String USER_CREATING= "Creating user with username: %s";
}
