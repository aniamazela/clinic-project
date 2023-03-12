package pl.mazela.project.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mazela.project.models.Booking;
import pl.mazela.project.repositories.AppointmentRepository;
import pl.mazela.project.repositories.BookingRepository;
import pl.mazela.project.repositories.DoctorRepository;



@Controller

public class SpecialistController {
    private AppointmentRepository appointmentRepo;
    private DoctorRepository doctorRepo;
    private BookingRepository bookingRepo;

    public SpecialistController(AppointmentRepository appointmentRepo, DoctorRepository doctorRepo,
                                BookingRepository bookingRepo    ) {
        this.appointmentRepo = appointmentRepo;
        this.doctorRepo=doctorRepo;
        this.bookingRepo=bookingRepo;
    }

    @GetMapping ("/doctor/{id}/{nameAndSurname}")
    public String showDoctor(@PathVariable Long id,
                             Model model, @PathVariable String nameAndSurname){
        Booking booking = new Booking();
        model.addAttribute("booking", booking);
        model.addAttribute("doctor", doctorRepo.findById(id).orElse(null));
        model.addAttribute("appointments",appointmentRepo.findAll());
        return "specialist";
    }

/*    @GetMapping()
    public String showBooking(Model model){
        model.addAttribute(new Booking());
        model.addAttribute("booking", bookingRepo.findAll());
        return "booking";
    }*/

    @PostMapping("/booking")
    public String createBooking(@ModelAttribute("booking") Booking booking){

        bookingRepo.save(booking);
                return "booking";
    }
}
