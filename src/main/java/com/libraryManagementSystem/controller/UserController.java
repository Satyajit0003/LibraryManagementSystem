package com.libraryManagementSystem.controller;

import com.libraryManagementSystem.dto.UserDto;
import com.libraryManagementSystem.entity.Book;
import com.libraryManagementSystem.entity.BookIssued;
import com.libraryManagementSystem.entity.User;
import com.libraryManagementSystem.services.BookIssuedService;
import com.libraryManagementSystem.services.BookService;
import com.libraryManagementSystem.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "User APIs")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookIssuedService bookIssuedService;

    @Autowired
    private BookService bookService;

    @PutMapping("update-user")
    @Operation(summary = "Update User Details")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {
        User oldUser = userService.updateUser(userDto);
        log.info("User [{}] details updated successfully", userDto.getUserName());
        return ResponseEntity.ok(oldUser);
    }

    @Operation(summary = "Delete User Account")
    @DeleteMapping("delete-user")
    public ResponseEntity<String> deleteUser() {
        String userName = userService.deleteUser();
        log.info("User [{}] deleted successfully", userName);
        return ResponseEntity.ok("User deleted successfully");
    }

    @Operation(summary = "Find book by name")
    @GetMapping("/book-by-bookname/{bookName}")
    public ResponseEntity<Book> findByBookName(@PathVariable String bookName) {
        Book book = bookService.findByBookName(bookName);
        log.info("Fetched book details for book name: [{}]", bookName);
        return ResponseEntity.ok(book);
    }

    @GetMapping("books-by/semester/{semester}/and/branch/{branch}")
    @Operation(summary = "Get all books by semester and branch")
    public ResponseEntity<List<Book>> getAllBookBySemAndBranch(@PathVariable int semester, @PathVariable String branch) {
        List<Book> bySemesterAndBranch = bookService.findBySemesterAndBranch(semester, branch);
        log.info("Fetched books for semester [{}] and branch [{}]", semester, branch);
        return ResponseEntity.ok(bySemesterAndBranch);
    }

    @Operation(summary = "Issue Book to User by Book ID")
    @GetMapping("/issued/bookId/{id}")
    public ResponseEntity<BookIssued> issued(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        log.info("User [{}] is attempting to issue book with ID [{}]", userName, id);
        BookIssued bookIssued = bookIssuedService.issuedBook(id);
        log.info("Book issued successfully to user [{}]", userName);

        return new ResponseEntity<>(bookIssued, HttpStatus.CREATED);
    }

    @Operation(summary = "Return Issued Book by ID")
    @GetMapping("/returned/issuedId/{id}")
    public ResponseEntity<BookIssued> returned(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        log.info("User [{}] is attempting to return issued book with issuedId [{}]", userName, id);
        BookIssued bookIssued = bookIssuedService.returnedBook(id);
        log.info("Book returned successfully by user [{}]", userName);

        return new ResponseEntity<>(bookIssued, HttpStatus.OK);
    }

}
