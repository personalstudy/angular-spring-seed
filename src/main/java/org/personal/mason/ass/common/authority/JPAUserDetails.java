package org.personal.mason.ass.common.authority;

import org.personal.mason.ass.common.authority.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mason on 6/30/14.
 */
public class JPAUserDetails implements UserDetails {

	private static final long serialVersionUID = -8819484201008675440L;
	private User user;
    private boolean enableGroups;

    public JPAUserDetails(User user, boolean enableGroups) {
        this.user = user;
        this.enableGroups = enableGroups;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(user != null) {
            Set<GrantedAuthority> authorities = user.getAuthorities().stream().filter(role -> role.isEnabled()).map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

            if(enableGroups){
                Set<GrantedAuthority> authoritySet = new HashSet<>();
                user.getGroups().stream().filter(group -> group.isEnabled()).forEach(group -> {
                    authoritySet.addAll(group.getRoles().stream().filter(role -> role.isEnabled()).map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
                });
                authorities.addAll(authoritySet);
            }

            return authorities;
        }
        return Collections.emptyList();
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
        return user.isNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
