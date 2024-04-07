package ru.ershova.MalinkaCoffee.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ershova.MalinkaCoffee.model.HistoryOrder;
import ru.ershova.MalinkaCoffee.model.Person;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class HistoryOrderDAO {
    @PersistenceContext
    EntityManager entityManager;
    public List<HistoryOrder> getAllHistoryOrders() {
        return entityManager.createQuery("FROM HistoryOrder ORDER BY date", HistoryOrder.class)
                .getResultList();
    }

    public List<HistoryOrder> getAllHistoryOrdersForPerson(Person person) {
        return entityManager.createQuery("FROM HistoryOrder WHERE person.id = ?1 ORDER BY date", HistoryOrder.class)
                .setParameter(1, person.getId())
                .getResultList();
    }

    @Transactional
    public void save(HistoryOrder historyOrder) {
        entityManager.persist(historyOrder);
    }
}
