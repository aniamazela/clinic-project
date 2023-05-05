package pl.mazela.project.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.mazela.project.models.Booking;
import pl.mazela.project.models.Status;
import pl.mazela.project.models.User;
import pl.mazela.project.repositories.AppointmentRepository;
import pl.mazela.project.repositories.BookingRepository;
import pl.mazela.project.repositories.DoctorRepository;

@Controller
public class BookingController {

    BookingController(BookingRepository bookingRepo, DoctorRepository doctorRepo,
            AppointmentRepository appointmentRepo) {
        this.bookingRepo = bookingRepo;
        this.doctorRepo = doctorRepo;
        this.appointmentRepo = appointmentRepo;
    }

    BookingRepository bookingRepo;
    DoctorRepository doctorRepo;
    AppointmentRepository appointmentRepo;

    public String findPacient() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String pacient = user.getUsername();
        return pacient;
    }

    @PostMapping("/doctor/{did}/{nameAndSurname}")
    public String saveDoctorToBooking(Booking booking, Model model, @PathVariable Long did) {
        model.addAttribute("doctor", doctorRepo.findById(did).orElse(null));
        model.addAttribute("day1", doctorRepo.findById(did).orElse(null).getDayOn1());
        model.addAttribute("day2", doctorRepo.findById(did).orElse(null).getDayOn2());
        model.addAttribute("day3", null);
        model.addAttribute("message", " ");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        booking.setPacient(user.getUsername());
        booking.setIdDoctor(did);
        booking.setStatus(Status.inProgress);
        bookingRepo.save(booking);
        return "bookingDate";
    }

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
        Long did = bookingRepo.findById(bid).orElse(null).getIdDoctor();

        if (bookingRepo.findByDoctorAndDate(did, data).size() == 14) {
            model.addAttribute("day1", doctorRepo.findById(did).orElse(null).getDayOn1());
            model.addAttribute("day2", doctorRepo.findById(did).orElse(null).getDayOn2());
            model.addAttribute("day3", date);
            model.addAttribute("message", "Brak wolnych terminów na wybrany dzień.");
            return "bookingDate";

        } else {
            model.addAttribute("bookingRepo", bookingRepo);
            return "bookingTime";
        }
    }

    @PostMapping("/updateType/{bid}")
    String updateTimeOfBooking(
            @PathVariable("bid") Long bid,
            @ModelAttribute Booking booking, Model model,
            @RequestParam("time") LocalTime time) {
        booking = bookingRepo.findById(bid).orElse(null);
        model.addAttribute("booking", bookingRepo.findById(bid).orElse(null));
        model.addAttribute("doctor", doctorRepo.findById(bookingRepo.findById(bid).orElse(null).getIdDoctor()).get());
        model.addAttribute("specialization",
                doctorRepo.findById(bookingRepo.findById(bid).orElse(null).getIdDoctor()).get().getSpecialization());
        booking.setTime(time);
        bookingRepo.save(booking);
        model.addAttribute("appointments", appointmentRepo.findAll());
        return "bookingType";
    }

    @PostMapping("/confirm/{bid}")
    String updateTypeOfBooking(@PathVariable("bid") Long bid, Model model,
            @RequestParam String type, @ModelAttribute Booking booking) {
        booking = bookingRepo.findById(bid).orElse(null);
        booking.setType(type);
        bookingRepo.save(booking);
        model.addAttribute("doctor", doctorRepo.findById(bookingRepo.findById(bid).orElse(null).getIdDoctor()).get());
        model.addAttribute("booking", bookingRepo.findById(bid).orElse(null));
        return "bookingFinal";
    }

    @PostMapping("/sucess/{bid}")
    String confirmBooking(@PathVariable("bid") Long bid, @ModelAttribute Booking booking) {
        booking = bookingRepo.findById(bid).orElse(null);
        booking.setStatus(Status.saved);
        bookingRepo.save(booking);
        return "bookingSucess";
    }

    @GetMapping("showBookingAfterNow")
    public String showMyBookingsAfterToday(Model model) {
        model.addAttribute("bookings",
                bookingRepo.findByDateisAfterNowForPacient(findPacient(),
                        Sort.by("date").ascending().and(Sort.by("time"))));
        model.addAttribute("doctorRepo", doctorRepo);
        model.addAttribute("KindfOfBooking", "Oczekujące wizyty");
        model.addAttribute("buttonName", "DEL&INFO");
        return "myBooking";
    }

    @GetMapping("showBookingBeforeNow")
    public String showMyBookingsBeforeToday(Model model) {
        model.addAttribute("bookings",
                bookingRepo.findByDateisBeforeNowForPacient(findPacient(),
                        Sort.by("date").ascending().and(Sort.by("time"))));
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
                bookingRepo.findByDateisAfterNowForPacient(findPacient(),
                        Sort.by("date").ascending().and(Sort.by("time"))));
        model.addAttribute("doctorRepo", doctorRepo);
        model.addAttribute("KindfOfBooking", "Oczekujące wizyty");
        model.addAttribute("buttonName", "DEL&INFO");
        return "myBooking";
    }

    @GetMapping("bookingInfo")
    public String showInfo(@RequestParam Long bid, Model model) {
        model.addAttribute("booking", bookingRepo.findById(bid).orElse(null));
        model.addAttribute("doctor", doctorRepo.findById(bookingRepo.findById(bid).orElse(null).getIdDoctor()).get());
        Date date = bookingRepo.findById(bid).orElse(null).getDate();
        LocalTime time = bookingRepo.findById(bid).orElse(null).getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Calendar calendarBooking = new GregorianCalendar(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                time.getHour(),
                time.getMinute(),
                time.getSecond());

        if (bookingRepo.findById(bid).orElse(null).getStatus().equals(Status.deleted)) {
            model.addAttribute("status", "odwołana");
        } else if (calendarBooking.getTime().after(new Date())) {
            model.addAttribute("status", "oczekująca");
        } else {
            model.addAttribute("status", "zakończona");
        }

        model.addAttribute("appointment",
                (appointmentRepo.findByType(bookingRepo.findById(bid).orElse(null).getType())));
        return "bookingInfo";

    }

}
