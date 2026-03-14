package pro.sky.pio_pix.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDTO {

    @NotNull
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    @Min(value = 0L)
    private BigDecimal balance;

    protected void userIdToDto(AccountDTO accountDTO, Long userId) {
        accountDTO.userId = userId;
    }

    public AccountDTO() {
    }

    public AccountDTO(Long id, Long userId, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }

}
