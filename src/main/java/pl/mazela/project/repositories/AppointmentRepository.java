package pl.mazela.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mazela.project.models.Appointment;

import java.util.List;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
    List<Appointment> findAll();

    Object findByType(String type);
}
