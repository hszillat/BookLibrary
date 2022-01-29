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
@Table(name = "authorities", indexes = {@Index(columnList = "username")})
public class Authorities {
    @Id
    @NonNull
    @Email
    @Column(nullable = false, unique = true)
    @JoinColumn(table = "users", name = "username")
    public String username;

    @Column(nullable = false)
    public String authority;
}
