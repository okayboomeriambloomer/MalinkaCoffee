package ru.ershova.MalinkaCoffee.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ershova.MalinkaCoffee.model.Drink;
import ru.ershova.MalinkaCoffee.model.Drink;
import ru.ershova.MalinkaCoffee.model.UpcomingOrder;
import ru.ershova.MalinkaCoffee.model.Volume;

import java.util.Collections;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DrinkDAO {
    @PersistenceContext
    private EntityManager entityManager;
    public List<Drink> getAllDrinks() {
        return entityManager.createQuery("FROM Drink", Drink.class)
                .getResultList();
    }

    public Drink getById(Integer drinkId) {
        return entityManager.createQuery("FROM Drink WHERE id = ?1", Drink.class)
                .setParameter(1, drinkId)
                .getSingleResult();
    }

    @Transactional
    public void saveNewUpcomingOrder(Drink drink, UpcomingOrder upcomingOrder) {
        if (drink.getUpcomingOrders() == null) {
            drink.setUpcomingOrders(Collections.singletonList(upcomingOrder));
        } else {
            drink.getUpcomingOrders().add(upcomingOrder);
        }
    }

    public List<Volume> getAllVolumes() {
        return entityManager.createQuery("FROM Volume", Volume.class)
                .getResultList();
    }

    @Transactional
    public void save(Drink drink, Integer volumeId) {
        Volume volume = entityManager.find(Volume.class, volumeId);
        drink.setVolume(volume);
        if (volume.getDrinks() == null) {
            volume.setDrinks(Collections.singletonList(drink));
        } else {
            volume.getDrinks().add(drink);
        }
        entityManager.persist(drink);
    }

    public boolean contains(Drink drink, Integer volumeId) {
        return !entityManager.createQuery("FROM Drink WHERE name = ?1 AND volume.id = ?2", Drink.class)
                .setParameter(1, drink.getName())
                .setParameter(2, volumeId)
                .getResultList().isEmpty();
    }

    @Transactional
    public void update(Drink drink, Integer volumeId) {
        Volume volume = entityManager.find(Volume.class, volumeId);
        drink.setVolume(volume);
        if (volume.getDrinks() == null) {
            volume.setDrinks(Collections.singletonList(drink));
        } else {
            volume.getDrinks().add(drink);
        }
        entityManager.merge(drink);
    }

    @Transactional
    public void remove(Integer drinkId) {
        Drink drink = entityManager.createQuery("FROM Drink WHERE id = ?1", Drink.class)
                .setParameter(1, drinkId)
                .getSingleResult();
        entityManager.remove(drink);
    }
}
