package com.mlkb.ftm.config;

import com.mlkb.ftm.entity.Authorities;
import com.mlkb.ftm.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
public class UserDetailsDTO implements UserDetails {
    private final String email;
    private final String password;
    private final Set<Authorities> authorities;
    private final boolean isEnabled;

    public UserDetailsDTO(User userInfo) {
        this.email = userInfo.getEmail();
        this.password = userInfo.getPassword();
        this.authorities = userInfo.getAuthorities();
        this.isEnabled = userInfo.getEnabled() > 0;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
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
        return this.isEnabled;
    }
}
