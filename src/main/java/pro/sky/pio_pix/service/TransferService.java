package pro.sky.pio_pix.service;

import org.springframework.security.core.Authentication;
import pro.sky.pio_pix.dto.TransferDataDTO;
import pro.sky.pio_pix.dto.UserDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransferService {

    List<TransferDataDTO> findTransferWithPagination(int pageNumber, int pageAmount);

    TransferDataDTO transfer(Long userToId, BigDecimal sum, Authentication authentication);

}
