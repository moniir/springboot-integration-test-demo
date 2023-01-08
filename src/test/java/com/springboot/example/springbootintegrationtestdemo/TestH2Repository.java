package com.springboot.example.springbootintegrationtestdemo;

import com.springboot.example.springbootintegrationtestdemo.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestH2Repository extends JpaRepository<Book,Integer> {

}
