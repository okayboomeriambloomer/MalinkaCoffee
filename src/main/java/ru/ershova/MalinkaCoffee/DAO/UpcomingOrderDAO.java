package ru.ershova.MalinkaCoffee.DAO;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ershova.MalinkaCoffee.model.*;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class UpcomingOrderDAO {
    @PersistenceContext
    EntityManager entityManager;

    public List<UpcomingOrder> getAllUpcomingOrders() {
        return entityManager.createQuery("FROM UpcomingOrder ORDER BY date", UpcomingOrder.class)
                .getResultList();
    }

    @Transactional
    public UpcomingOrder saveNewOrder(Person person,
                                      CoffeePoint coffeePoint,
                                      @Nullable Drink drink,
                                      @Nullable Cake cake,
                                      Double orderAmount,
                                      LocalDateTime date) {
        UpcomingOrder upcomingOrder = new UpcomingOrder(person, coffeePoint, drink, cake, orderAmount, date);
        entityManager.persist(upcomingOrder);
        return upcomingOrder;
    }

    public List<UpcomingOrder> getAllUpcomingOrdersForPerson(Person person) {
        return entityManager.createQuery("FROM UpcomingOrder WHERE person.id = ?1 ORDER BY date", UpcomingOrder.class)
                .setParameter(1, person.getId())
                .getResultList();
    }

    public UpcomingOrder getById(Integer upcomingOrderId) {
        return entityManager.createQuery("FROM UpcomingOrder WHERE id = ?1", UpcomingOrder.class)
                .setParameter(1, upcomingOrderId)
                .getSingleResult();
    }

    public void remove(UpcomingOrder upcomingOrder) {
        entityManager.remove(upcomingOrder);
    }
}
