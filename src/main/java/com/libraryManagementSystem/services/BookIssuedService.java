package com.libraryManagementSystem.services;

import com.libraryManagementSystem.dto.EmailEvent;
import com.libraryManagementSystem.entity.Book;
import com.libraryManagementSystem.entity.BookIssued;
import com.libraryManagementSystem.entity.User;
import com.libraryManagementSystem.repository.BookIssuedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookIssuedService {

    @Autowired
    private BookIssuedRepository bookIssuedRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Transactional
    public void issuedBook(String userName, String bookId) {
        User user = userService.findByUserName(userName).orElse(null);
        if(user == null) {
            log.error("User not found: {}", userName);
            throw new RuntimeException("User not found");
        }
        log.info("Issuing book with ID {} to user {}", bookId, userName);
        Book book = bookService.findByBookId(bookId)
                .orElseThrow(() -> {
                    log.error("Book not found with ID {}", bookId);
                    return new RuntimeException("Book not found");
                });

        if (book.getAvailableCopies() <= 0) {
            log.warn("No available copies for book ID {}", bookId);
            throw new RuntimeException("No available copies to issue");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookService.saveBook(book);
        log.info("Decreased available copies for book ID {}. New available copies: {}", bookId, book.getAvailableCopies());

        BookIssued bookIssued = new BookIssued();
        bookIssued.setBookId(bookId);
        bookIssued.setIssueDate(LocalDateTime.now().toString());
        bookIssued.setReturned(false);

        EmailEvent emailEvent = new EmailEvent(
                user.getEmail(),
                "Book Issued Notification",
                String.format("Dear %s, you have successfully issued the book '%s' on %s. Please return it by %s.",
                        user.getUserName(),
                        book.getBookName(),
                        LocalDate.now(),
                        LocalDate.now().plusDays(14))
        );
        log.info("Sending email notification for issued book to user {}", userName);
        kafkaProducerService.produceEmailNotification(emailEvent);
        saveIssuedBook(bookIssued, userName);
    }

    @Transactional
    public void returnedBook(String issuedId, String userName) {
        log.info("Processing return for issued ID {} by user {}", issuedId, userName);

        BookIssued bookIssued = bookIssuedRepository.findById(issuedId)
                .orElseThrow(() -> {
                    log.error("Issued book not found with ID {}", issuedId);
                    return new RuntimeException("Issued book not found");
                });

        Book book = bookService.findByBookId(bookIssued.getBookId())
                .orElseThrow(() -> {
                    log.error("Book not found with ID {}", bookIssued.getBookId());
                    return new RuntimeException("Book not found");
                });

        User user = userService.findByUserName(userName)
                .orElseThrow(() -> {
                    log.error("User not found: {}", userName);
                    return new RuntimeException("User not found");
                });

        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookService.saveBook(book);
        log.info("Increased available copies for book ID {}. New available copies: {}", book.getBookId(), book.getAvailableCopies());

        boolean removed = user.getBookIssuedList().removeIf(b -> b.getIssuedId().equals(issuedId));
        if (removed) {
            log.info("Removed issued book ID {} from user {}'s list", issuedId, userName);
        } else {
            log.warn("Issued book ID {} not found in user {}'s list", issuedId, userName);
        }

        userService.saveUser(user);

        bookIssued.setReturned(true);
        bookIssued.setReturnDate(LocalDateTime.now().toString());
        bookIssuedRepository.save(bookIssued);

        EmailEvent emailEvent = new EmailEvent(
                user.getEmail(),
                "Book Return Notification",
                String.format("Dear %s, you have successfully returned the book '%s' on %s.",
                        user.getUserName(),
                        book.getBookName(),
                        LocalDate.now())
        );
        log.info("Sending email notification for returned book to user {}", userName);
        kafkaProducerService.produceEmailNotification(emailEvent);
        log.info("Marked issued book ID {} as returned", issuedId);
    }

    @Transactional
    @CachePut(value = "bookIssued", key = "#bookIssued.issuedId")
    @CacheEvict(value = "booksIssued", allEntries = true )
    public BookIssued saveIssuedBook(BookIssued bookIssued, String userName) {
        log.info("Saving issued book for user {}", userName);
        User user = userService.findByUserName(userName)
                .orElseThrow(() -> {
                    log.error("User not found: {}", userName);
                    return new RuntimeException("User not found");
                });

        BookIssued saved = bookIssuedRepository.save(bookIssued);
        log.info("BookIssued record saved with ID {}", saved.getIssuedId());

        user.getBookIssuedList().add(saved);
        userService.saveUser(user);
        log.info("Updated user {}'s issued book list", userName);
        return saved;
    }

    @Cacheable(value = "bookIssued", key = "#id")
    public Optional<BookIssued> findById(String id) {
        log.info("Finding BookIssued by ID {}", id);
        return bookIssuedRepository.findById(id);
    }

    @Cacheable(value = "booksIssued")
    public List<BookIssued> getAllIssuedBook() {
        log.info("Fetching all issued books");
        return bookIssuedRepository.findAll();
    }

    @Caching(evict = {
            @CacheEvict(value = "bookIssued", key = "#id"),
            @CacheEvict(value = "booksIssued", allEntries = true)
    })
    public void deleteById(String id) {
        log.info("Deleting issued book with ID {}", id);
        bookIssuedRepository.deleteById(id);
    }
}
