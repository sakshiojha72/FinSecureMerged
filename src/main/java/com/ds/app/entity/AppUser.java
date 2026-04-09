package com.ds.app.entity;

import com.ds.app.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
<<<<<<< HEAD
import lombok.AllArgsConstructor;
=======
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
<<<<<<< HEAD
@AllArgsConstructor
=======
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
@EqualsAndHashCode(exclude = "role")
@ToString(exclude = {
    "failedLoginAttemptsCount",
    "isAccountLocked"
})
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;
    private String password;

    private Integer failedLoginAttemptsCount = 0;
    private Boolean isAccountLocked = false;

    private UserRole role;
<<<<<<< HEAD
=======

    
    public AppUser(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
}