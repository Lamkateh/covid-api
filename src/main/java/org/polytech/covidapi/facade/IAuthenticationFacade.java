package org.polytech.covidapi.facade;

import org.polytech.covidapi.entities.ERole;
import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
    boolean hasRole(ERole role);
}
