package ru.ershova.MalinkaCoffee.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Entity
@Table(schema = "coffee", name = "person")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Имя не заполнено")
    @Column(name = "name")
    @Pattern(regexp = "[а-яА-Яa-zA-Z]+", message = "Формат имени невалиден")
    private String name;

    @NotEmpty(message = "Фамилия не заполнена")
    @Column(name = "surname")
    @Pattern(regexp = "[а-яА-Яa-zA-Z]+", message = "Формат фамилии невалиден")
    private String surname;

    @NotEmpty(message = "Номер телефона не заполнен")
    @Pattern(regexp="(\\+7|8)[0-9]{10}", message = "Формат номера телефона невалиден")
    @Column(name = "telephone_number")
    private String telephoneNumber;

    @NotEmpty(message = "Пароль не может быть пустым")
    @Column(name = "password")
    private String password;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_role", referencedColumnName = "id")
    private Role role;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "person")
    List<UpcomingOrder> upcomingOrders;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "person")
    List<HistoryOrder> historyOrders;

    public Person(String name, String surname, String telephoneNumber, String password, Role role) {
        this.name = name;
        this.surname = surname;
        this.telephoneNumber = telephoneNumber;
        this.password = password;
        this.role = role;
    }
}
