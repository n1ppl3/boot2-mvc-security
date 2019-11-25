package ru.pipan.spring.security.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SecuredMethodsConfiguration.class, ClassWithSecuredMethodsTest.TestUserDetailsConfig.class, ClassWithSecuredMethods.class})
class ClassWithSecuredMethodsTest {

    private static final String JANE = "JANE";
    private static final String JOHN = "JOHN";
    private static final String BEAN_NAME = "myUserDetailsService";

    @Autowired
    private ClassWithSecuredMethods classWithSecuredMethods;

    static class TestUserDetailsConfig {
        @Bean(name = BEAN_NAME)
        UserDetailsService userDetailsService() {
            UserDetails user = User.withDefaultPasswordEncoder().username(JOHN).password("").roles("VIEWER").authorities("SYS_ADMIN").build();
            UserDetails userWithoutRights = User.withDefaultPasswordEncoder().username(JANE).password("").authorities(emptyList()).build();
            return new InMemoryUserDetailsManager(user, userWithoutRights);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - >>>

    @Test
    @WithUserDetails(value = JOHN, userDetailsServiceBeanName = BEAN_NAME)
    void getUsernameInLowerCase0() {
        assertEquals("john", classWithSecuredMethods.getUsernameInLowerCase());
    }

    @Test
    @WithUserDetails(value = JANE, userDetailsServiceBeanName = BEAN_NAME)
    void getUsernameInLowerCase1() {
        assertThrows(AccessDeniedException.class, () -> classWithSecuredMethods.getUsernameInLowerCase()).printStackTrace();
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - >>>

    @Test
    @WithMockUser(username = "JOHN", authorities = {"SYS_ADMIN"})
    void getUsernameInLowerCase2() {
        assertEquals("john", classWithSecuredMethods.getUsernameInLowerCase());
    }

    @Test
    @WithAnonymousUser
    void getUsernameInLowerCase3() {
        assertThrows(AccessDeniedException.class, () -> classWithSecuredMethods.getUsernameInLowerCase()).printStackTrace();
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - >>>

    @Test
    @WithMockUser(username = "john", roles = {"VIEWER"})
    void getUsername_Success() {
        assertEquals("john", classWithSecuredMethods.getUsername());
    }

    @Test
    @WithAnonymousUser
    void getUsername_Failure() {
        assertThrows(AccessDeniedException.class, () -> classWithSecuredMethods.getUsername()).printStackTrace();
    }

}
