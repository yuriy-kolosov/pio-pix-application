package pro.sky.pio_pix.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransferDataDTO {

    @NotNull
    private Long id;

    @NotNull
    private Long userFromId;

    @NotNull
    private Long userToId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDateTime localDateTime;

    public void userFromIdToDto(TransferDataDTO transferDataDTO, Long userId) {
        transferDataDTO.userFromId = userId;
    }

    public void userToIdToDto(TransferDataDTO transferDataDTO, Long userId) {
        transferDataDTO.userToId = userId;
    }

}
