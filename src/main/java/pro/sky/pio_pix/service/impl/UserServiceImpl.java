package pro.sky.pio_pix.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pro.sky.pio_pix.dto.EmailDataDTO;
import pro.sky.pio_pix.dto.PhoneDataDTO;
import pro.sky.pio_pix.dto.UserDTO;
import pro.sky.pio_pix.entity.EmailDataEntity;
import pro.sky.pio_pix.entity.PhoneDataEntity;
import pro.sky.pio_pix.entity.UserEntity;
import pro.sky.pio_pix.exception.*;
import pro.sky.pio_pix.mapper.EmailDataMapper;
import pro.sky.pio_pix.mapper.PhoneDataMapper;
import pro.sky.pio_pix.mapper.UserMapper;
import pro.sky.pio_pix.repository.EmailDataRepository;
import pro.sky.pio_pix.repository.PhoneDataRepository;
import pro.sky.pio_pix.repository.UserRepository;
import pro.sky.pio_pix.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;

    /**
     * Method to read user by email
     *
     * @param userEmail user's email
     * @return user info
     */
    @Cacheable(value = "user", key = "#userEmail")
    public UserDTO findUserByEmail(String userEmail) {
        EmailDataEntity emailDataEntity = emailDataRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EmailDataNotFoundException("Email пользователя отсутствует в базе данных"));
        UserEntity userEntity = userRepository.findById(emailDataEntity.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь отсутствует в базе данных"));
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setName(userEntity.getName());
        userDTO.setDateOfBirth(userEntity.getDateOfBirth());
        return userDTO;
    }

    /**
     * Method to read user by phone
     *
     * @param userPhone user's phone number
     * @return user info
     */
    @Cacheable(value = "user", key = "#userPhone")
    public UserDTO findUserByPhone(String userPhone) {
        PhoneDataEntity phoneDataEntity = phoneDataRepository.findByPhone(userPhone)
                .orElseThrow(() -> new PhoneDataNotFoundException("Телефон пользователя отсутствует в базе данных"));
        UserEntity userEntity = userRepository.findById(phoneDataEntity.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь отсутствует в базе данных"));
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setName(userEntity.getName());
        userDTO.setDateOfBirth(userEntity.getDateOfBirth());
        return userDTO;
    }

    /**
     * Method to read list of users with names beginning according username specified (by pages)
     *
     * @param userName   name specified
     * @param pageNumber page number
     * @param pageAmount page amount
     * @return list of users
     */
    public List<UserDTO> findUserByNameWithPagination(String userName, int pageNumber, int pageAmount) {
        PageRequest userPages = PageRequest.of(pageNumber - 1, pageAmount);
        Page<UserEntity> userEntityPage = userRepository.findByNameWithPagination(userName, userPages);
        List<UserDTO> userDTOList = userEntityPage.stream()
                .map(UserMapper.INSTANCE::toDto)
                .toList();
        return userDTOList;
    }

    /**
     * Method to read list of users with birthdays later than date specified (by pages)
     *
     * @param dateOfBirth date specified
     * @param pageNumber  page number
     * @param pageAmount  page amount
     * @return list of users
     */
    public List<UserDTO> findUserByDateOfBirthWithPagination(LocalDate dateOfBirth, int pageNumber, int pageAmount) {
        PageRequest userPages = PageRequest.of(pageNumber - 1, pageAmount);
        Page<UserEntity> userEntityPage = userRepository
                .findByDateOfBirthWithPagination(dateOfBirth, userPages);
        List<UserDTO> userDTOList = userEntityPage.stream()
                .map(UserMapper.INSTANCE::toDto)
                .toList();
        return userDTOList;
    }

    /**
     * Method to add user additional email
     *
     * @param userEmail      additional user's email
     * @param authentication applicant
     * @return user's email added data
     */
    @CachePut(value = "email", key = "#userEmail")
    public EmailDataDTO addNewUserEmail(String userEmail, Authentication authentication) {
        UserEntity applicantEntity = userRepository.findByName(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь отсутствует в базе данных"));
        EmailDataEntity emailDataEntity = new EmailDataEntity();
        if (emailDataRepository.findEmailData(userEmail).isPresent()) {
            if (applicantEntity.getId() == emailDataRepository.findEmailData(userEmail).get().getUser().getId()) {
                throw new EmailDataInvalidException("Email данного пользователя присутствует в базе данных");
            } else {
                throw new EmailDataInvalidException("Email присутствует в базе данных и принадлежит другому пользователю");
            }
        } else {
            emailDataEntity.setId(null);                       // emailDataEntity: будет сгенерирован новый id
            emailDataEntity.setUser(applicantEntity);
            emailDataEntity.setEmail(userEmail);
            emailDataRepository.save(emailDataEntity);
        }
        EmailDataDTO emailDataDTO = EmailDataMapper.INSTANCE.toDTO(emailDataEntity);
        emailDataDTO.userIdToDto(emailDataDTO, applicantEntity.getId());  // Указываем user_id
        return emailDataDTO;
    }

    /**
     * Method to add user additional phone number
     *
     * @param userPhone      additional user's phone
     * @param authentication applicant
     * @return user's phone number added data
     */
    @CachePut(value = "phone", key = "#userPhone")
    public PhoneDataDTO addNewUserPhone(String userPhone, Authentication authentication) {
        UserEntity applicantEntity = userRepository.findByName(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь отсутствует в базе данных"));
        PhoneDataEntity phoneDataEntity = new PhoneDataEntity();
        if (phoneDataRepository.findPhoneData(userPhone).isPresent()) {
            if (applicantEntity.getId() == phoneDataRepository.findPhoneData(userPhone).get().getUser().getId()) {
                throw new PhoneDataInvalidException("Номер телефона данного пользователя присутствует в базе данных");
            } else {
                throw new PhoneDataInvalidException("Номер телефона присутствует в базе данных и принадлежит другому пользователю");
            }
        } else {
            phoneDataEntity.setId(null);                       // phoneDataEntity: будет сгенерирован новый id
            phoneDataEntity.setUser(applicantEntity);
            phoneDataEntity.setPhone(userPhone);
            phoneDataRepository.save(phoneDataEntity);
        }
        PhoneDataDTO phoneDataDTO = PhoneDataMapper.INSTANCE.toDTO(phoneDataEntity);
        phoneDataDTO.userIdToDto(phoneDataDTO, applicantEntity.getId());  // Указываем user_id
        return phoneDataDTO;
    }

    /**
     * Method to update user additional email
     *
     * @param emailDataDTO   new user email data
     * @param userEmail      updated user's email
     * @param authentication applicant
     * @return user's email updated data
     */
    @CachePut(value = "email", key = "#userEmail")
    public EmailDataDTO updateUserEmail(EmailDataDTO emailDataDTO, String userEmail, Authentication authentication) {
        UserEntity applicantEntity = userRepository.findByName(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь отсутствует в базе данных"));
        EmailDataEntity updatedEmailDataEntity = emailDataRepository.findEmailData(userEmail)
                .orElseThrow(() -> new EmailDataNotFoundException("Email пользователя отсутствует в базе данных"));
        if (applicantEntity.getId() != updatedEmailDataEntity.getUser().getId()) {
            throw new EmailDataInvalidException("Email принадлежит другому пользователю");
        }
        if (emailDataRepository.findEmailData(emailDataDTO.getEmail()).isPresent()) {
            throw new EmailDataInvalidException("Новый email пользователя уже присутствует в базе данных");
        }
        if (applicantEntity.getId() != emailDataDTO.getUserId()) {
            throw new EmailDataInvalidException("Пользователь может изменить только собственный email");
        } else {
            EmailDataEntity newEmailDataEntity = new EmailDataEntity();
            newEmailDataEntity.setId(updatedEmailDataEntity.getId());
            newEmailDataEntity.setUser(applicantEntity);
            newEmailDataEntity.setEmail(emailDataDTO.getEmail());
            emailDataRepository.save(newEmailDataEntity);
            EmailDataDTO newEmailDataDTO = EmailDataMapper.INSTANCE.toDTO(newEmailDataEntity);
            newEmailDataDTO.userIdToDto(newEmailDataDTO, applicantEntity.getId());  // Указываем user_id
            return newEmailDataDTO;
        }
    }

    /**
     * Method to update user phone number
     *
     * @param phoneDataDTO   new user phone data
     * @param userPhone      updated user's phone number
     * @param authentication applicant
     * @return user's phone updated data
     */
    @CachePut(value = "phone", key = "#userPhone")
    public PhoneDataDTO updateUserPhone(PhoneDataDTO phoneDataDTO, String userPhone, Authentication authentication) {
        UserEntity applicantEntity = userRepository.findByName(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь отсутствует в базе данных"));
        PhoneDataEntity updatedPhoneDataEntity = phoneDataRepository.findPhoneData(userPhone)
                .orElseThrow(() -> new PhoneDataNotFoundException("Телефон пользователя отсутствует в базе данных"));
        if (applicantEntity.getId() != updatedPhoneDataEntity.getUser().getId()) {
            throw new PhoneDataInvalidException("Номер телефона принадлежит другому пользователю");
        }
        if (phoneDataRepository.findPhoneData(phoneDataDTO.getPhone()).isPresent()) {
            throw new PhoneDataInvalidException("Новый номер телефона пользователя уже присутствует в базе данных");
        }
        if (applicantEntity.getId() != phoneDataDTO.getUserId()) {
            throw new PhoneDataInvalidException("Пользователь может изменить номер только собственного телефона");
        } else {
            PhoneDataEntity newPhoneDataEntity = new PhoneDataEntity();
            newPhoneDataEntity.setId(updatedPhoneDataEntity.getId());
            newPhoneDataEntity.setUser(applicantEntity);
            newPhoneDataEntity.setPhone(phoneDataDTO.getPhone());
            phoneDataRepository.save(newPhoneDataEntity);
            PhoneDataDTO newPhoneDataDTO = PhoneDataMapper.INSTANCE.toDTO(newPhoneDataEntity);
            newPhoneDataDTO.userIdToDto(newPhoneDataDTO, applicantEntity.getId());  // Указываем user_id
            return newPhoneDataDTO;
        }
    }

    /**
     * Method to delete user's email
     *
     * @param userEmail      user's email to delete
     * @param authentication applicant
     * @return user's email deleted data
     */
    @CacheEvict(value = "email", key = "#userEmail")
    public EmailDataDTO deleteUserEmail(String userEmail, Authentication authentication) {
        UserEntity applicantEntity = userRepository.findByName(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь отсутствует в базе данных"));
        EmailDataEntity emailDataEntity = emailDataRepository.findEmailData(userEmail)
                .orElseThrow(() -> new EmailDataNotFoundException("Email пользователя отсутствует в базе данных"));
        if (applicantEntity.getId() == emailDataEntity.getUser().getId()) {
            EmailDataDTO deletedEmailDataDTO = EmailDataMapper.INSTANCE.toDTO(emailDataEntity);
            deletedEmailDataDTO.userIdToDto(deletedEmailDataDTO, applicantEntity.getId());  // Указываем user_id
            emailDataRepository.delete(emailDataEntity);
            return deletedEmailDataDTO;
        } else {
            throw new EmailDataInvalidException("Email принадлежит другому пользователю");
        }
    }

    /**
     * Method to delete user's phone number
     *
     * @param userPhone      user's phone number to delete
     * @param authentication applicant
     * @return user's phone deleted data
     */
    @CacheEvict(value = "phone", key = "#userPhone")
    public PhoneDataDTO deleteUserPhone(String userPhone, Authentication authentication) {
        UserEntity applicantEntity = userRepository.findByName(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь отсутствует в базе данных"));
        PhoneDataEntity phoneDataEntity = phoneDataRepository.findPhoneData(userPhone)
                .orElseThrow(() -> new EmailDataNotFoundException("Номер телефона пользователя отсутствует в базе данных"));
        if (applicantEntity.getId() == phoneDataEntity.getUser().getId()) {
            PhoneDataDTO deletedPhoneDataDTO = PhoneDataMapper.INSTANCE.toDTO(phoneDataEntity);
            deletedPhoneDataDTO.userIdToDto(deletedPhoneDataDTO, applicantEntity.getId());  // Указываем user_id
            phoneDataRepository.delete(phoneDataEntity);
            return deletedPhoneDataDTO;
        } else {
            throw new EmailDataInvalidException("Номер телефона принадлежит другому пользователю");
        }
    }


}
