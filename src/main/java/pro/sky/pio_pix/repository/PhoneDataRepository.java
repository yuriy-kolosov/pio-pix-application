package pro.sky.pio_pix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.pio_pix.entity.PhoneDataEntity;

import java.util.Optional;

public interface PhoneDataRepository extends JpaRepository<PhoneDataEntity, Long> {

    Optional<PhoneDataEntity> findByPhone(String email);

    @Query("SELECT p FROM PhoneDataEntity p WHERE p.phone = :phone")
    Optional<PhoneDataEntity> findPhoneData(@Param("phone") String phone);

}
