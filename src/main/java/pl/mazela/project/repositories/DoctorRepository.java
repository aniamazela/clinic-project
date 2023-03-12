package pl.mazela.project.repositories;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.mazela.project.models.Doctor;
import pl.mazela.project.models.Specialization;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>{
    List<Doctor> findBySpecializationIs(Specialization specialization); 
    List<Doctor> findByName(String name);

}
