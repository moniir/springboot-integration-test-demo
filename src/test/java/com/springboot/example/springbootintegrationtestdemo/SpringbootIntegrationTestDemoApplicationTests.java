package com.springboot.example.springbootintegrationtestdemo;

import com.springboot.example.springbootintegrationtestdemo.entity.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringbootIntegrationTestDemoApplicationTests {

    @LocalServerPort
    private int port;

    private String baseurl = "http://localhost";
    private static RestTemplate restTemplate;

    @Autowired
    private TestH2Repository testH2Repository;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }
    @BeforeEach
    public void setUp(){
        baseurl = baseurl.concat(":").concat(port+"").concat("/books");

    }

    @Test
    public void testAddBook(){
        Book book = new Book("Modern Poetry",350);
        Book bookTestEntry = restTemplate.postForObject(baseurl.concat("/addbook"),book,Book.class);
        assertEquals("Modern Poetry",bookTestEntry.getName());
    }
    @Test
    @Sql(statements = "INSERT INTO book (id,name, price) VALUES (4,'William Shakespeare', 3000)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM book WHERE name='William Shakespeare'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetBooks() {
        List<Book> books = restTemplate.getForObject(baseurl, List.class);
        assertEquals(1, books.size());
        assertEquals(1, testH2Repository.findAll().size());
    }
    @Test
    @Sql(statements = "INSERT INTO book (id,name, price) VALUES (1,'Shakespeare', 3340)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM book WHERE id=1", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testFindBookById() {
        Book book = restTemplate.getForObject(baseurl
                + "/{id}", Book.class, 1);
        assertAll(
                () -> assertNotNull(book),
                () -> assertEquals(1, book.getId()),
                () -> assertEquals("Shakespeare", book.getName())
        );
    }
    @Test
    @Sql(statements = "INSERT INTO book (id,name, price) VALUES (2,'South asian and african literature', 999)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM book WHERE id=1", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUpdateBook(){
        Book book = new Book("shoes", 1999);
        restTemplate.put(baseurl+"/update/{id}", book, 2);
        Book bookFromDB = testH2Repository.findById(2).get();
        assertAll(
                () -> assertNotNull(bookFromDB),
                () -> assertEquals(1999, bookFromDB.getPrice())
        );
    }

    @Test
    @Sql(statements = "INSERT INTO book (id,name, price) VALUES (8,'some books', 1499)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteBook(){
        int recordCount=testH2Repository.findAll().size();
        assertEquals(1, recordCount);
        restTemplate.delete(baseurl+"/delete/{id}", 8);
        assertEquals(0, testH2Repository.findAll().size());

    }
}
