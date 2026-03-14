package pro.sky.pio_pix.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.pio_pix.entity.UserEntity;

import java.time.LocalDate;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByName(String name);

    @Query(value = "SELECT u FROM UserEntity u WHERE u.name LIKE :name%")
    Page<UserEntity> findByNameWithPagination(@Param("name") String name, PageRequest userPages);

    @Query(value = "SELECT u FROM UserEntity u WHERE u.dateOfBirth BETWEEN :date AND CURRENT_DATE")
    Page<UserEntity> findByDateOfBirthWithPagination(@Param("date") LocalDate date, PageRequest userPages);

}
