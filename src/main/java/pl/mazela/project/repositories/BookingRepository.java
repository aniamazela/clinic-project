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
    @Query("Select b from Booking b WHERE b.date <= CURRENT_DATE and b.pacient=:pacient and b.status='saved'")
    List <Booking> findByDateisBeforeTodayForPacient(String pacient, Sort sort);
    @Query("Select b from Booking b WHERE b.date >= CURRENT_DATE and b.pacient=:pacient and b.status='saved'")
    List <Booking> findByDateisAfterTodayForPacient(String pacient, Sort sort);
    @Query("Select b from Booking b WHERE b.status=:status and b.pacient=:pacient")
    List <Booking> findByStatusForPacient(Status status, String pacient);

}
