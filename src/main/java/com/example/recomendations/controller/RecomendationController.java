package com.example.recomendations.controller;

import com.example.recomendations.model.Book;
import com.example.recomendations.model.Recomendation;
import com.example.recomendations.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RecomendationController {

    @Value("${recommendations.books.api}")
    private String booksApiHots;

    @Value("${recommendations.books.port}")
    private String booksApiPort;

    @PostMapping("/")
    public Recomendation getRecomendationsForUser(@RequestBody User user) {
        RestTemplate restTemplate = new RestTemplate();

        List<Book> recommendations = new ArrayList<>();
        for(String autor:user.getAutores()) {
            for(String genero:user.getGeneros()) {
                String url = "http://" + this.booksApiHots + ":" + this.booksApiPort +
                        "/?gender=" + genero + "&autor=" + autor;
                List<Book> books = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>() {}).getBody();
                if (books != null ) {
                    books = books.stream().filter(book -> !recommendations.contains(book)).collect(Collectors.toList());
                    recommendations.addAll(books);
                }
            }
        }

        List<Book> suggestions = new ArrayList<>();
        for(String autor:user.getAutores()) {
            String url = "http://" + this.booksApiHots + ":" + this.booksApiPort + "/?autor=" + autor;
            List<Book> books = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>() {}).getBody();
            if (books != null ) {
                books = books.stream().filter(book -> !recommendations.contains(book)).collect(Collectors.toList());
                books = books.stream().filter(book -> !suggestions.contains(book)).collect(Collectors.toList());
                suggestions.addAll(books);
            }
        }
        for(String genero:user.getGeneros()) {
            String url = "http://" + this.booksApiHots + ":" + this.booksApiPort + "/?gender=" + genero;
            List<Book> books = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>() {}).getBody();
            if (books != null ) {
                books = books.stream().filter(book -> !recommendations.contains(book)).collect(Collectors.toList());
                books = books.stream().filter(book -> !suggestions.contains(book)).collect(Collectors.toList());
                suggestions.addAll(books);
            }
        }

        return Recomendation.builder().user(user).recommendations(recommendations).suggestions(suggestions).build();
    }
}
