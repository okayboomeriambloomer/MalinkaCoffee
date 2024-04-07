package ru.ershova.MalinkaCoffee.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ershova.MalinkaCoffee.model.Cake;
import ru.ershova.MalinkaCoffee.model.UpcomingOrder;

import java.util.Collections;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class CakeDAO {
    @PersistenceContext
    EntityManager entityManager;
    public List<Cake> getAllCakes() {
        return entityManager.createQuery("FROM Cake", Cake.class)
                .getResultList();
    }

    public Cake getById(Integer cakeId) {
        return entityManager.createQuery("FROM Cake WHERE id = ?1", Cake.class)
                .setParameter(1, cakeId)
                .getSingleResult();
    }

    @Transactional
    public void saveNewUpcomingOrder(Cake cake, UpcomingOrder upcomingOrder) {
        if (cake.getUpcomingOrders() == null) {
            cake.setUpcomingOrders(Collections.singletonList(upcomingOrder));
        } else {
            cake.getUpcomingOrders().add(upcomingOrder);
        }
    }

    public void save(Cake cake) {
        entityManager.persist(cake);
    }

    public boolean contains(Cake cake) {
        return !entityManager.createQuery("FROM Cake WHERE name = ?1", Cake.class)
                .setParameter(1, cake.getName())
                .getResultList().isEmpty();
    }

    @Transactional
    public void update(Cake cake) {
        entityManager.merge(cake);
    }

    @Transactional
    public void remove(Integer cakeId) {
        Cake cake = entityManager.createQuery("FROM Cake WHERE id = ?1", Cake.class)
                .setParameter(1, cakeId)
                .getSingleResult();
        entityManager.remove(cake);
    }
}
