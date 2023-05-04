package pl.mazela.project.controller;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
import pl.mazela.project.repositories.AppointmentRepository;
import pl.mazela.project.repositories.BookingRepository;
import pl.mazela.project.repositories.DoctorRepository;

@Controller
@RequestMapping("/")
public class HomeController {

    HomeController(BookingRepository bookingRepo, DoctorRepository doctorRepo, AppointmentRepository appointmentRepo) {
        this.bookingRepo = bookingRepo;
        this.doctorRepo = doctorRepo;
        this.appointmentRepo = appointmentRepo;
    }

    BookingRepository bookingRepo;
    DoctorRepository doctorRepo;
    AppointmentRepository appointmentRepo;


    public String findPacient(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String pacient = user.getUsername(); 
        return pacient;
    }


    @GetMapping
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String registerForm(Model model, @Valid User user, Errors errors) {
        model.addAttribute("user", new User());
        return "login";
    }


    @GetMapping("showBookingAfterNow")
    public String showMyBookingsAfterToday(Model model) {
        model.addAttribute("bookings",
                bookingRepo.findByDateisAfterNowForPacient(findPacient(), Sort.by("date").ascending().and(Sort.by("time"))));
        model.addAttribute("doctorRepo", doctorRepo);
        model.addAttribute("KindfOfBooking", "Oczekujące wizyty");
        model.addAttribute("buttonName", "DEL&INFO");
        return "myBooking";
    }

    @GetMapping("showBookingBeforeNow")
    public String showMyBookingsBeforeToday(Model model) {
        model.addAttribute("bookings",
                bookingRepo.findByDateisBeforeNowForPacient(findPacient(), Sort.by("date").ascending().and(Sort.by("time"))));
        model.addAttribute("doctorRepo", doctorRepo);
        model.addAttribute("KindfOfBooking", "Zakończone wizyty");
        model.addAttribute("buttonName", "INFO");
        return "myBooking";
    }

    @GetMapping("showDeleted")
    public String showMyBookingsDeleted(Model model) {
        model.addAttribute("bookings", bookingRepo.findByStatusForPacient(Status.deleted, findPacient(),
                Sort.by("date").ascending().and(Sort.by("time"))));
        model.addAttribute("doctorRepo", doctorRepo);
        model.addAttribute("KindfOfBooking", "Odwołane wizyty");
        model.addAttribute("buttonName", "INFO");
        return "myBooking";
    }

    @GetMapping("deleteBooking")
    public String deleteMyBooking(Model model, @ModelAttribute Booking booking, @RequestParam Long bid) {
        booking = bookingRepo.findById(bid).orElse(null);
        booking.setStatus(Status.deleted);
        bookingRepo.save(booking);
        model.addAttribute("bookings",
                bookingRepo.findByDateisAfterNowForPacient(findPacient(), Sort.by("date").ascending().and(Sort.by("time"))));
        model.addAttribute("doctorRepo", doctorRepo);
        model.addAttribute("KindfOfBooking", "Oczekujące wizyty");
        model.addAttribute("buttonName", "DEL&INFO");
        return "myBooking";
    }

    @GetMapping("bookingInfo")
    public String showInfo(@RequestParam Long bid, Model model){
        model.addAttribute("booking", bookingRepo.findById(bid).orElse(null));
        model.addAttribute("doctor", doctorRepo.findById(bookingRepo.findById(bid).orElse(null).getIdDoctor()).get());        
        Date date = bookingRepo.findById(bid).orElse(null).getDate();
        LocalTime time=bookingRepo.findById(bid).orElse(null).getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Calendar calendarBooking = new GregorianCalendar(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            time.getHour(),
            time.getMinute(),
            time.getSecond());

        if (bookingRepo.findById(bid).orElse(null).getStatus().equals(Status.deleted)){
            model.addAttribute("status", "odwołana");
        } else if (calendarBooking.getTime().after(new Date())){
            model.addAttribute("status", "oczekująca");
        } else {
            model.addAttribute("status", "zakończona");
        }

        model.addAttribute("appointment", 
        (appointmentRepo.findByType(bookingRepo.findById(bid).orElse(null).getType())));
        return "bookingInfo";

    }

}
