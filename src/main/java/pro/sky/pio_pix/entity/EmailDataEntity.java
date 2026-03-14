package pro.sky.pio_pix.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "email_data")
public class EmailDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "email")
    @Email
    private String email;

    public EmailDataEntity(UserEntity user) {
        this.user = user;
    }

    public EmailDataEntity(Long id, UserEntity user, String email) {
        this.id = id;
        this.user = user;
        this.email = email;
    }

}
