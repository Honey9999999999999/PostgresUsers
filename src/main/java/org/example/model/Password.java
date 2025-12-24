package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.util.PasswordHasher;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Password {
    @Id
    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String salt;

    public Password(User user, String password){
        this.user = user;
        salt = PasswordHasher.generateSalt();
        this.password = PasswordHasher.hashPassword(password, salt);
    }
}
