package pro.sky.pio_pix.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.executable.ValidateOnExecution;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pro.sky.pio_pix.dto.EmailDataDTO;
import pro.sky.pio_pix.dto.PhoneDataDTO;
import pro.sky.pio_pix.dto.UserDTO;
import pro.sky.pio_pix.service.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Operation(summary = "Получить информацию о пользователе по email: точное совпадение",
            tags = "Пользователи",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDTO.class)
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
    @GetMapping("/email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam("userEmail")
                                                  @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                                                          message = "Email пользователя: неверный формат "
                                                                  + "| см. пример: name@corp.domain") String userEmail) {
        logger.debug("\"GET\" getUserByEmail method was invoke...");
        UserDTO userDTO = userService.findUserByEmail(userEmail);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @Operation(summary = "Получить информацию о пользователе по номеру телефона: точное совпадение",
            tags = "Пользователи",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDTO.class)
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
    @GetMapping("/phone")
    public ResponseEntity<UserDTO> getUserByPhone(@RequestParam("userPhone")
                                                  @Pattern(regexp = "^79\\d{9}$"
                                                          , message = "Мобильный телефон пользователя: неверный формат "
                                                          + "| см. пример: 79012345678")
                                                  String userPhone) {
        logger.debug("\"GET\" getUserByPhone method was invoke...");
        UserDTO userDTO = userService.findUserByPhone(userPhone);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @Operation(summary = "Получить информацию о пользователе по имени: начальные символы (постраничная выборка)",
            tags = "Пользователи",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDTO.class)
                            )),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
            })
    @GetMapping("/name/by-pages")
    public ResponseEntity<List<UserDTO>> getUserByName(@RequestParam String userName
            , @RequestParam int pageNumber
            , @RequestParam int pageAmount) {
        logger.debug("\"GET\" getUserByName method was invoke...");
        List<UserDTO> userDTOList = userService.findUserByNameWithPagination(userName, pageNumber, pageAmount);
        return ResponseEntity.status(HttpStatus.OK).body(userDTOList);
    }

    @Operation(summary = "Получить информацию о пользователе по дате рождения: позже указанной даты (постраничная выборка)",
            tags = "Пользователи",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDTO.class)
                            )),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
            })
    @GetMapping("/date-of-birth/by-pages")
    public ResponseEntity<List<UserDTO>> getUserByDateOfBirth(@RequestParam("dateOfBirth")
                                                              @DateTimeFormat(pattern = "dd.MM.yyyy")
                                                              LocalDate dateOfBirth
            , @RequestParam int pageNumber
            , @RequestParam int pageAmount) {
        logger.debug("\"GET\" getUserByDateOfBirth method was invoke...");
        List<UserDTO> userDTOList = userService.findUserByDateOfBirthWithPagination(dateOfBirth, pageNumber, pageAmount);
        return ResponseEntity.status(HttpStatus.OK).body(userDTOList);
    }

    @Operation(summary = "Добавить новый email: доступно текущему пользователю",
            tags = "Пользователи",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = EmailDataDTO.class)
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
    @PostMapping("/email")
    public ResponseEntity<EmailDataDTO> addNewEmail(@RequestParam("userEmail")
                                                    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                                                            message = "Email пользователя: неверный формат "
                                                                    + "| см. пример: name@corp.domain") String userEmail
            , Authentication authentication) {
        logger.debug("\"POST\" addNewEmail method was invoke...");
        EmailDataDTO emailDataDTO = userService.addNewUserEmail(userEmail, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(emailDataDTO);
    }

    @Operation(summary = "Добавить новый номер телефона: доступно текущему пользователю",
            tags = "Пользователи",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PhoneDataDTO.class)
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
    @PostMapping("/phone")
    public ResponseEntity<PhoneDataDTO> addNewPhone(@RequestParam("userPhone")
                                                    @Pattern(regexp = "^79\\d{9}$"
                                                            , message = "Мобильный телефон пользователя: неверный формат "
                                                            + "| см. пример: 79012345678")
                                                    String userPhone
            , Authentication authentication) {
        logger.debug("\"POST\" addNewEmail method was invoke...");
        PhoneDataDTO phoneDataDTO = userService.addNewUserPhone(userPhone, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(phoneDataDTO);
    }

    @Operation(summary = "Изменить email: доступно текущему пользователю",
            tags = "Пользователи",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = EmailDataDTO.class)
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
    @PutMapping("/email")
    public ResponseEntity<EmailDataDTO> updateEmail(@Valid @RequestBody EmailDataDTO emailDataDTO,
                                                    @RequestParam("userEmail")
                                                    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                                                            message = "Email пользователя: неверный формат | см. пример: name@corp.domain")
                                                    String userEmail
            , Authentication authentication) {
        logger.debug("\"PUT\" updateEmail method was invoke...");
        EmailDataDTO updatedEmailDataDTO = userService.updateUserEmail(emailDataDTO, userEmail, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(updatedEmailDataDTO);
    }

    @Operation(summary = "Изменить номер телефона: доступно текущему пользователю",
            tags = "Пользователи",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PhoneDataDTO.class)
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
    @PutMapping("/phone")
    public ResponseEntity<PhoneDataDTO> updatePhone(@Valid @RequestBody PhoneDataDTO phoneDataDTO,
                                                    @RequestParam("userPhone")
                                                    @Pattern(regexp = "^79\\d{9}$"
                                                            , message = "Мобильный телефон пользователя: неверный формат "
                                                            + "| см. пример: 79012345678")
                                                    String userPhone
            , Authentication authentication) {
        logger.debug("\"PUT\" updatePhoneEmail method was invoke...");
        PhoneDataDTO updatedPhoneDataDTO = userService.updateUserPhone(phoneDataDTO, userPhone, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPhoneDataDTO);
    }

    @Operation(summary = "Удалить email: доступно текущему пользователю",
            tags = "Пользователи",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = EmailDataDTO.class)
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
    @DeleteMapping("/email")
    public ResponseEntity<EmailDataDTO> deleteEmail(@RequestParam("userEmail")
                                                    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                                                            message = "Email пользователя: неверный формат "
                                                                    + "| см. пример: name@corp.domain")
                                                    String userEmail
            , Authentication authentication) {
        logger.debug("\"DELETE\" deleteEmail method was invoke...");
        EmailDataDTO emailDataDTO = userService.deleteUserEmail(userEmail, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(emailDataDTO);
    }

    @Operation(summary = "Удалить номер телефона: доступно текущему пользователю",
            tags = "Пользователи",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PhoneDataDTO.class)
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

    @DeleteMapping("/phone")
    public ResponseEntity<PhoneDataDTO> deletePhone(@RequestParam("userPhone")
                                                    @Pattern(regexp = "^79\\d{9}$"
                                                            , message = "Мобильный телефон пользователя: неверный формат "
                                                            + "| см. пример: 79012345678")
                                                    String userPhone
            , Authentication authentication) {
        logger.debug("\"DELETE\" deletePhone method was invoke...");
        PhoneDataDTO phoneDataDTO = userService.deleteUserPhone(userPhone, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(phoneDataDTO);
    }


}
