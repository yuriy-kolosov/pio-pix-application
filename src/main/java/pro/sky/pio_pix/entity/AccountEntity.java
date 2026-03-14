package pro.sky.pio_pix.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "account")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", unique = true)
    protected UserEntity user;

    @Column(name = "balance")
    private BigDecimal balance;

    public AccountEntity(UserEntity user) {
        this.user = user;
    }

    public AccountEntity(Long id, UserEntity user, BigDecimal balance) {
        this.id = id;
        this.user = user;
        this.balance = balance;
    }

}
