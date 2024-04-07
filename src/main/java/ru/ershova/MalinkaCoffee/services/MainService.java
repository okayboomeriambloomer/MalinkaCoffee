package ru.ershova.MalinkaCoffee.services;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ershova.MalinkaCoffee.DAO.*;
import ru.ershova.MalinkaCoffee.model.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MainService {
    private PersonDAO personDAO;
    private CakeDAO cakeDAO;
    private DrinkDAO drinkDAO;
    private CoffeePointsDAO coffeePointsDAO;
    private UpcomingOrderDAO upcomingOrderDAO;
    private HistoryOrderDAO historyOrderDAO;

    @Autowired
    public MainService(PersonDAO personDAO,
                       CakeDAO cakeDAO,
                       DrinkDAO drinkDAO,
                       CoffeePointsDAO coffeePointsDAO,
                       UpcomingOrderDAO upcomingOrderDAO,
                       HistoryOrderDAO historyOrderDAO) {
        this.personDAO = personDAO;
        this.cakeDAO = cakeDAO;
        this.drinkDAO = drinkDAO;
        this.coffeePointsDAO = coffeePointsDAO;
        this.upcomingOrderDAO = upcomingOrderDAO;
        this.historyOrderDAO = historyOrderDAO;
    }

    public List<Drink> getAllDrinks() {
        return drinkDAO.getAllDrinks();
    }

    public List<Cake> getAllCakes() {
        return cakeDAO.getAllCakes();
    }

    public List<CoffeePoint> getAllCoffeePoints() {
        return coffeePointsDAO.getAllCoffeePoints();
    }

    public List<UpcomingOrder> getAllUpcomingOrders() {
        return upcomingOrderDAO.getAllUpcomingOrders();
    }

    public List<HistoryOrder> getAllHistoryOrders() {
        return historyOrderDAO.getAllHistoryOrders();
    }

    public Drink getDrinkById(Integer drinkId) {
        return drinkDAO.getById(drinkId);
    }

    public Cake getCakeById(Integer cakeId) {
        return cakeDAO.getById(cakeId);
    }

    public CoffeePoint getCoffeePointById(Integer coffeePointId) {
        return coffeePointsDAO.getById(coffeePointId);
    }

    @Transactional
    public void addOrderToUpcoming(Integer personId,
                                   Integer coffeePointId,
                                   @Nullable Integer drinkId,
                                   @Nullable Integer cakeId,
                                   Double orderAmount,
                                   LocalDateTime date) {
        Person person = personDAO.getById(personId);
        CoffeePoint coffeePoint = coffeePointsDAO.getById(coffeePointId);
        Drink drink = (drinkId == null) ? null : drinkDAO.getById(drinkId);
        Cake cake = (cakeId == null) ? null : cakeDAO.getById(cakeId);

        UpcomingOrder upcomingOrder = upcomingOrderDAO.saveNewOrder(person, coffeePoint, drink, cake, orderAmount, date);
        personDAO.saveNewUpcomingOrder(person, upcomingOrder);
        coffeePointsDAO.saveNewUpcomingOrder(coffeePoint, upcomingOrder);
        if (drink != null) drinkDAO.saveNewUpcomingOrder(drink, upcomingOrder);
        if (cake != null) cakeDAO.saveNewUpcomingOrder(cake, upcomingOrder);
    }

    public List<UpcomingOrder> getAllUpcomingOrdersForPerson(Integer personId) {
        Person person = personDAO.getById(personId);
        return upcomingOrderDAO.getAllUpcomingOrdersForPerson(person);
    }

    public List<HistoryOrder> getAllHistoryOrdersForPerson(Integer personId) {
        Person person = personDAO.getById(personId);
        return historyOrderDAO.getAllHistoryOrdersForPerson(person);
    }

    @Transactional
    public void addNewPerson(Person person) {
        personDAO.saveNewPerson(person);
    }

    public boolean containsPerson(Person person) {
        return personDAO.contains(person);
    }

    @Transactional
    public void replaceUpcomingOrderToHistory(Integer upcomingOrderId) {
        UpcomingOrder upcomingOrder = upcomingOrderDAO.getById(upcomingOrderId);
        HistoryOrder historyOrder = HistoryOrder.of(upcomingOrder);
        historyOrderDAO.save(historyOrder);
        upcomingOrderDAO.remove(upcomingOrder);
    }

    @Transactional
    public void addNewCoffeePoint(CoffeePoint coffeePoint) {
        coffeePointsDAO.save(coffeePoint);
    }

    public boolean containsCoffeePoint(CoffeePoint coffeePoint) {
        return coffeePointsDAO.contains(coffeePoint);
    }

    @Transactional
    public void updateCoffeePoint(CoffeePoint coffeePoint) {
        coffeePointsDAO.update(coffeePoint);
    }

    @Transactional
    public void removeCoffeePoint(Integer coffeePointId) {
        coffeePointsDAO.remove(coffeePointId);
    }

    @Transactional
    public void addNewDrink(Drink drink, Integer volumeId) {
        drinkDAO.save(drink, volumeId);
    }

    @Transactional
    public boolean containsDrink(Drink drink, Integer volumeId) {
        return drinkDAO.contains(drink, volumeId);
    }

    @Transactional
    public void updateDrink(Drink drink, Integer volumeId) {
        drinkDAO.update(drink, volumeId);
    }

    @Transactional
    public void removeDrink(Integer drinkId) {
        drinkDAO.remove(drinkId);
    }

    @Transactional
    public void addNewCake(Cake cake) {
        cakeDAO.save(cake);
    }

    public boolean containsCake(Cake cake) {
        return cakeDAO.contains(cake);
    }

    @Transactional
    public void updateCake(Cake cake) {
        cakeDAO.update(cake);
    }

    @Transactional
    public void removeCake(Integer cakeId) {
        cakeDAO.remove(cakeId);
    }

    public List<Volume> getAllVolumes() {
        return drinkDAO.getAllVolumes();
    }
}
