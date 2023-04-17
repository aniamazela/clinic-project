package pl.mazela.project.repositories;

import java.time.LocalTime;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mazela.project.models.Booking;

@Repository
public interface BookingRepository extends JpaRepository <Booking, Long>{
    
    Boolean existsByIdDoctorAndDateAndTime(Long idDoctor, Date date, LocalTime time);
}
