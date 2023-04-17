package pl.mazela.project.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;



import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mazela.project.models.Booking;
import pl.mazela.project.models.Doctor;
import pl.mazela.project.models.Status;
import pl.mazela.project.models.User;
import pl.mazela.project.repositories.AppointmentRepository;
import pl.mazela.project.repositories.BookingRepository;
import pl.mazela.project.repositories.DoctorRepository;

@Controller

public class SpecialistController {
    private AppointmentRepository appointmentRepo;
    private DoctorRepository doctorRepo;
    private BookingRepository bookingRepo;
    Doctor myDoctor;

    public SpecialistController(AppointmentRepository appointmentRepo, DoctorRepository doctorRepo,
            BookingRepository bookingRepo) {
        this.appointmentRepo = appointmentRepo;
        this.doctorRepo = doctorRepo;
        this.bookingRepo = bookingRepo;
    }

    // @GetMapping("/doctor/{id}/{nameAndSurname}")
    // public String showDoctor(@PathVariable Long id,
    // Model model, @PathVariable String nameAndSurname) {
    // LocalDate today = LocalDate.now();
    // LocalDate maxDate = today.plusDays(32);
    // model.addAttribute("minDate", today);
    // model.addAttribute("maxDate", maxDate);
    // Booking booking = new Booking();
    // model.addAttribute("booking", booking);
    // myDoctor = doctorRepo.findById(id).orElse(null);
    // model.addAttribute("doctor", doctorRepo.findById(id).orElse(null));
    // model.addAttribute("appointments", appointmentRepo.findAll());
    // model.addAttribute("day1", doctorRepo.findById(id).orElse(null).getDayOn1());
    // model.addAttribute("day2", doctorRepo.findById(id).orElse(null).getDayOn2());
    // return "specialistDateOk";
    // }

    // @GetMapping("/doctor/{id}/{nameAndSurname}/{date}")
    // public String chooseTime(@PathVariable Long id, @RequestParam String date,
    // Model model, @PathVariable String nameAndSurname) {
    // model.addAttribute("doctor", doctorRepo.findById(id).orElse(null));
    // model.addAttribute("date", date);
    // return "specialist_time";
    // }

    // @GetMapping("/doctor/{id}/{nameAndSurname}/{date}/{time}")
    // public String chooseType(@PathVariable Long id, @RequestParam String date,
    // @RequestParam String time,
    // Model model, @PathVariable String nameAndSurname) {
    // model.addAttribute("doctor", doctorRepo.findById(id).orElse(null));
    // model.addAttribute("date", date);
    // model.addAttribute("time", time);
    // return "specialist_tyyype";
    // }

    @PostMapping("/doctor/{did}/{nameAndSurname}")
    public String saveDoctorToBooking(Booking booking, Model model, @PathVariable Long did) {

        model.addAttribute("doctor", doctorRepo.findById(did).orElse(null));
        model.addAttribute("day1", doctorRepo.findById(did).orElse(null).getDayOn1());
        model.addAttribute("day2", doctorRepo.findById(did).orElse(null).getDayOn2());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        booking.setPacient(user.getUsername());
        booking.setIdDoctor(did);
        booking.setStatus(Status.inProgress);
        bookingRepo.save(booking);
        return "bookingDate";
    }

    // @GetMapping("/updateBooking/{id}")
    // String updateDate(Booking booking, @PathVariable("id") Long id, @RequestParam Date date) {

    //     booking = bookingRepo.findById(id).orElse(null);
    //     booking.setDate(date);
    //     bookingRepo.save(booking);
    //     return "specialist_time";
    // }

    @PostMapping("/updateTime/{bid}")
    String updateBookin(@RequestParam Long bid, @ModelAttribute Booking booking, Model model,
            @RequestParam("date") String date) throws ParseException {
                model.addAttribute("booking", bookingRepo.findById(bid).orElse(null));
        booking = bookingRepo.findById(bid).orElse(null);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date data = formatter.parse(date);
        booking.setDate(data);
        bookingRepo.save(booking);
        model.addAttribute("doctor", doctorRepo.findById(bookingRepo.findById(bid).orElse(null).getIdDoctor()).get());
        model.addAttribute("bookingRepo", bookingRepo);
        return "bookingTime";
    }

    @PostMapping("/updateType/{bid}")
    String updateTimeOfBooking(
        @PathVariable ("bid") Long bid, 
        @ModelAttribute Booking booking, Model model,
    @RequestParam("time") LocalTime time){
       booking = bookingRepo.findById(bid).orElse(null);
       model.addAttribute("booking", bookingRepo.findById(bid).orElse(null));
       model.addAttribute("doctor", doctorRepo.findById(bookingRepo.findById(bid).orElse(null).getIdDoctor()).get());
       model.addAttribute("specialization", doctorRepo.findById(bookingRepo.findById(bid).orElse(null).getIdDoctor()).get().getSpecialization());
       booking.setTime(time.withNano(0));
        bookingRepo.save(booking);
       model.addAttribute("appointments", appointmentRepo.findAll());
        return "bookingType";
    }

    @PostMapping("/confirm/{bid}")
    String updateTypeOfBooking(@PathVariable ("bid") Long bid, Model model,
    @RequestParam String type, @ModelAttribute Booking booking){
        booking = bookingRepo.findById(bid).orElse(null);
        booking.setType(type);
        bookingRepo.save(booking);
        model.addAttribute("doctor", doctorRepo.findById(bookingRepo.findById(bid).orElse(null).getIdDoctor()).get());
        model.addAttribute("booking", bookingRepo.findById(bid).orElse(null));
        return "bookingFinal";
    }

    @PostMapping("/sucess/{bid}")
    String confirmBooking(@PathVariable ("bid") Long bid, @ModelAttribute Booking booking){
        booking = bookingRepo.findById(bid).orElse(null);
        booking.setStatus(Status.saved);
        bookingRepo.save(booking);
        return "bookingSucess";
    }


    // @PostMapping("/doctor/{id}/{nameAndSurname}/{date}")
    // public String createBooking(@ModelAttribute("booking") Booking booking, Model
    // model,
    // @RequestParam Long id, @RequestParam String date) {
    // bookingRepo.save(booking);
    // model.addAttribute("doctor", doctorRepo.findById(id).orElse(null));
    // model.addAttribute("date", date);
    // return "specialist_time";
    // }

    // @PostMapping ("/bookingTime")
    // public String chooseTime(@ModelAttribute("booking") Booking booking,
    // Model model, User user, Doctor doctor){
    // String pacient=user.getUsername();
    // Long idDoctor=doctor.getId();
    // bookingRepo.save(booking);
    // return "specialist_time";
    // }

    // @GetMapping("/doctor/{id}/{nameAndSurname}/{date}")
    // public String saveDateAndChooseTime(Model model, @RequestParam String date,
    // @PathVariable Long id,
    // @RequestParam Long did){
    // model.addAttribute("doctor", doctorRepo.findById(did).orElse(null));
    // model.addAttribute("date", date);
    // return "specialist_time";
    // }
}
