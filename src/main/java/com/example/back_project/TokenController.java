package com.example.back_project;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("http://localhost:8081")
@RestController
@RequestMapping(value="/*")
public class TokenController {

    @GetMapping("*")
    public void test() {
        System.out.println("joker");
    }

}