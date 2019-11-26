package ru.pipan.spring.boot2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.pipan.spring.boot2.config.SecurityConfig;
import ru.pipan.spring.boot2.controller.SecuredController;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.pipan.spring.boot2.config.SecurityConstants.MY_ROLE_MANAGER;
import static ru.pipan.spring.boot2.config.SecurityConstants.USER_NAME_MANAGER;

@Import({SecurityConfig.class})
@WebMvcTest(SecuredController.class)
// If you are looking to load your full application configuration and use MockMVC, you should consider @SpringBootTest combined with @AutoConfigureMockMvc rather than this annotation.
class MvcSecurityApplicationTest0 {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser()
    void endpointAuthForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SecuredController.MAPPING)
                .accept(MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = USER_NAME_MANAGER, roles = {MY_ROLE_MANAGER})
    void endpointAuthOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SecuredController.MAPPING)
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(USER_NAME_MANAGER)));
    }

}
