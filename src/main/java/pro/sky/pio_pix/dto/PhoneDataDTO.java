package pro.sky.pio_pix.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

@Data
public class PhoneDataDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    @Pattern(regexp = "^79\\d{9}$"
            , message = "Мобильный телефон пользователя | формат: 79012345678")
    private String phone;

    public void userIdToDto(PhoneDataDTO phoneDataDTO, Long userId) {
        phoneDataDTO.userId = userId;
    }

    public PhoneDataDTO() {
    }

    public PhoneDataDTO(Long id, Long userId, String phone) {
        this.id = id;
        this.userId = userId;
        this.phone = phone;
    }

}
