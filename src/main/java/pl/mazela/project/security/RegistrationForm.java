package pl.mazela.project.security;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;
import pl.mazela.project.models.User;

@Data
public class RegistrationForm {
    @NotEmpty(message = "Wypełnienie tego pola jest obowiązkowe!")
    private String username;

    @NotEmpty(message = "Wypełnienie tego pola jest obowiązkowe!")
    @Size(min = 8, max = 20, message = "Hasło powinno składać się z 8 do 20 znaków.")
    private String password;

    @NotEmpty(message = "Wypełnienie tego pola jest obowiązkowe!")
    private String firstName;

    @NotEmpty(message = "Wypełnienie tego pola jest obowiązkowe!")
    private String surname;

    @NotEmpty(message = "Wypełnienie tego pola jest obowiązkowe!")
    private String email;

    @NotEmpty(message = "Wypełnienie tego pola jest obowiązkowe!")
    @Size(min = 9, max = 9, message = "Numer telefonu musi składać się z dokładnie 9 cyfr.")
    private String phoneNumber;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(username, passwordEncoder.encode(password), firstName,
                surname, email, phoneNumber);
    }
}
