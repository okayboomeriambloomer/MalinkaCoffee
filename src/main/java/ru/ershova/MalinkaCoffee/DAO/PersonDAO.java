package ru.ershova.MalinkaCoffee.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ershova.MalinkaCoffee.model.Person;
import ru.ershova.MalinkaCoffee.model.Role;
import ru.ershova.MalinkaCoffee.model.UpcomingOrder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class PersonDAO {
    @PersistenceContext
    EntityManager entityManager;

    public Optional<Person> findByTelephoneNumber(String number) {
        List<Person> personList = entityManager.createQuery("FROM Person WHERE telephoneNumber = ?1", Person.class)
                .setParameter(1, number)
                .getResultList();
        if (personList.isEmpty()) return Optional.empty();
        else return Optional.of(personList.get(0));
    }

    public Person getById(int personId) {
        return entityManager.createQuery("FROM Person WHERE id = ?1", Person.class)
                .setParameter(1, personId)
                .getSingleResult();
    }

    @Transactional
    public void saveNewUpcomingOrder(Person person, UpcomingOrder upcomingOrder) {
        if (person.getUpcomingOrders() == null) {
            person.setUpcomingOrders(Collections.singletonList(upcomingOrder));
        } else {
            person.getUpcomingOrders().add(upcomingOrder);
        }
    }

    @Transactional
    public void saveNewPerson(Person person) {
        Role role = entityManager.createQuery("FROM Role WHERE role = ?1", Role.class)
                .setParameter(1, "ROLE_CLIENT")
                .getSingleResult();

        person.setRole(role);

        if (role.getPersons() == null) {
            role.setPersons(Collections.singletonList(person));
        } else {
            role.getPersons().add(person);
        }

        entityManager.persist(person);
    }

    public boolean contains(Person person) {
        List<Person> persons = entityManager.createQuery("FROM Person WHERE telephoneNumber = ?1", Person.class)
                .setParameter(1, person.getTelephoneNumber())
                .getResultList();
        return !persons.isEmpty();
    }
}
