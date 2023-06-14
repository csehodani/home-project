package com.example.backendtestproject.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "birth_dates")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BirthDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long birth_date_id;

    private Integer day;

    private Integer month;

    private Integer year;

    @OneToOne(mappedBy = "birthDate")
    private ProjectManager projectManager;

    @OneToOne(mappedBy = "birthDate")
    private Programmer programmer;

    public BirthDate(Integer day, Integer month, Integer year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }
}
