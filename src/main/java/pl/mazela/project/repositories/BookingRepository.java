package pl.mazela.project.repositories;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mazela.project.models.Booking;
import pl.mazela.project.models.Doctor;

@Repository
public interface BookingRepository extends JpaRepository <Booking, Long>{
    
    Boolean existsByIdDoctorAndDateAndTime(Long idDoctor, Date date, LocalTime time);
    List <Booking> findAllByPacient(String pacient);
    Optional<Doctor> findDoctorById(Long idDoctor);

}
