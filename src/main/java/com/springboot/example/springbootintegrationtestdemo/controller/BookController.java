package com.springboot.example.springbootintegrationtestdemo.controller;

import com.springboot.example.springbootintegrationtestdemo.entity.Book;
import com.springboot.example.springbootintegrationtestdemo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService service;

    @PostMapping("/addbook")
    public Book addBook(@RequestBody Book book) {
        book = service.saveBook(book);
        return book;
    }
    @GetMapping
    public List<Book> findAllBooks() {
        return service.getBooks();
    }

    @GetMapping("/{id}")
    public Book findProductById(@PathVariable int id) {
        return service.getBookById(id);
    }


    @PutMapping("/update/{id}")
    public Book updateProduct(@RequestBody Book book, @PathVariable int id) {
        return service.updateBook(id, book);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        return service.deleteBook(id);
    }
}
