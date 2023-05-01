package pl.mazela.project.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.mazela.project.models.User;
import pl.mazela.project.repositories.BookingRepository;
import pl.mazela.project.repositories.DoctorRepository;


@Controller
@RequestMapping("/")
public class HomeController {

    HomeController(BookingRepository bookingRepo, DoctorRepository doctorRepo){
        this.bookingRepo=bookingRepo;
        this.doctorRepo=doctorRepo;
    }
    
    BookingRepository bookingRepo;
    DoctorRepository doctorRepo;
    
    @GetMapping
    public String home(){
        return "home";
    }  

    @GetMapping("/login")
    public String registerForm(Model model, @Valid User user, Errors errors) {
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/myBooking")
    public String showMyBooking(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String pacient = user.getUsername();
        model.addAttribute("bookings", bookingRepo.findAllByPacient(pacient));
        model.addAttribute("doctorRepo", doctorRepo);
        // SimpleDateFormat DateFormat = new SimpleDateFormat("dd.MM.yyyy");
        model.addAttribute("today", new Date());
        return "myBooking";
    }
    
}
