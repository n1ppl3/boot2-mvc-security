package ru.pipan.spring.security.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SecuredMethodsConfiguration.class, ClassWithSecuredMethods.class})
class ClassWithSecuredMethodsTest {

    @Autowired
    private ClassWithSecuredMethods classWithSecuredMethods;

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - >>>

    @Test
    @WithMockUser(username = "JOHN", authorities = {"SYS_ADMIN"})
    void getUsernameInLowerCase_Success() {
        String username = classWithSecuredMethods.getUsernameInLowerCase();
        assertEquals("john", username);
    }

    @Test
    @WithAnonymousUser
    void getUsernameInLowerCase_Failure() {
        Exception e = assertThrows(AccessDeniedException.class, () -> classWithSecuredMethods.getUsernameInLowerCase());
        e.printStackTrace();
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - >>>

    @Test
    @WithMockUser(username = "john", roles = {"VIEWER"})
    void getUsername_Success() {
        String userName = classWithSecuredMethods.getUsername();
        assertEquals("john", userName);
    }

    @Test
    @WithAnonymousUser
    void getUsername_Failure() {
        Exception e = assertThrows(AccessDeniedException.class, () -> classWithSecuredMethods.getUsername());
        e.printStackTrace();
    }

}
