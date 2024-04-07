package ru.ershova.MalinkaCoffee.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(schema = "coffee", name = "history_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "id_person", referencedColumnName = "id")
    private Person person;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "id_coffee_point", referencedColumnName = "id")
    private CoffeePoint coffeePoint;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "id_drink", referencedColumnName = "id")
    private Drink drink;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "id_cake", referencedColumnName = "id")
    private Cake cake;

    @Column(name = "order_amount")
    private Double orderAmount;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;

    public HistoryOrder(Person person, CoffeePoint coffeePoint, Drink drink, Cake cake, Double orderAmount, LocalDateTime date) {
        this.person = person;
        this.coffeePoint = coffeePoint;
        this.drink = drink;
        this.cake = cake;
        this.orderAmount = orderAmount;
        this.date = date;
    }

    public static HistoryOrder of(UpcomingOrder upcomingOrder) {
        return new HistoryOrder(
                upcomingOrder.getPerson(),
                upcomingOrder.getCoffeePoint(),
                upcomingOrder.getDrink(),
                upcomingOrder.getCake(),
                upcomingOrder.getOrderAmount(),
                upcomingOrder.getDate()
        );
    }
}
