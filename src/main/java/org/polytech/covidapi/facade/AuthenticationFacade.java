package org.polytech.covidapi.facade;

import org.polytech.covidapi.entities.ERole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {
    
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public boolean hasRole(ERole role) {
        System.out.println(role);
        return getAuthentication().getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(role.name()));
    }
}