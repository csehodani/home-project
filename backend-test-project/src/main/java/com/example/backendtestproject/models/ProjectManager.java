package com.example.backendtestproject.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project_managers")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SQLDelete(sql = "UPDATE project_managers SET deleted = true WHERE project_manager_id=?")
@Where(clause = "deleted = false")
public class ProjectManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_manager_id")
    private Long projectManagerId;

    private String name;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "birth_date_id")
    private BirthDate birthDate;

    private String phoneNumber;

    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "projectManager", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private List<Programmer> programmers = new ArrayList<>();

    private Boolean deleted = Boolean.FALSE;

    public void addProgrammer(Programmer programmer) {
        this.programmers.add(programmer);
        programmer.addProjectManager(this);
    }
}
