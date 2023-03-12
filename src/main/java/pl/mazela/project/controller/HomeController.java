package pl.mazela.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.mazela.project.models.User;


@Controller
@RequestMapping("/")
public class HomeController {
    

    @GetMapping
    public String home(){
        return "home";
    }  

    @GetMapping("/login")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }
    
}
