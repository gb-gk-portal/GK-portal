package ru.geekbrains.gkportal.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "account")
public class Account extends AbstractEntity {

    @Column(name = "login")
    @NotNull(message = "Couldn't be empty!")
    private String login;

    @Column(name = "confirmed")
    @NotNull(message = "Couldn't be empty!")
    private boolean confirmed;

    @Column(name = "active")
    @NotNull(message = "Couldn't be empty!")
    private boolean active;

    @Column(name = "password_hash")
    @NotNull(message = "Couldn't be empty!")
    private String passwordHash;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "account_role",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @NotNull(message = "Role s.b. selected!")
    private Collection<Role> roles;

}
