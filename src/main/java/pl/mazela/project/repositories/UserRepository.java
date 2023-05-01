package pl.mazela.project.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.mazela.project.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
    User findByUsername(String username);


}
