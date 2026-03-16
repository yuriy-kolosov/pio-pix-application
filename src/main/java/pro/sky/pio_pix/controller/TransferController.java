package pro.sky.pio_pix.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pro.sky.pio_pix.dto.TransferDataDTO;
import pro.sky.pio_pix.service.TransferService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Operation(summary = "Получить информацию о выполненных переводах средств",
            tags = "Трансфер",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TransferDataDTO.class)
                            )),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found"
                    )
            })
    @GetMapping()
    public ResponseEntity<List<TransferDataDTO>> getTransfer(@RequestParam int pageNumber
            , @RequestParam int pageAmount) {
        logger.debug("\"GET\" getTransfer method was invoke...");
        List<TransferDataDTO> transferDataDTOList = transferService.findTransferWithPagination(pageNumber, pageAmount);
        return ResponseEntity.status(HttpStatus.OK).body(transferDataDTOList);

    }

    @Operation(summary = "Выполнить перевод средств на счет получателя",
            tags = "Трансфер",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TransferDataDTO.class)
                            )),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict"
                    )
            })
    @PostMapping()
    public ResponseEntity<TransferDataDTO> transfer(@Valid @RequestParam Long userToId
            , @Valid @RequestParam BigDecimal amount, Authentication authentication) {
        TransferDataDTO transferDataDTO = transferService.transfer(userToId, amount, authentication);
        logger.debug("\"POST\" transfer method was invoke...");
        return ResponseEntity.status(HttpStatus.OK).body(transferDataDTO);
    }

}
