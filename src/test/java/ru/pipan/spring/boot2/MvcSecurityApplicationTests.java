package ru.pipan.spring.boot2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.pipan.spring.boot2.controller.SecuredController;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.pipan.spring.boot2.config.SecurityConstants.MY_ROLE_MANAGER;
import static ru.pipan.spring.boot2.config.SecurityConstants.USER_NAME_MANAGER;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MvcSecurityApplicationTests {

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
