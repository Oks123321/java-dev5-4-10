package com.goit.javacore5.feature.human;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Human {
    private long id;
    private String name;
    private LocalDate birthday;
    private Gender gender;

    public enum Gender {
        male,
        female
    }
}