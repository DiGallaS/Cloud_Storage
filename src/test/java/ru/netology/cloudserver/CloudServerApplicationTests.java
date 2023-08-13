package ru.netology.cloudserver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import com.google.gson.Gson;
import org.springframework.http.MediaType;
import ru.netology.cloudserver.controller.FileController;
import ru.netology.cloudserver.model.AuthorizationRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudServerApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private MockMvc mockMvc;

	private static final String WRONG_LOGIN = "user";
	private static final String LOGIN = "basa1@gmail.com";
	private static final String PASSWORD = "11111";
	private static final String ENDPOINT_LOGIN = "/login";
	private static final String ENDPOINT_LOGOUT = "/logout";
	private static AuthorizationRequest validRequest;
	private static AuthorizationRequest invalidRequest;
	private static final Gson gson = new Gson();

	@BeforeAll
	public static void beforeAll(){
		validRequest = new AuthorizationRequest(LOGIN, PASSWORD);
		invalidRequest = new AuthorizationRequest(WRONG_LOGIN, PASSWORD);
	}

	@Test
	void testLoginSuccess() throws Exception {
		this.mockMvc.perform(post(ENDPOINT_LOGIN).contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(validRequest))).andExpect(status().isOk());
	}

	@Test
	void testLoginFail() throws Exception {
		mockMvc.perform(post(ENDPOINT_LOGIN).contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(invalidRequest))).andExpect(status().isUnauthorized());
	}

	@Test
	void testLogoutSuccess() throws Exception {
		mockMvc.perform(post(ENDPOINT_LOGOUT).contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(validRequest))).andExpect(status().is3xxRedirection());
	}


	@Test
	void FilesControllerTest() {
		ServletContext servletContext = webApplicationContext.getServletContext();
		Assertions.assertNotNull(servletContext);
		Assertions.assertNotNull(webApplicationContext.getBean(FileController.class));
	}


}
