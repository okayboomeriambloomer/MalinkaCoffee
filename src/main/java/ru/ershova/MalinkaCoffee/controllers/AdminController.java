package ru.ershova.MalinkaCoffee.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ershova.MalinkaCoffee.model.Cake;
import ru.ershova.MalinkaCoffee.model.CoffeePoint;
import ru.ershova.MalinkaCoffee.model.Drink;
import ru.ershova.MalinkaCoffee.security.PersonDetails;
import ru.ershova.MalinkaCoffee.services.MainService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    MainService mainService;

    @Autowired
    public AdminController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping
    public String getAdminPage(@AuthenticationPrincipal UserDetails userDetails,
                               Model model) {
        PersonDetails personDetails = (PersonDetails) userDetails;
        model.addAttribute("person", personDetails);
        return "/admin/main-page";
    }

    @GetMapping("/upcoming-orders")
    public String getUpcomingOrders(Model model) {
        model.addAttribute("upcomingOrders", mainService.getAllUpcomingOrders());
        return "/admin/upcoming-orders";
    }

    @PatchMapping("/upcoming-orders/{id}")
    public String replaceUpcomingToHistory(@PathVariable("id") Integer upcomingOrderId) {
        mainService.replaceUpcomingOrderToHistory(upcomingOrderId);
        return "redirect:/admin/upcoming-orders";
    }

    @GetMapping("/coffee-points")
    public String getCoffeePoints(Model model) {
        model.addAttribute("coffeePoints", mainService.getAllCoffeePoints());
        return "/admin/coffee-points";
    }

    @GetMapping("/coffee-points/new")
    public String getNewCoffeePointPage(@ModelAttribute("coffeePoint") CoffeePoint coffeePoint) {
        return "/admin/coffee-point-new";
    }

    @PostMapping("/coffee-points/new")
    public String createNewCoffeePoint(@ModelAttribute("coffeePoint") @Valid CoffeePoint coffeePoint,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/admin/coffee-point-new";
        }
        if (mainService.containsCoffeePoint(coffeePoint)) {
            bindingResult.rejectValue("address", "error.address",
                    "Точка с таким адресом уже существует");
            return "/admin/coffee-point-new";
        }
        mainService.addNewCoffeePoint(coffeePoint);
        return "redirect:/admin/coffee-points";
    }

    @GetMapping("/coffee-points/{id}")
    public String getUpdateCoffeePointPage(@PathVariable("id") Integer coffeePointId,
                                Model model) {
        model.addAttribute("coffeePoint", mainService.getCoffeePointById(coffeePointId));
        return "/admin/coffee-point-update";
    }

    @PatchMapping("/coffee-points/{id}")
    public String updateCoffeePoint(@ModelAttribute("coffeePoint") @Valid CoffeePoint coffeePoint,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/admin/coffee-points-update";
        }
        if (mainService.containsCoffeePoint(coffeePoint)) {
            bindingResult.rejectValue("address", "error.address",
                    "Точка с таким адресом уже существует");
            return "/admin/coffee-point-update";
        }
        mainService.updateCoffeePoint(coffeePoint);
        return "/admin/coffee-point-update";
    }

    @DeleteMapping("/coffee-points/{id}")
    public String deleteCoffeePoint(@PathVariable("id") Integer coffeePointId) {
        mainService.removeCoffeePoint(coffeePointId);
        return "redirect:/admin/coffee-points";
    }

    @GetMapping("/drinks")
    public String getDrinks(Model model) {
        model.addAttribute("drinks", mainService.getAllDrinks());
        return "/admin/drinks";
    }

    @GetMapping("/drinks/new")
    public String getNewDrinkPage(@ModelAttribute("drink") Drink drink,
                                  Model model) {
        model.addAttribute("volumes", mainService.getAllVolumes());
        model.addAttribute("volumeId", 1);
        return "/admin/drink-new";
    }

    @PostMapping("/drinks/new")
    public String createNewDrink(@ModelAttribute("drink") @Valid Drink drink,
                                 BindingResult bindingResult,
                                 @RequestParam("volumeId") Integer volumeId,
                                 Model model) {
        model.addAttribute("volumes", mainService.getAllVolumes());
        model.addAttribute("volumeId", volumeId);
        if (bindingResult.hasErrors()) {
            return "/admin/drink-new";
        }
        if (mainService.containsDrink(drink, volumeId)) {
            bindingResult.rejectValue("name", "error.name",
                    "Напиток с таким названием и объемом уже существует");
            return "/admin/drink-new";
        }
        mainService.addNewDrink(drink, volumeId);
        return "redirect:/admin/drinks";
    }

    @GetMapping("/drinks/{id}")
    public String getUpdateDrinkPage(@PathVariable("id") Integer drinkId,
                                    Model model) {
        Drink drink = mainService.getDrinkById(drinkId);
        model.addAttribute("drink", drink);
        model.addAttribute("volumes", mainService.getAllVolumes());
        model.addAttribute("volumeId", drink.getVolume().getId());
        return "/admin/drink-update";
    }

    @PatchMapping("/drinks/{id}")
    public String updateDrink(@ModelAttribute("drink") @Valid Drink drink,
                              BindingResult bindingResult,
                              @RequestParam("volumeId") Integer volumeId,
                              Model model) {
        model.addAttribute("volumes", mainService.getAllVolumes());
        model.addAttribute("volumeId", volumeId);
        if (bindingResult.hasErrors()) {
            return "/admin/drink-update";
        }
        if (mainService.containsDrink(drink, volumeId)) {
            bindingResult.rejectValue("name", "error.name",
                    "Напиток с таким названием и объемом уже существует");
            return "/admin/drink-update";
        }
        mainService.updateDrink(drink, volumeId);
        return "/admin/drink-update";
    }

    @DeleteMapping("/drinks/{id}")
    public String deleteDrink(@PathVariable("id") Integer drinkId) {
        mainService.removeDrink(drinkId);
        return "redirect:/admin/drinks";
    }

    @GetMapping("/cakes")
    public String getCakes(Model model) {
        model.addAttribute("cakes", mainService.getAllCakes());
        return "/admin/cakes";
    }

    @GetMapping("/cakes/new")
    public String getNewCakePage(@ModelAttribute("cake") Cake cake) {
        return "/admin/cake-new";
    }

    @PostMapping("/cakes/new")
    public String createNewCake(@ModelAttribute("cake") @Valid Cake cake,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/admin/cake-new";
        }
        if (mainService.containsCake(cake)) {
            bindingResult.rejectValue("name", "error.name",
                    "Десерт с таким названием уже существует");
            return "/admin/cake-new";
        }
        mainService.addNewCake(cake);
        return "redirect:/admin/cakes";
    }

    @GetMapping("/cakes/{id}")
    public String getUpdateCakePage(@PathVariable("id") Integer cakeId,
                                Model model) {
        model.addAttribute("cake", mainService.getCakeById(cakeId));
        return "/admin/cake-update";
    }

    @PatchMapping("/cakes/{id}")
    public String updateCake(@ModelAttribute("cake") @Valid Cake cake,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/admin/cake-update";
        }
        if (mainService.containsCake(cake)) {
            bindingResult.rejectValue("name", "error.name",
                    "Десерт с таким названием уже существует");
            return "/admin/cake-update";
        }
        mainService.updateCake(cake);
        return "/admin/cake-update";
    }

    @DeleteMapping("/cakes/{id}")
    public String deleteCake(@PathVariable("id") Integer cakeId) {
        mainService.removeCake(cakeId);
        return "redirect:/admin/cakes";
    }

    @GetMapping("/history-orders")
    public String getHistoryOrders(Model model) {
        model.addAttribute("historyOrders", mainService.getAllHistoryOrders());
        return "/admin/history-orders";
    }
}
