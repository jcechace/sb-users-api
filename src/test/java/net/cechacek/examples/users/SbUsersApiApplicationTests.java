package net.cechacek.examples.users;

import net.cechacek.examples.users.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SbUsersApiApplicationTests {

	@Autowired
	UserService userService;

	@Test
	void contextLoads() {
	}

}
