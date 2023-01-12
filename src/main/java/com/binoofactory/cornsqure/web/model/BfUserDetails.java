package com.binoofactory.cornsqure.web.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.binoofactory.cornsqure.web.model.entity.Users;

import lombok.Setter;

@Setter
public class BfUserDetails extends Users implements UserDetails {
    
    private boolean enabled = false;
    
    private boolean expired = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getType().getAuthorities();
    }

    @Override
    public String getUsername() {
        return this.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return expired;
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
        return enabled;
    }
}
