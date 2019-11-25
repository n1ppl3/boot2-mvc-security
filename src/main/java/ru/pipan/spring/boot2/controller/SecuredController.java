package ru.pipan.spring.boot2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

import static ru.pipan.spring.boot2.config.SecurityConstants.MY_ROLE_MANAGER;

@RestController
public class SecuredController {
    private static final Logger logger = LoggerFactory.getLogger(SecuredController.class);

    public static final String MAPPING = "/foo/salute";

    @Secured("ROLE_" + MY_ROLE_MANAGER)
    @GetMapping(path = {MAPPING})
    public String saluteYourManager(@AuthenticationPrincipal User activeUser) {
        logger.warn("activeUser: {}", activeUser);
        return String.format("Hi '%s'! It's %s", activeUser.getUsername(), ZonedDateTime.now());
    }
}
