package com.safeapp.admin.web.model;

import java.util.Collection;

import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Setter;

@Setter
public class AdminDetails extends Admins implements UserDetails {
    
    private boolean enabled = false;
    private boolean expired = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getType().getAuthorities();
    }

    @Override
    public String getUsername() {
        return this.getAdminID();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
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

}