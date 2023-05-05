package pl.mazela.project.controller;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;

import pl.mazela.project.models.User;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String registerForm(Model model, @Valid User user, Errors errors) {
        model.addAttribute("user", new User());
        return "login";
    }

}
