package ru.ershova.MalinkaCoffee.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(schema = "coffee", name = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "role")
    private String role;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "role")
    List<Person> persons;

    public Role(String role) {
        this.role = role;
    }
}
