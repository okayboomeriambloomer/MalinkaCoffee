package ru.ershova.MalinkaCoffee.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Entity
@Table(schema = "coffee", name = "drink")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Drink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Название товара не заполнено")
    @Column(name = "name")
    @Pattern(regexp = "[а-яА-Яa-zA-Z]+", message = "Формат названия товара невалиден")
    private String name;

    @Column(name = "price")
    @NotNull(message = "Стоимость напитка не заполнена")
    private double price;

    @ManyToOne
    @JoinColumn(name = "id_volume", referencedColumnName = "id")
    Volume volume;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "drink")
    List<UpcomingOrder> upcomingOrders;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "drink")
    List<HistoryOrder> historyOrders;

    public Drink(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
