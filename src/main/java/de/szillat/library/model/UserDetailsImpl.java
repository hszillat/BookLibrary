package de.szillat.library.model;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private static final Logger _log = LoggerFactory.getLogger(UserDetailsImpl.class);

    private final User user;

    public UserDetailsImpl(@NonNull User user) {
        this.user = user;

        _log.debug("Using user: '{}'", user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Store the role for an account in the DB and use it here.
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("USER");
        return Arrays.asList(authority);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
