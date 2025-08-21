package com.libraryManagementSystem.services;

import com.libraryManagementSystem.dto.BookDto;
import com.libraryManagementSystem.entity.Book;
import com.libraryManagementSystem.exception.BookDeletionException;
import com.libraryManagementSystem.exception.BookNotFoundException;
import com.libraryManagementSystem.repository.BookRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @CachePut(value = "book", key = "#result.bookId")
    @CacheEvict(value = "books", allEntries = true)
    public Book createBook(BookDto bookDto){
        Book newBook = new Book();
        newBook.setBookId(UUID.randomUUID().toString());
        newBook.setBookName(bookDto.getBookName());
        newBook.setAuthorName(bookDto.getAuthorName());
        newBook.setBranch(bookDto.getBranch());
        newBook.setSemester(bookDto.getSemester());
        newBook.setAvailableCopies(bookDto.getAvailableCopies());
        newBook.setTotalCopies(bookDto.getTotalCopies());
        bookRepository.save(newBook);
        return newBook;
    }

    @Cacheable(value = "book", key = "#id")
    public Book findByBookId(String id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isEmpty()){
            throw new BookNotFoundException("Book not found");
        }
        Book book = bookOptional.get();
        return book;
    }

    @Cacheable(value = "book", key = "'name_' + #bookName")
    public Book findByBookName(String bookName) {
        Optional<Book> bookOptional = bookRepository.findByBookName(bookName);
        if(bookOptional.isEmpty()){
            throw new BookNotFoundException("Book not found");
        }
        Book book = bookOptional.get();
        return book;
    }

    @Cacheable(value = "books")
    public List<Book> getAllBook(){
        return bookRepository.findAll();
    }

    @CachePut(value = "book", key = "#result.bookId")
    @CacheEvict(value = "books", allEntries = true)
    public Book updateBook(BookDto bookDto, String id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isEmpty()){
            throw new BookNotFoundException("Book not found");
        }
        Book oldBook = bookOptional.get();
        oldBook.setBookName(bookDto.getBookName());
        oldBook.setAuthorName(bookDto.getAuthorName());
        oldBook.setBranch(bookDto.getBranch());
        oldBook.setSemester(bookDto.getSemester());
        oldBook.setAvailableCopies(bookDto.getAvailableCopies());
        oldBook.setTotalCopies(bookDto.getTotalCopies());
        bookRepository.save(oldBook);
        return oldBook;
    }

    @Cacheable(value = "books_by_semester_branch", key = "#semester + '-' + #branch")
    public List<Book> findBySemesterAndBranch(int semester, String branch) {
        return bookRepository.findBySemesterAndBranch(semester, branch);
    }

    @Caching(evict = {
            @CacheEvict(value = "book", key = "#id"),
            @CacheEvict(value = "books", allEntries = true),
            @CacheEvict(value = "books_by_semester_branch", allEntries = true)
    })
    public void deleteById(String id){
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isEmpty()){
            throw new BookNotFoundException("Book not found");
        }
        Book book = bookOptional.get();
        if (book.getAvailableCopies() != book.getTotalCopies()) {
            throw  new BookDeletionException("Book is issued to someone, so it cannot be deleted");
        }
        bookRepository.deleteById(id);
    }
}
