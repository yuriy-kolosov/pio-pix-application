package pro.sky.pio_pix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.pio_pix.entity.EmailDataEntity;

import java.util.Optional;

public interface EmailDataRepository extends JpaRepository<EmailDataEntity, Long> {

    Optional<EmailDataEntity> findByEmail(String email);

    @Query("SELECT e FROM EmailDataEntity e WHERE e.email = :email")
    Optional<EmailDataEntity> findEmailData(@Param("email") String email);

}
