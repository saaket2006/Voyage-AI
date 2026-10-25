package com.voyageai.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class UserPrincipal implements UserDetails {

    private final String id;
    private final String email;
    private final String displayName;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(String id, String email, String displayName, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.password = password;
        this.authorities = authorities != null ? authorities : Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public static UserPrincipal create(String id, String email, String displayName, String role) {
        String effectiveRole = (role != null && !role.isBlank()) ? role : "ROLE_USER";
        if (!effectiveRole.startsWith("ROLE_")) {
            effectiveRole = "ROLE_" + effectiveRole;
        }
        return new UserPrincipal(id, email, displayName, "", Collections.singletonList(new SimpleGrantedAuthority(effectiveRole)));
    }

    public static UserPrincipal create(String id, String email, String displayName) {
        return create(id, email, displayName, "ROLE_USER");
    }

    public boolean isAdmin() {
        return authorities != null && authorities.stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
