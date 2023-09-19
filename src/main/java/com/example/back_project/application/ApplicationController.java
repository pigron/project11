package com.example.back_project.application;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping(value="/app")
public class ApplicationController {

    @PostMapping("/test/map")
    public Map<String, Object> test(@RequestBody Map<String, Object> body) {
        System.out.println(body);
        return body;
    }

    @PostMapping("/test/string")
    public String test1(@RequestBody String body) {
        System.out.println(body);
        return body;
    }

}