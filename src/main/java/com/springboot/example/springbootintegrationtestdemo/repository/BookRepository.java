package com.springboot.example.springbootintegrationtestdemo.repository;

import com.springboot.example.springbootintegrationtestdemo.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {

    Book findByName(String name);
}
