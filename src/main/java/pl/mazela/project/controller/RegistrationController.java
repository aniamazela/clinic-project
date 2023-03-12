package pl.mazela.project.controller;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.mazela.project.models.User;
import pl.mazela.project.repositories.UserRepository;
import pl.mazela.project.security.RegistrationForm;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "registerForm";
    }

    @PostMapping
    public String processRegister(Model model, @ModelAttribute("user") @Valid RegistrationForm form,
            BindingResult result) {

        if (result.hasErrors()) {
            model.addAttribute("message", "Formularz zawiera błędy!");
            return "registerForm";
        }

        if (userRepo.findByUsername(form.getUsername()) != null) {
            model.addAttribute("message", "Użytkownik juz istnieje!");
            return "registerForm";
        }

        //User user = 
        userRepo.save(form.toUser(passwordEncoder));
       // model.addAttribute("user", user);
        return "redirect:/login";

    }
}
