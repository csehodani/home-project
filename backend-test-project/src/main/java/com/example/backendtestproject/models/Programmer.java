package com.example.backendtestproject.models;

import com.example.backendtestproject.enums.Responsibility;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "programmers")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@SQLDelete(sql = "UPDATE programmers SET deleted = true WHERE programmer_id=?")
@Where(clause = "deleted = false")
public class Programmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "programmer_id")
    private Long programmerId;

    private String name;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "birth_date_id")
    private BirthDate birthDate;

    private String phoneNumber;

    private String email;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "project_manager_id")
    private ProjectManager projectManager;

    @Enumerated(value = EnumType.STRING)
    private Responsibility responsibility;

    private Boolean isApprentice;

    private Boolean deleted = Boolean.FALSE;

    public void addProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }
}
