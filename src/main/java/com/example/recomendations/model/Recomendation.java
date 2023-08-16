package com.example.recomendations.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recomendation {

    private User user;
    private List<Book> recommendations;
    private List<Book> suggestions;
    
}
