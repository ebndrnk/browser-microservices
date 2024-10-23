package org.ebndrnk.springbrauser.model.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "t_user")
@Data
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 4, message = "Больше 3-ёх символов")
    private String username;

    @Size(min = 8, message = "Пароль не должен быть менее 8-и символов")
    private String password;

    @Transient
    private String passwordConfirm;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.EAGER)
    @JoinTable(name = "t_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SearchRequest> searchRequests;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "user",
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Photo photo;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean photoExist(){
        return this.photo != null;
    }
}
