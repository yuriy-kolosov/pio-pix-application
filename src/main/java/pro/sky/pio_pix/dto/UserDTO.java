package pro.sky.pio_pix.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UserDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    @Length(min = 3, max = 500)
    private String name;

    @NotNull
//    @Pattern(regexp = "^((0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012]).(?:19|20)[0-9][0-9])$"
//            , message = "Дата рождения пользователя в формате: dd.mm.yyyy ")
    private LocalDate dateOfBirth;

    public UserDTO() {
    }

    public UserDTO(Long id, String name, LocalDate dateOfBirth) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }
}
