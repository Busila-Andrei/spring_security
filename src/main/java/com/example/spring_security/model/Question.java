package com.example.spring_security.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ElementCollection
    private List<String> options;

    private String correctAnswer;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

}
