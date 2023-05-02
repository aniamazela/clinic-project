package pl.mazela.project.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.mazela.project.models.Booking;
import pl.mazela.project.models.Status;
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
    // User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    // String pacient = user.getUsername();
    
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

    @GetMapping("showBookingAfterToday")
    public String showMyBookingsAfterToday(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String pacient = user.getUsername();
        model.addAttribute("bookings", 
        bookingRepo.findByDateisAfterTodayForPacient(pacient, Sort.by("date").
        ascending().and(Sort.by("time"))));
        model.addAttribute("doctorRepo", doctorRepo);
        return "myBooking";
    }
    
    @GetMapping("showBookingBeforeToday")
    public String showMyBookingsBeforeToday(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String pacient = user.getUsername();
        model.addAttribute("bookings", 
        bookingRepo.findByDateisBeforeTodayForPacient(pacient, Sort.by("date").
        ascending().and(Sort.by("time"))));
        model.addAttribute("doctorRepo", doctorRepo);
        return "myBooking";
    }

    @GetMapping("showDeleted")
    public String showMyBookingsDeleted(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String pacient = user.getUsername();
        model.addAttribute("bookings", bookingRepo.findByStatusForPacient(Status.deleted, pacient));
        model.addAttribute("doctorRepo", doctorRepo);
        return "myBooking";
    }

    @GetMapping("deleteBooking")
    public String deleteMyBooking(Model model, @ModelAttribute Booking booking, @RequestParam Long bid){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String pacient = user.getUsername();
        booking = bookingRepo.findById(bid).orElse(null);
        booking.setStatus(Status.deleted);
        bookingRepo.save(booking);
        model.addAttribute("bookings", 
        bookingRepo.findByDateisBeforeTodayForPacient(pacient, Sort.by("date").
        ascending().and(Sort.by("time"))));
        model.addAttribute("doctorRepo", doctorRepo);
        return "myBooking";
    }
    
}
