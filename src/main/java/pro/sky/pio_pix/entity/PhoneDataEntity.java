package pro.sky.pio_pix.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "phone_data")
public class PhoneDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "phone")
    @Pattern(regexp = "^79\\d{9}$"
            , message = "Мобильный телефон пользователя | формат: 79012345678")
    private String phone;

    public PhoneDataEntity(UserEntity user) {
        this.user = user;
    }

    public PhoneDataEntity(Long id, UserEntity user, String phone) {
        this.id = id;
        this.user = user;
        this.phone = phone;
    }

}
