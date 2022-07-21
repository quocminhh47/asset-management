package com.nashtech.assetmanagement.sercurity.userdetail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nashtech.assetmanagement.entities.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrinciple implements UserDetails {

    private String staffCode;
    private String userName;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> roles;

    public UserPrinciple(String staffCode, String userName, String password, Collection<? extends GrantedAuthority> roles) {
        this.staffCode = staffCode;
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }

    public static UserPrinciple build(Users users){
         List<GrantedAuthority> authorities=new ArrayList<>();
         if (users.getRole().getName().equalsIgnoreCase("ROLE_ADMIN"))
             authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if(users.getRole().getName().equalsIgnoreCase("ROLE_USER"))
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new UserPrinciple(users.getStaffCode(),
                users.getUserName(),
                users.getPassword(),
                authorities
                );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    public String getStaffCode() {
        return staffCode;
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
}
