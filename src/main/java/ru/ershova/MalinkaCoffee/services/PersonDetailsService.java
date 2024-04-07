package ru.ershova.MalinkaCoffee.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ershova.MalinkaCoffee.DAO.PersonDAO;
import ru.ershova.MalinkaCoffee.model.Person;
import ru.ershova.MalinkaCoffee.security.PersonDetails;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonDAO personDAO;

    @Autowired
    public PersonDetailsService(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personDAO.findByTelephoneNumber(username);
        if (person.isEmpty()) throw new UsernameNotFoundException("Пользователь с таким номером телефона не найден");
        return new PersonDetails(person.get());
    }
}
