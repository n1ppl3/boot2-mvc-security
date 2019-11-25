package ru.pipan.spring.security.core;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ClassWithSecuredMethods {

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    public String getUsernameInLowerCase() {
        return getUsername().toLowerCase();
    }

    @Secured("ROLE_VIEWER")
    public String getUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext.getAuthentication().getName();
    }
}
