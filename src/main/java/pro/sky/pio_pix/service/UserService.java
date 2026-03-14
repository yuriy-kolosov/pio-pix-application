package pro.sky.pio_pix.service;

import org.springframework.security.core.Authentication;
import pro.sky.pio_pix.dto.EmailDataDTO;
import pro.sky.pio_pix.dto.PhoneDataDTO;
import pro.sky.pio_pix.dto.UserDTO;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    List<UserDTO> findUserByNameWithPagination(String userName, int pageNumber, int pageAmount);

    UserDTO findUserByEmail(String userEmail);

    UserDTO findUserByPhone(String userEmail);

    List<UserDTO> findUserByDateOfBirthWithPagination(LocalDate dateOfBirth, int pageNumber, int pageAmount);

    EmailDataDTO addNewUserEmail(String userEmail, Authentication authentication);

    PhoneDataDTO addNewUserPhone(String userPhone, Authentication authentication);

    EmailDataDTO updateUserEmail(EmailDataDTO emailDataDTO, String userEmail, Authentication authentication);

    PhoneDataDTO updateUserPhone(PhoneDataDTO phoneDataDTO, String userEmail, Authentication authentication);

    EmailDataDTO deleteUserEmail(String userEmail, Authentication authentication);

    PhoneDataDTO deleteUserPhone(String userPhone, Authentication authentication);

}
