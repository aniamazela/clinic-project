package pl.mazela.project.repositories;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.mazela.project.models.Booking;
import pl.mazela.project.models.Doctor;
import pl.mazela.project.models.Status;

@Repository
public interface BookingRepository extends JpaRepository <Booking, Long>{
    
    Boolean existsByIdDoctorAndDateAndTime(Long idDoctor, Date date, LocalTime time);
    List <Booking> findAllByPacient(String pacient);
    Optional<Doctor> findDoctorById(Long idDoctor);
    @Query("Select b from Booking b WHERE concat(b.date,' ', b.time) < concat(CURRENT_DATE,' ', CURRENT_TIME) and b.pacient=:pacient and b.status='saved'")
    List <Booking> findByDateisBeforeNowForPacient(String pacient, Sort sort);
    @Query("Select b from Booking b WHERE concat(b.date,' ', b.time) >= concat(CURRENT_DATE,' ', CURRENT_TIME) and b.pacient=:pacient and b.status='saved'")
    List <Booking> findByDateisAfterNowForPacient(String pacient, Sort sort);
    @Query("Select b from Booking b WHERE b.status=:status and b.pacient=:pacient")
    List <Booking> findByStatusForPacient(Status status, String pacient, Sort sort);

}
