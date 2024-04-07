package ru.ershova.MalinkaCoffee.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(schema = "coffee", name = "volume")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Volume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "volume")
    private String volume;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "volume")
    List<Drink> drinks;

    public Volume(String volume) {
        this.volume = volume;
    }
}
