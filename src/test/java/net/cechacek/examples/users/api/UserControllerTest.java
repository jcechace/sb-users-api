package net.cechacek.examples.users.api;

import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import net.cechacek.examples.users.api.dto.UserResponse;
import net.cechacek.examples.users.persistence.access.UserRepository;
import net.cechacek.examples.users.services.AuthTokenService;
import net.cechacek.examples.users.services.UserService;
import net.cechacek.examples.users.services.domain.AuthUser;
import net.cechacek.examples.users.services.domain.User;
import net.cechacek.examples.users.util.UserMapper;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;


/**
 * Test for {@link UserController} intended to demonstrate a possibel way of testing REST API.
 */
//@formatter:off
public class UserControllerTest extends BaseControllerTest  {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthTokenService authTokenService;

    @Override
    public String getControllerBasePath() {
        return UserController.PATH;
    }

    @BeforeEach
    @Transactional
    public void setup() {
        userRepository.deleteAll();
    }

    public User ensureUser(int sn) {
        var user =  new User(
                null,
                "user" + sn,
                "user " + sn + "@example.com",
                "password" + sn
        );
        user = userService.create(user);
        Assumptions.assumeThat(userRepository.findById(user.getId())).isPresent();
        return user;
    }

    public List<User> ensureUsers(int count) {
        return IntStream.range(1, count + 1)
                .mapToObj(this::ensureUser)
                .toList();
    }

    public String getToken(User user) {
        return authTokenService.generateJWTToken(new AuthUser(user.getEmail(), user.getPassword()));
    }

    @Test
    public void getNonExistingUser() {
        given()
            .accept(ContentType.JSON).
        when()
            .get("/4242").
        then()
            .statusCode(404);
    }

    @Test
    public void getUser() {
        var expected = ensureUser(1);

        var actual =
            given()
                .accept(ContentType.JSON).
            when()
                .get("/{id}", expected.getId()).
            then()
                .statusCode(200)
                .contentType(ContentType.JSON).
            and()
                .extract().as(UserResponse.class);

        Assertions.assertThat(actual).isEqualTo(userMapper.toResponse(expected));
    }

    @Test
    public void getUserList() {
        var expected = ensureUsers(3);

        var actual =
            given()
                .accept(ContentType.JSON).
            when()
                .get().
            then()
                .statusCode(200)
                .contentType(ContentType.JSON).
            and()
                .extract().jsonPath().getList(".", UserResponse.class);

        Assertions.assertThat(actual).containsExactlyElementsOf(userMapper.toResponse(expected));
    }

    @Test
    public void createUser() {
        var response =
            given()
                .body("""
                        {
                            "name": "Thomas Jasper Cat",
                            "email": "toma@example.com",
                            "password": "secret"
                        }
                        """).
            when()
                .post().
            then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .header("Location", matchesRegex(fullUrl() + "/\\d+")).
            and()
                .extract().as(UserResponse.class);

        var actual  = userService.findById(response.getId());
        Assertions.assertThat(actual).map(userMapper::toResponse).hasValue(response);
    }

    @Test
    public void deleteUserNoAuth() {
        var user = ensureUser(1);

        when()
            .delete("/{id}", user.getId()).
        then()
            .statusCode(401);

        var acutal = userService.findById(user.getId());
        Assertions.assertThat(acutal).isPresent();
    }

    @Test
    public void deleteUserBasicAuth() {
        var users = ensureUsers(2);
        var user = users.get(0);
        var deleted = users.get(1);

        given()
            .auth().basic(user.getEmail(), user.getPassword()).
        when()
            .delete("/{id}", deleted.getId()).
        then()
            .statusCode(401);

        var acutal = userService.findById(deleted.getId());
        Assertions.assertThat(acutal).isPresent();
    }

    @Test
    public void deleteUserTokenAuth() {
        var users = ensureUsers(2);
        var token = getToken(users.get(0));
        var deleted = users.get(1);

        given()
            .auth().oauth2(token).
        when()
            .delete("/{id}", deleted.getId()).
        then()
            .statusCode(204);

        var acutal = userService.findById(deleted.getId());
        Assertions.assertThat(acutal).isEmpty();
    }

}
//@formatter:on