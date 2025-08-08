package com.libraryManagementSystem.services;

import com.libraryManagementSystem.entity.Book;
import com.libraryManagementSystem.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl {

    @Autowired
    private BookRepository bookRepository;

    @Cacheable(value = "book", key = "#id")
    public Optional<Book> findByBookId(String id) {
        return bookRepository.findById(id);
    }

    @Cacheable(value = "books")
    public List<Book> getAllBook(){
        return bookRepository.findAll();
    }

    @CachePut(value = "book", key = "#book.bookId")
    @CacheEvict(value = "books", allEntries = true)
    public Book saveBook(Book book){
        return bookRepository.save(book);
    }

    @Cacheable(value = "book", key = "'name_' + #bookName")
    public Optional<Book> findByBookName(String bookName) {
        return bookRepository.findByBookName(bookName);
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
        bookRepository.deleteById(id);
    }
}
