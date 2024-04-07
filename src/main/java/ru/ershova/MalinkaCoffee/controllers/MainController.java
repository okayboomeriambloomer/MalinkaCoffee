package ru.ershova.MalinkaCoffee.controllers;

import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ershova.MalinkaCoffee.model.Cake;
import ru.ershova.MalinkaCoffee.model.Drink;
import ru.ershova.MalinkaCoffee.model.Person;
import ru.ershova.MalinkaCoffee.security.PersonDetails;
import ru.ershova.MalinkaCoffee.services.MainService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Controller
public class MainController {

    MainService mainService;

    @Autowired
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/")
    public String getHomePage() {
        return "home-page";
    }

    @GetMapping("/forbidden")
    public String forbidden(@AuthenticationPrincipal UserDetails userDetails,
                            Model model) {
        model.addAttribute("role", new ArrayList<>(userDetails.getAuthorities()).get(0).getAuthority());
        return "forbidden";
    }

    @GetMapping("/client-side")
    public String getClientPage(@AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        PersonDetails personDetails = (PersonDetails) userDetails;
        model.addAttribute("person", personDetails);
        return "client-page";
    }

    @GetMapping("/register")
    public String getRegisterPage(@ModelAttribute("person") Person person) {
        return "register-page";
    }

    @PostMapping("/register")
    public String confirmRegister(@ModelAttribute("person") @Valid Person person,
                                  BindingResult bindingResult) {
        System.out.println(person);
        if (bindingResult.hasErrors()) return "register-page";
        if (mainService.containsPerson(person)) {
            bindingResult.rejectValue(
                    "id",
                    "error.id",
                    "Пользователь с таким номером телефона уже существует");
            return "register-page";
        }
        mainService.addNewPerson(person);
        return "redirect:/";
    }

    @GetMapping("/client-side/get-order")
    public String getOrderPage(@RequestParam(value = "cakeId", required = false) Integer cakeId,
                               @RequestParam(value = "drinkId", required = false) Integer drinkId,
                               @RequestParam(value = "coffeePointId", required = false) Integer coffeePointId,
                               @RequestParam(value = "date", required = false) LocalDateTime date,
                               @RequestParam(value = "isDrink", required = false) Boolean isDrink,
                               @RequestParam(value = "isCake", required = false) Boolean isCake,
                               Model model) {
        model.addAttribute("drinkId", (drinkId == null) ? 0 : drinkId);
        model.addAttribute("cakeId", (cakeId == null) ? 0 : cakeId);
        model.addAttribute("coffeePointId", (coffeePointId == null) ? 0 : coffeePointId);
        if (date == null) {
            date = LocalDateTime.now().plusHours(1);
        }

        model.addAttribute("drinks", mainService.getAllDrinks());
        model.addAttribute("cakes", mainService.getAllCakes());
        model.addAttribute("coffeePoints", mainService.getAllCoffeePoints());
        model.addAttribute("date", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        model.addAttribute("noDrink", (isDrink == null || isDrink) ? null : "on");
        model.addAttribute("noCake", (isCake == null || isCake) ? null : "on");
        return "get-order";
    }

    @PostMapping("/client-side/get-order-processing")
    public String getOrderProcessing(@RequestParam("drinkId") Integer drinkId,
                                     @RequestParam("cakeId") Integer cakeId,
                                     @RequestParam(value = "noDrink", required = false) String noDrink,
                                     @RequestParam(value = "noCake", required = false) String noCake,
                                     @RequestParam("coffeePointId") Integer coffeePointId,
                                     @RequestParam("date") LocalDateTime date,
                                     Model model) {
        Drink drink = mainService.getDrinkById(drinkId);
        Cake cake = mainService.getCakeById(cakeId);
        model.addAttribute("isDrink", noDrink == null);
        model.addAttribute("drink", drink);
        model.addAttribute("isCake", noCake == null);
        model.addAttribute("cake", cake);
        model.addAttribute("orderAmount",
                (noDrink == null ? drink.getPrice() : 0) + (noCake == null ? cake.getPrice() : 0));
        model.addAttribute("coffeePoint", mainService.getCoffeePointById(coffeePointId));
        model.addAttribute("date", date);

        return "get-order-accept";
    }

    @PostMapping("/client-side/get-order-accept")
    public String getOrderAccept(@RequestParam("cakeId") Integer cakeId,
                                 @RequestParam("drinkId") Integer drinkId,
                                 @RequestParam("coffeePointId") Integer coffeePointId,
                                 @RequestParam("date") LocalDateTime date,
                                 @RequestParam("isDrink") Boolean isDrink,
                                 @RequestParam("isCake") Boolean isCake,
                                 @RequestParam("orderAmount") Double orderAmount,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        PersonDetails personDetails = (PersonDetails) userDetails;
        mainService.addOrderToUpcoming(
                personDetails.getId(),
                coffeePointId,
                (isDrink) ? drinkId : null,
                (isCake) ? cakeId : null,
                orderAmount,
                date);
        return "redirect:/client-side";
    }

    @GetMapping("/client-side/orders")
    public String getClientOrders(@AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        PersonDetails personDetails = (PersonDetails) userDetails;
        model.addAttribute("upcomingOrders",
                mainService.getAllUpcomingOrdersForPerson(personDetails.getId()));
        model.addAttribute("historyOrders",
                mainService.getAllHistoryOrdersForPerson(personDetails.getId()));
        model.addAttribute("person", personDetails);
        return "client-orders";
    }
}
