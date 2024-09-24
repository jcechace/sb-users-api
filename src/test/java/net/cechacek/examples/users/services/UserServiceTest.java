package net.cechacek.examples.users.services;

import jakarta.transaction.Transactional;
import net.cechacek.examples.users.TestcontainersConfiguration;
import net.cechacek.examples.users.domain.User;
import net.cechacek.examples.users.persistence.UserEntity;
import net.cechacek.examples.users.persistence.access.UserRepository;
import net.cechacek.examples.users.util.UserMapper;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Transactional
public class UserServiceTest  {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        Assumptions.assumeThat(userRepository.findAll()).isEmpty();
    }

    public UserEntity userEntity(int sn) {
        return new UserEntity()
                .withName("user" + sn)
                .withEmail("user " + sn + "@example.com")
                .withPassword("password" + sn);
    }

    public User userModel(int sn) {
        return userMapper.toModel(userEntity(sn));
    }

    @Test
    public void shouldReturnEmptyListForNoUsers() {
        // when
        var actual = userService.findAll();
        // then
        Assertions.assertThat(actual).isEmpty();
    }

    @Test
    public void shouldReturnListOfAllUsers() {
        // given existing users
        var given = userRepository.saveAll(List.of(userEntity(1), userEntity(2)));
        var expected = userMapper.toModel(given);
        // when
        var actual = userService.findAll();
        // then
        Assertions.assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    public void shouldReturnUserById() {
        // given existing users
        var given = userRepository.saveAll(List.of(userEntity(1), userEntity(2)));
        var expected = userMapper.toModel(given.getFirst());
        // when
        var actual = userService.findById(given.getFirst().getId());
        // then
        Assertions.assertThat(actual).hasValue(expected);
    }

    @Test
    public void shouldReturnEmptyForNonExistingUser() {
        // given existing users
        var given = userRepository.saveAll(List.of(userEntity(1), userEntity(2)));
        // when
        var actual = userService.findById(1000);
        // then
        Assertions.assertThat(actual).isEmpty();
    }

    @Test
    public void shouldCreateUser() {
        // given
        var given = userModel(1);
        // when
        var actual = userService.create(given);
        // then
        var expectedEntity = userRepository.findById(actual.getId());
        Assertions.assertThat(expectedEntity).isPresent();

        var expectedModel = userMapper.toModel(expectedEntity.get());
        Assertions.assertThat(actual).isEqualTo(expectedModel);

        // password should be encoded
        Assertions.assertThat(actual.getPassword()).isNotEqualTo(given.getPassword());
        var encodedPasswordMatch = passwordEncoder.matches(given.getPassword(), actual.getPassword());
        Assertions.assertThat(encodedPasswordMatch).isTrue();
    }

    @Test
    public void shouldDeleteUser() {
        // given
        var given = userRepository.save(userEntity(1));
        // when
        userService.delete(given.getId());
        // then
        var actual = userRepository.findById(given.getId());
        Assertions.assertThat(actual).isEmpty();
    }
}
