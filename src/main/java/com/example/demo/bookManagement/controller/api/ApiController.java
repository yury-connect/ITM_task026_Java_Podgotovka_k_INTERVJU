package com.example.demo.bookManagement.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/hello")
public class ApiController {

    @GetMapping
    public String sayHello() {
        return "Hello from REST API!";
    }
}
