package net.cechacek.examples.users;

import org.springframework.boot.SpringApplication;

public class TestSbUsersApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(SbUsersApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
