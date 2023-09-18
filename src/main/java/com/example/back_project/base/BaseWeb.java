package com.example.back_project.base;

import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

public class BaseWeb {
    public WebClient baseWeb(){
        return WebClient.builder()
                .baseUrl("http://192.168.100.75:30780/api/v4/projects")
                .build();
    }

    public WebClient userWeb(){
        return WebClient.builder()
                .baseUrl("http://192.168.100.75:30780/api/v4")
                .build();
    }

    public WebClient normalWeb() {
        return WebClient.create();
    }

    public WebClient argoWeb() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcmdvY2QiLCJzdWIiOiJhZG1pbjphcGlLZXkiLCJuYmYiOjE2ODA2NTc2MTMsImlhdCI6MTY4MDY1NzYxMywianRpIjoiYXBpdGVzdCJ9.rathQt_SFa7FSHdXaYgIBu8cjORsfdAf6F-8ZJU820w";

        return WebClient.builder()
                .baseUrl("http://192.168.100.75:30880/api/v1")
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
    }
}