package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.BookDto;
import com.librarymanagementsystem.entity.Book;
import com.librarymanagementsystem.entity.BookIssued;
import com.librarymanagementsystem.services.BookIssuedService;
import com.librarymanagementsystem.services.BookService;
import com.librarymanagementsystem.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/librarian")
@Tag(name = "Librarian APIs")
public class LibrarianController {

    private final BookService bookService;
    private final BookIssuedService bookIssuedService;
    private final UserService userService;

    public LibrarianController(BookService bookService, BookIssuedService bookIssuedService, UserService userService) {
        this.bookService = bookService;
        this.bookIssuedService = bookIssuedService;
        this.userService = userService;
    }

    @PostMapping("create-book")
    @Operation(summary = "Create a new book")
    public ResponseEntity<Book> createBook(@RequestBody BookDto bookDto) {
        Book book = bookService.createBook(bookDto);
        log.info("Book created successfully with ID: [{}]", book.getBookId());
        return ResponseEntity.ok(book);
    }

    @GetMapping("book-by-id/{id}")
    @Operation(summary = "Find book by ID")
    public ResponseEntity<Book> findByBookId(@PathVariable String id) {
        Book book = bookService.findByBookId(id);
        log.info("Fetched book details for ID: [{}]", id);
        return ResponseEntity.ok(book);
    }


    @GetMapping("all-books")
    @Operation(summary = "Get all books")
    public ResponseEntity<List<Book>> getAllBooks() {
        log.info("Fetching all available books");
        return ResponseEntity.ok(bookService.getAllBook());
    }

    @PutMapping("/update-book-by-id/{id}")
    @Operation(summary = "Update book by ID")
    public ResponseEntity<Book> updateBook(@RequestBody BookDto bookDto, @PathVariable String id) {
        Book book = bookService.updateBook(bookDto, id);
        log.info("Updated book details for ID: [{}]", id);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Delete book by ID")
    @DeleteMapping("/delete-book-by-id/{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable String id) {
        bookService.deleteById(id);
        log.info("Deleted book with ID: [{}]", id);
        return ResponseEntity.ok("Book deleted successfully");
    }

    @Operation(summary = "All issued books by id")
    @GetMapping("/issued-book-by-id/{id}")
    public ResponseEntity<BookIssued> findBookIssuedById(@PathVariable String id) {
        BookIssued bookIssued = bookIssuedService.findById(id);
        log.info("Fetched issued book record with ID: [{}]", id);
        return ResponseEntity.ok(bookIssued);
    }

    @Operation(summary = "Get all issued books")
    @GetMapping("/all-issued-book")
    public ResponseEntity<List<BookIssued>> getAllBookIssued() {
        log.info("Fetching all issued book records");
        return ResponseEntity.ok(bookIssuedService.getAllIssuedBook());
    }

    @Operation(summary = "Delete issued book by ID and user name")
    @DeleteMapping("delete-by/issuedId/{id}/and/userName/{userName}")
    public ResponseEntity<String> deleteIssuedBook(@PathVariable String id, @PathVariable String userName) {
        bookIssuedService.deleteById(id, userName);
        log.info("Deleted issued book record with ID: [{}] for user [{}]", id, userName);
        return ResponseEntity.ok("IssuedBook deleted successfully");
    }

}
