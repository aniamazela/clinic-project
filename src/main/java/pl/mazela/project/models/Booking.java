package pl.mazela.project.models;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
// @NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Table(name = "Bookings")

public class Booking implements Serializable {
  private static final long serialVersionUID = 1L;
  private LocalDate today = LocalDate.now();

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String pacient;

  // @DateTimeFormat(pattern = "dd.MM.yyyy")
  // @Column
  // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
  // @DateTimeFormat(pattern = "yyyy-MM-dd")
  // /**/private Calendar date;
  // @DateTimeFormat(pattern = "yyyy-MM-dd")
  // private Calendar date;

  private Long idDoctor;
  
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date date;

  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  private LocalTime time;

  private String type;

  

}
