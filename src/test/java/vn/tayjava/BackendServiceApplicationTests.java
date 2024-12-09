package vn.tayjava;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import vn.tayjava.controller.AuthenticationController;
import vn.tayjava.controller.EmailController;
import vn.tayjava.controller.UserController;

@ExtendWith(SpringExtension.class) // kích hoạt sử dụng SpringExtension
@SpringBootTest
class BackendServiceApplicationTests {

	@Autowired
	private AuthenticationController authenticationController;

	@Autowired
	private EmailController emailController;

	@Autowired
	private UserController userController;

	// Testing if application loads correctly
	@Test
	void contextLoads() {
		Assertions.assertThat(authenticationController).isNotNull();
		Assertions.assertThat(userController).isNotNull();
		Assertions.assertThat(emailController).isNotNull();
	}
}
