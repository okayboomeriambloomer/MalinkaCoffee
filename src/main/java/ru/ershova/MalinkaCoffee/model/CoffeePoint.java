package ru.ershova.MalinkaCoffee.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Entity
@Table(schema = "coffee", name = "coffee_point")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoffeePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "address")
    @NotEmpty(message = "Адрес не заполнен")
    private String address;

    @Column(name = "manager_name")
    @NotEmpty(message = "Имя управляющего не заполнено")
    @Pattern(regexp = "[а-яА-Яa-zA-Z]+", message = "Формат имени невалиден")
    private String managerName;

    @Column(name = "telephone_number")
    @NotEmpty(message = "Номер телефона не заполнен")
    @Pattern(regexp="(\\+7|8)[0-9]{10}", message = "Формат номера телефона невалиден")
    private String telephoneNumber;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "coffeePoint", cascade = CascadeType.REMOVE)
    List<UpcomingOrder> upcomingOrders;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "coffeePoint", cascade = CascadeType.REMOVE)
    List<HistoryOrder> historyOrders;
}
