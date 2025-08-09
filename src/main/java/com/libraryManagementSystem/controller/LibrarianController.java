package com.libraryManagementSystem.controller;

import com.libraryManagementSystem.dto.BookDto;
import com.libraryManagementSystem.entity.Book;
import com.libraryManagementSystem.entity.BookIssued;
import com.libraryManagementSystem.services.BookIssuedService;
import com.libraryManagementSystem.services.BookService;
import com.libraryManagementSystem.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/librarian")
@Tag(name = "Librarian APIs")
public class LibrarianController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookIssuedService bookIssuedService;

    @Autowired
    private UserService userService;

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

    @Operation(summary = "Find book by name")
    @GetMapping("/book-by-bookname/{bookName}")
    public ResponseEntity<Book> findByBookName(@PathVariable String bookName) {
        Book book = bookService.findByBookName(bookName);
        log.info("Fetched book details for book name: [{}]", bookName);
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

    @GetMapping("books-by/semester/{semester}/and/branch/{branch}")
    @Operation(summary = "Get all books by semester and branch")
    public ResponseEntity<List<Book>> getAllBookBySemAndBranch(@PathVariable int semester, @PathVariable String branch) {
        List<Book> bySemesterAndBranch = bookService.findBySemesterAndBranch(semester, branch);
        log.info("Fetched books for semester [{}] and branch [{}]", semester, branch);
        return ResponseEntity.ok(bySemesterAndBranch);
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
