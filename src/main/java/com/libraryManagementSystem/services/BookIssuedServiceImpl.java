package com.libraryManagementSystem.services;

import com.libraryManagementSystem.dto.EmailEvent;
import com.libraryManagementSystem.entity.Book;
import com.libraryManagementSystem.entity.BookIssued;
import com.libraryManagementSystem.entity.User;
import com.libraryManagementSystem.exception.BookIssuedNotFoundException;
import com.libraryManagementSystem.exception.BookNotAvailableException;
import com.libraryManagementSystem.exception.BookNotFoundException;
import com.libraryManagementSystem.exception.UserNotFoundException;
import com.libraryManagementSystem.repository.BookIssuedRepository;
import com.libraryManagementSystem.repository.BookRepository;
import com.libraryManagementSystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class BookIssuedServiceImpl implements BookIssuedService{

    @Autowired
    private BookIssuedRepository bookIssuedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Transactional
    @CachePut(value = "issuedBook", key = "#result.issuedId")
    @CacheEvict(value = "issuedBooks", allEntries = true)
    public BookIssued issuedBook(String bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found or may be deleted");
        }

        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if(bookOptional.isEmpty()){
            throw new BookNotFoundException("Book not found");
        }

        User user = userOptional.get();
        Book book = bookOptional.get();

        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException("No available copies to issue");
        }

        BookIssued bookIssued = new BookIssued();
        bookIssued.setIssuedId(UUID.randomUUID().toString());
        bookIssued.setBookId(bookId);
        bookIssued.setIssueDate(LocalDateTime.now().toString());
        bookIssued.setReturned(false);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        user.getBookIssuedList().add(bookIssued);
        userRepository.save(user);
        bookIssuedRepository.save(bookIssued);

        EmailEvent emailEvent = new EmailEvent(
                user.getEmail(),
                "Book Issued Notification",
                String.format("Dear %s, you have successfully issued the book '%s' on %s. Please return it by %s.",
                        user.getUserName(),
                        book.getBookName(),
                        LocalDate.now(),
                        LocalDate.now().plusDays(14))
        );
        kafkaProducerService.produceEmailNotification(emailEvent);
        return bookIssued;
    }

    @Transactional
    @CachePut(value = "issuedBook", key = "#result.issuedId")
    @CacheEvict(value = "issuedBooks", allEntries = true)
    public BookIssued returnedBook(String issuedId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        Optional<User> userOptional = userRepository.findByUserName(userName);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found or may be deleted");
        }

        Optional<BookIssued> bookIssuedOptional = bookIssuedRepository.findById(issuedId);
        if(bookIssuedOptional.isEmpty()) {
            throw new BookIssuedNotFoundException("Issued book not found");
        }

        BookIssued bookIssued = bookIssuedOptional.get();

        Optional<Book> bookOptional = bookRepository.findById(bookIssued.getBookId());
        if(bookOptional.isEmpty()){
            throw new BookNotFoundException("Book not found");
        }

        User user = userOptional.get();
        Book book = bookOptional.get();

        boolean removed = user.getBookIssuedList().removeIf(b -> b.getIssuedId().equals(issuedId));
        if (!removed) {
            throw  new BookIssuedNotFoundException("Issued book not found for this user");
        }

        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
        userRepository.save(user);
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

        return bookIssued;
    }

    @Cacheable(value = "issuedBook", key = "#id")
    public BookIssued findById(String id) {
        Optional<BookIssued> bookIssuedOptional = bookIssuedRepository.findById(id);
        if(bookIssuedOptional.isEmpty()) {
            throw new BookIssuedNotFoundException("Issued book not found");
        }
        BookIssued bookIssued = bookIssuedOptional.get();
        return bookIssued;
    }

    @Cacheable(value = "issuedBooks")
    public List<BookIssued> getAllIssuedBook() {
        return bookIssuedRepository.findAll();
    }

    @Caching(evict = {
            @CacheEvict(value = "issuedBook", key = "#id"),
            @CacheEvict(value = "issuedBooks", allEntries = true)
    })
    public void deleteById(String issuedId, String userName) {
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found or may be deleted");
        }

        Optional<BookIssued> bookIssuedOptional = bookIssuedRepository.findById(issuedId);
        if(bookIssuedOptional.isEmpty()) {
            throw new BookIssuedNotFoundException("Issued book not found");
        }

        BookIssued bookIssued = bookIssuedOptional.get();

        Optional<Book> bookOptional = bookRepository.findById(bookIssued.getBookId());
        if(bookOptional.isEmpty()){
            throw new BookNotFoundException("Book not found");
        }

        User user = userOptional.get();
        Book book = bookOptional.get();

        boolean removed = user.getBookIssuedList().removeIf(b -> b.getIssuedId().equals(issuedId));
        if (!removed) {
            throw  new BookIssuedNotFoundException("Issued book not found for this user");
        }

        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
        userRepository.save(user);
        bookIssuedRepository.deleteById(issuedId);
    }
}
