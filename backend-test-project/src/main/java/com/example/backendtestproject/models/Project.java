package com.example.backendtestproject.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SQLDelete(sql = "UPDATE projects SET deleted = true WHERE project_id=?")
@Where(clause = "deleted = false")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectId;

    @OneToOne(mappedBy = "project")
    private ProjectManager projectManager;

    @OneToMany(mappedBy = "project")
    @Builder.Default
    private List<Programmer> programmers = new ArrayList<>();

    private String client;

    private String startDate;

    private String description;

    private Boolean deleted = Boolean.FALSE;
}
