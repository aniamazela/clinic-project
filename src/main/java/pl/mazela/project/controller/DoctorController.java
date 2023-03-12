package pl.mazela.project.controller;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.mazela.project.models.Specialization;
import pl.mazela.project.repositories.DoctorRepository;

@Controller

public class DoctorController {
    private final DoctorRepository doctorRepo;
    final int size=4;

    public DoctorController(DoctorRepository doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    @GetMapping(params={"!sort", "!page", "!size", "!specializaton"}, path = "/doctors")
    public String getAllDoctors(Model model) {
        int numberOfDoctors= doctorRepo.findAll().size();
        int maxPage=(int) Math.ceil(numberOfDoctors/4);
        model.addAttribute("showAll", "only4");
        model.addAttribute("noPage", "noPage");
        model.addAttribute("maxPage", maxPage);
        model.addAttribute("doctors", doctorRepo.findAll());
        return "doctors";
    }

    @GetMapping("/doctors")   //?page=0&size=5
    public String getDoctorsPage(Model model, Pageable pageable, @RequestParam int page) {
        int numberOfDoctors= doctorRepo.findAll().size();
        int maxPage=(int) Math.ceil(numberOfDoctors/4)+1;
        model.addAttribute("showAll", "ok");
        model.addAttribute("noPage", "pageOk");
        model.addAttribute("maxPage", maxPage);
        pageable = PageRequest.of(page-1,size);
        model.addAttribute("doctors", doctorRepo.findAll(pageable));
        return "doctors";
    }

    @GetMapping(params = "specialization", path = "/doctors/specialization")
    public String showDoctorsBySpecialization(Model model, @RequestParam ("specialization") Specialization specialization){
        model.addAttribute("showAll", "ok");
        model.addAttribute("noPage", "noPage");
        model.addAttribute("doctors", doctorRepo.findBySpecializationIs(specialization));
        return "doctors";
    }
}
