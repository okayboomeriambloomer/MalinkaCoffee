package ru.ershova.MalinkaCoffee.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ershova.MalinkaCoffee.model.CoffeePoint;
import ru.ershova.MalinkaCoffee.model.UpcomingOrder;

import java.util.Collections;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class CoffeePointsDAO {
    @PersistenceContext
    EntityManager entityManager;

    public List<CoffeePoint> getAllCoffeePoints() {
        return entityManager.createQuery("FROM CoffeePoint", CoffeePoint.class)
                .getResultList();
    }

    public CoffeePoint getById(Integer coffeePointId) {
        return entityManager.createQuery("FROM CoffeePoint WHERE id = ?1", CoffeePoint.class)
                .setParameter(1, coffeePointId)
                .getSingleResult();
    }

    @Transactional
    public void saveNewUpcomingOrder(CoffeePoint coffeePoint, UpcomingOrder upcomingOrder) {
        if (coffeePoint.getUpcomingOrders() == null) {
            coffeePoint.setUpcomingOrders(Collections.singletonList(upcomingOrder));
        } else {
            coffeePoint.getUpcomingOrders().add(upcomingOrder);
        }
    }

    public void save(CoffeePoint coffeePoint) {
        entityManager.persist(coffeePoint);
    }

    public boolean contains(CoffeePoint coffeePoint) {
        return !entityManager.createQuery("FROM CoffeePoint WHERE address = ?1", CoffeePoint.class)
                .setParameter(1, coffeePoint.getAddress())
                .getResultList().isEmpty();
    }

    @Transactional
    public void update(CoffeePoint coffeePoint) {
        entityManager.merge(coffeePoint);
    }

    @Transactional
    public void remove(Integer coffeePointId) {
        CoffeePoint coffeePoint = entityManager.createQuery("FROM CoffeePoint WHERE id = ?1", CoffeePoint.class)
                .setParameter(1, coffeePointId)
                .getSingleResult();
        entityManager.remove(coffeePoint);
    }
}
