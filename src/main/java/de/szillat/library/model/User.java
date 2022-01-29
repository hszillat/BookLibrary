package de.szillat.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "users", indexes = {@Index(columnList = "username")})
public class User {
    @Id
    @NonNull
    @Email
    @Column(nullable = false, unique = true)
    public String username;

    @NonNull
    public String password;

    @Column(nullable = false)
    public boolean enabled;
}
