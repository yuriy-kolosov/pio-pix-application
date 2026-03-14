package pro.sky.pio_pix.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.pio_pix.entity.TransferDataEntity;
import pro.sky.pio_pix.entity.UserEntity;

import java.time.LocalDate;

public interface TransferDataRepository extends JpaRepository<TransferDataEntity, Long> {

    @Query(value = "SELECT t FROM TransferDataEntity t")
    Page<TransferDataEntity> findWithPagination(PageRequest userPages);


}
