package pro.sky.pio_pix.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

@Data
public class EmailDataDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    @Email
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
            , message = "Email пользователя | формат: name@corp.domain")
    private String email;

    public void userIdToDto(EmailDataDTO emailDataDTO, Long userId) {
        emailDataDTO.userId = userId;
    }

    public EmailDataDTO() {
    }

    public EmailDataDTO(Long id, Long userId, String email) {
        this.id = id;
        this.userId = userId;
        this.email = email;
    }

}
