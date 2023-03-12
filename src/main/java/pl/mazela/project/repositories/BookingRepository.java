package pl.mazela.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mazela.project.models.Booking;

@Repository
public interface BookingRepository extends JpaRepository <Booking, Long>{
    
}
