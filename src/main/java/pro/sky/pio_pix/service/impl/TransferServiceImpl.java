package pro.sky.pio_pix.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pro.sky.pio_pix.dto.TransferDataDTO;
import pro.sky.pio_pix.entity.AccountEntity;
import pro.sky.pio_pix.entity.TransferDataEntity;
import pro.sky.pio_pix.entity.UserEntity;
import pro.sky.pio_pix.exception.AccountInvalidException;
import pro.sky.pio_pix.exception.AccountNotFoundException;
import pro.sky.pio_pix.exception.UserInvalidException;
import pro.sky.pio_pix.exception.UserNotFoundException;
import pro.sky.pio_pix.mapper.TransferDataMapper;
import pro.sky.pio_pix.repository.AccountRepository;
import pro.sky.pio_pix.repository.TransferDataRepository;
import pro.sky.pio_pix.repository.UserRepository;
import pro.sky.pio_pix.service.TransferService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final TransferDataRepository transferDataRepository;
    private final UserRepository userRepository;

    /**
     * Method to read list of transfers (by pages)
     *
     * @param pageNumber pageNumber
     * @param pageAmount pageAmount
     * @return list of transfers
     */
    @Override
    public List<TransferDataDTO> findTransferWithPagination(int pageNumber, int pageAmount) {
        PageRequest transferDataPages = PageRequest.of(pageNumber - 1, pageAmount);
        Page<TransferDataEntity> transferDataEntityPage = transferDataRepository
                .findWithPagination(transferDataPages);
        List<TransferDataDTO> transferDataDTOList = transferDataEntityPage.stream()
                .map(TransferDataMapper.INSTANCE::toDto)
                .toList();
        for (int i = 0; i < transferDataDTOList.size(); i++) {
            transferDataDTOList.get(i).userFromIdToDto(transferDataDTOList.get(i)
                    , transferDataEntityPage.getContent().get(i).getUserFrom().getId());
            transferDataDTOList.get(i).userToIdToDto(transferDataDTOList.get(i)
                    , transferDataEntityPage.getContent().get(i).getUserTo().getId());
        }
        return transferDataDTOList;
    }

    /**
     * Method to transfer from applicant account to recipient account
     *
     * @param userToId recipient id
     * @param amount  amount to transfer
     * @param authentication applicant
     * @return transfer data
     */
    @Override
    public TransferDataDTO transfer(Long userToId, BigDecimal amount, Authentication authentication) {

        BigDecimal MAX_BALANCE = BigDecimal.valueOf(9_000_000_000_000L);

        UserEntity applicantEntity = userRepository.findByName(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Отправитель отсутствует в базе данных"));

        if (applicantEntity.getId().equals(userToId)) {
            throw new UserInvalidException("Отправитель не может быть получателем");
        }

        UserEntity recipientEntity = userRepository.findById(userToId)
                .orElseThrow(() -> new UserNotFoundException("Получатель отсутствует в базе данных"));

        AccountEntity applicantAccountEntity = accountRepository.findById(applicantEntity.getId())
                .orElseThrow(() -> new AccountNotFoundException("Счет отправителя не найден в базе данных"));
        AccountEntity recipientAccountEntity = accountRepository.findById(recipientEntity.getId())
                .orElseThrow(() -> new AccountNotFoundException("Счет получателя не найден в базе данных"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AccountInvalidException("Ошибка в сумме перевода");
        }

        BigDecimal applicantBalance = applicantAccountEntity.getBalance();
        BigDecimal recipientBalance = recipientAccountEntity.getBalance();

        BigDecimal applicantNewBalance = applicantBalance.subtract(amount);
        BigDecimal recipientNewBalance = recipientBalance.add(amount);

        if (applicantNewBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new AccountInvalidException("Недостаточно средств на счете отправителя");
        }
        if (recipientNewBalance.compareTo(MAX_BALANCE) >= 0) {
            throw new AccountInvalidException("Ошибка в сумме перевода");
        }

        LocalDateTime transactionLocalDateTime;

        synchronized (this) {
            applicantAccountEntity.setBalance(applicantNewBalance);
            recipientAccountEntity.setBalance(recipientNewBalance);
            transactionLocalDateTime = LocalDateTime.now();
        }

        TransferDataEntity transferDataEntity = new TransferDataEntity();
        transferDataEntity.setUserFrom(applicantEntity);
        transferDataEntity.setUserTo(recipientEntity);
        transferDataEntity.setAmount(amount);
        transferDataEntity.setLocalDateTime(transactionLocalDateTime);
        transferDataRepository.save(transferDataEntity);

        TransferDataDTO transferDataDTO = TransferDataMapper.INSTANCE.toDto(transferDataEntity);
        transferDataDTO.userFromIdToDto(transferDataDTO, applicantEntity.getId());  // Указываем user_id
        transferDataDTO.userToIdToDto(transferDataDTO, recipientEntity.getId());  // Указываем user_id
        return transferDataDTO;
    }

}
