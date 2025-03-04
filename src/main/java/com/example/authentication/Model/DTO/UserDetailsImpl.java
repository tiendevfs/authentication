package com.example.authentication.Model.DTO;

import com.example.authentication.Model.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private String username;
    private String fullname;
    private String password;
    private List<SimpleGrantedAuthority> authorities;

    public static List<String> convert(Collection<? extends GrantedAuthority> authorities){
        return authorities.stream().map(role -> role.getAuthority())
                .collect(Collectors.toList());
    }
    public UserDetailsImpl build(User user){
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.getCode()))
                .collect(Collectors.toList());

        return UserDetailsImpl.builder()
                .username(user.getUsername())
                .fullname(user.getFullname())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void erasePassword(){
        password = null;
    }
    @Override
    public String getUsername() {
        return username;
    }
}
