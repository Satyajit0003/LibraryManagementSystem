package com.libraryManagementSystem.controller;

import com.libraryManagementSystem.dto.BookDto;
import com.libraryManagementSystem.entity.Book;
import com.libraryManagementSystem.entity.BookIssued;
import com.libraryManagementSystem.entity.User;
import com.libraryManagementSystem.services.BookIssuedServiceImpl;
import com.libraryManagementSystem.services.BookServiceImpl;
import com.libraryManagementSystem.services.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/librarian")
@Tag(name = "Librarian APIs")
public class LibrarianController {

    @Autowired
    private BookServiceImpl bookServiceImpl;

    @Autowired
    private BookIssuedServiceImpl bookIssuedServiceImpl;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @PostMapping("create-book")
    @Operation(summary = "Create a new book")
    public ResponseEntity<?> createBook(@RequestBody BookDto book) {
        try {
            Book newBook = new Book();
            newBook.setBookName(book.getBookName());
            newBook.setAuthorName(book.getAuthorName());
            newBook.setBranch(book.getBranch());
            newBook.setSemester(book.getSemester());
            newBook.setAvailableCopies(book.getAvailableCopies());
            newBook.setTotalCopies(book.getTotalCopies());

            bookServiceImpl.saveBook(newBook);
            log.info("Book created successfully: {}", newBook.getBookName());
            return new ResponseEntity<>(newBook, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create book: {}", e.getMessage(), e);
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("book-by-id/{id}")
    @Operation(summary = "Find book by ID")
    public ResponseEntity<Book> findByBookId(@PathVariable String id) {
        log.info("Fetching book by ID: {}", id);
        Book book = bookServiceImpl.findByBookId(id).orElse(null);
        if (book == null) {
            log.warn("Book not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Find book by name")
    @GetMapping("/book-by-bookname/{bookName}")
    public ResponseEntity<Book> findByBookName(@PathVariable String bookName) {
        log.info("Fetching book by name: {}", bookName);
        Book book = bookServiceImpl.findByBookName(bookName).orElse(null);
        if (book == null) {
            log.warn("Book not found with name: {}", bookName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(book);
    }

    @GetMapping("all-books")
    @Operation(summary = "Get all books")
    public ResponseEntity<List<Book>> getAllBooks() {
        log.info("Fetching all books");
        return new ResponseEntity<>(bookServiceImpl.getAllBook(), HttpStatus.OK);
    }

    @PutMapping("/update-book-by-id/{id}")
    @Operation(summary = "Update book by ID")
    public ResponseEntity<?> updateBook(@RequestBody BookDto book, @PathVariable String id) {
        log.info("Updating book with ID: {}", id);
        Book oldBook = bookServiceImpl.findByBookId(id).orElse(null);
        if (oldBook == null) {
            log.warn("Book not found with ID: {}", id);
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
        oldBook.setBookName(book.getBookName());
        oldBook.setAuthorName(book.getAuthorName());
        oldBook.setBranch(book.getBranch());
        oldBook.setSemester(book.getSemester());
        oldBook.setAvailableCopies(book.getAvailableCopies());
        oldBook.setTotalCopies(book.getTotalCopies());

        bookServiceImpl.saveBook(oldBook);
        log.info("Book updated successfully: {}", id);
        return new ResponseEntity<>(oldBook, HttpStatus.OK);
    }

    @GetMapping("books-by/semester/{semester}/and/branch/{branch}")
    @Operation(summary = "Get all books by semester and branch")
    public ResponseEntity<List<Book>> getAllBookBySemAndBranch(@PathVariable int semester, @PathVariable String branch) {
        log.info("Fetching books for Semester: {}, Branch: {}", semester, branch);
        List<Book> books = bookServiceImpl.findBySemesterAndBranch(semester, branch);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @Operation(summary = "Delete book by ID")
    @DeleteMapping("/delete-book-by-id/{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable String id) {
        log.info("Attempting to delete book with ID: {}", id);
        Optional<Book> bookOptional = bookServiceImpl.findByBookId(id);
        if (bookOptional.isEmpty()) {
            log.warn("Book not found with ID: {}", id);
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
        Book book = bookOptional.get();
        if (book.getAvailableCopies() != book.getTotalCopies()) {
            log.warn("Book with ID {} is currently issued. Cannot delete.", id);
            return new ResponseEntity<>("Book is issued to someone, so it cannot be deleted", HttpStatus.BAD_REQUEST);
        }
        bookServiceImpl.deleteById(id);
        log.info("Book deleted successfully: {}", id);
        return ResponseEntity.ok("Book deleted successfully");
    }

    @Operation(summary = "All issued books by id")
    @GetMapping("/issued-book-by-id/{id}")
    public ResponseEntity<BookIssued> findBookIssuedById(@PathVariable String id) {
        log.info("Fetching issued book by ID: {}", id);
        BookIssued bookIssued = bookIssuedServiceImpl.findById(id).orElse(null);
        if (bookIssued == null) {
            log.warn("Issued book not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(bookIssued);
    }

    @Operation(summary = "Get all issued books")
    @GetMapping("/all-issued-book")
    public ResponseEntity<List<BookIssued>> getAllBookIssued() {
        log.info("Fetching all issued books");
        return new ResponseEntity<>(bookIssuedServiceImpl.getAllIssuedBook(), HttpStatus.OK);
    }

    @Operation(summary = "Delete issued book by ID and user name")
    @DeleteMapping("delete-by/issuedId/{id}/and/userName/{userName}")
    public ResponseEntity<String> deleteIssuedBook(@PathVariable String id, @PathVariable String userName) {
        log.info("Attempting to delete issued book ID {} for user {}", id, userName);
        User user = userServiceImpl.findByUserName(userName).orElse(null);
        if (user == null) {
            log.warn("User not found: {}", userName);
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        BookIssued issued = bookIssuedServiceImpl.findById(id).orElse(null);
        if (issued == null) {
            log.warn("Issued book ID {} not found", id);
            return new ResponseEntity<>("Issued book not found", HttpStatus.NOT_FOUND);
        }
        Book book = bookServiceImpl.findByBookId(issued.getBookId()).orElse(null);
        if (book == null) {
            log.warn("Book not found with ID: {}", issued.getBookId());
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
        boolean removed = user.getBookIssuedList().removeIf(b -> b.getIssuedId().equals(id));
        if (!removed) {
            log.warn("Issued book ID {} not found in user {}'s list", id, userName);
            return new ResponseEntity<>("Issued book not found in user's list", HttpStatus.NOT_FOUND);
        }
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookServiceImpl.saveBook(book);
        userServiceImpl.saveUser(user);
        bookIssuedServiceImpl.deleteById(id);
        log.info("Issued book ID {} deleted successfully for user {}", id, userName);
        return ResponseEntity.ok("IssuedBook deleted successfully");
    }


}
