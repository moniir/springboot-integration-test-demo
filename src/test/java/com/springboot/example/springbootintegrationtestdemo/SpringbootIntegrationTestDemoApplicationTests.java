package com.springboot.example.springbootintegrationtestdemo;

import com.springboot.example.springbootintegrationtestdemo.entity.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
