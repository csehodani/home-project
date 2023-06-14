package com.example.backendtestproject.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long address_id;

    private Integer zipCode;

    private String city;

    private String street;

    @OneToOne(mappedBy = "address")
    private ProjectManager projectManager;

    @OneToOne(mappedBy = "address")
    private Programmer programmer;
}
