package com.libraryManagementSystem.services;

import com.libraryManagementSystem.entity.Book;
import com.libraryManagementSystem.repository.BookRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Optional<Book> findByBookId(ObjectId id) {
        return bookRepository.findById(id);
    }

    public List<Book> getAllBook(){
        return bookRepository.findAll();
    }

    public void saveBook(Book book){
        bookRepository.save(book);
    }

    public Optional<Book> findByBookName(String bookName) {
        return bookRepository.findByBookName(bookName);
    }

    public List<Book> findBySemesterAndBranch(int semester, String branch) {
        return bookRepository.findBySemesterAndBranch(semester, branch);
    }

    public void deleteById(ObjectId id){
        bookRepository.deleteById(id);
    }
}
