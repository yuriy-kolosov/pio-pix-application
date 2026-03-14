package pro.sky.pio_pix.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "transfer_data")
public class TransferDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_from_id")
    private UserEntity userFrom;

    @ManyToOne
    @JoinColumn(name = "user_to_id")
    private UserEntity userTo;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "local_date_time")
    private LocalDateTime localDateTime;




}
