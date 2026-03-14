package pro.sky.pio_pix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.pio_pix.entity.AccountEntity;

public interface AccountRepository extends JpaRepository <AccountEntity, Long> {

}
