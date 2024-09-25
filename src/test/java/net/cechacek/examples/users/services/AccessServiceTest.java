package net.cechacek.examples.users.services;

import jakarta.transaction.Transactional;
import net.cechacek.examples.users.TestcontainersConfiguration;
import net.cechacek.examples.users.services.domain.Access;
import net.cechacek.examples.users.persistence.AccessEntity;
import net.cechacek.examples.users.persistence.UserEntity;
import net.cechacek.examples.users.persistence.access.AccessRepository;
import net.cechacek.examples.users.persistence.access.UserRepository;
import net.cechacek.examples.users.util.AccessMapper;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;


@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Transactional
public class AccessServiceTest {
    @Autowired
    AccessService accessService;

    @Autowired
    AccessRepository accessRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccessMapper accessMapper;

    List<Long> userIds;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        accessRepository.deleteAll();
        Assumptions.assumeThat(userRepository.findAll()).isEmpty();
        Assumptions.assumeThat(accessRepository.findAll()).isEmpty();

        userIds = userRepository.saveAll(List.of(userEntity(1), userEntity(2)))
                .stream()
                .map(UserEntity::getId)
                .toList();
    }

    public UserEntity userEntity(int sn) {
        return new UserEntity()
                .withName("user" + sn)
                .withEmail("user " + sn + "@example.com")
                .withPassword("password" + sn);
    }

    public AccessEntity accessEntity(int sn, long userId) {
        var user = userRepository.findById(userId);
        Assumptions.assumeThat(user).isPresent();

        return new AccessEntity()
                .withUser(user.get())
                .withProjectName("project " + sn)
                .withProjectId("project" + sn);
    }

    public Access accessModel(int sn, long userId) {
        return accessMapper.toModel(accessEntity(sn, userId));
    }


    @Test
    public void shouldReturnEmptyListForNoAccess() {
        // vwhen
        var actual = accessService.findAll();
        // then
        Assertions.assertThat(actual).isEmpty();
    }

    @Test
    public void shouldReturnListOfAllAccess() {
        // given existing access grants
        var given = accessRepository.saveAll(
                List.of(accessEntity(1, userIds.get(0)), accessEntity(2, userIds.get(1)))
        );
        var expected = accessMapper.toModel(given);
        // when
        var actual = accessService.findAll();
        // then
        Assertions.assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    public void shouldReturnListOfAllAccessForUser() {
        // given existing access grants
        var given = accessRepository.saveAll(
                List.of(accessEntity(1, userIds.get(0)), accessEntity(2, userIds.get(1)))
        );
        var expected = accessMapper.toModel(given)
                .stream()
                .filter(access -> access.getUserId() == userIds.getFirst())
                .toList();
        // when
        var actual = accessService.findAllByUser(userIds.getFirst());
        // then
        Assertions.assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    public void shouldReturnListOfAllAccessForProject() {
        // given existing access grants
        var given = accessRepository.saveAll(List.of(
                accessEntity(1, userIds.get(0)),
                accessEntity(2, userIds.get(0)),
                accessEntity(2, userIds.get(1))
        ));
        var projectId = "project2";
        var expected = accessMapper.toModel(given)
                .stream()
                .filter(access -> access.getProjectId().equals(projectId))
                .toList();
        // when
        var actual = accessService.findAllByProject(projectId);
        // then
        Assertions.assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    public void shouldReturnAccess() {
        // given existing access grants
        var given = accessRepository.saveAll(List.of(
                accessEntity(1, userIds.get(0)),
                accessEntity(2, userIds.get(0)),
                accessEntity(2, userIds.get(1))
        ));
        var projectId = "project2";
        var expected = accessMapper.toModel(given.get(1));
        // when
        var actual = accessService.findByUserAndProject(userIds.get(0), projectId);
        // then
        Assertions.assertThat(actual).hasValue(expected);
    }

    @Test
    public void shouldCreateAccess() {
        // given
        var given = accessModel(1, userIds.getFirst());
        // when
        var actual = accessService.create(given.getUserId(), given.getProjectId(), given.getProjectName());
        // then
        var expectedEntity = accessRepository.findByUserIdAndProjectId(given.getUserId(), given.getProjectId());
        Assertions.assertThat(expectedEntity).isPresent();

        var expectedModel = accessMapper.toModel(expectedEntity.get());
        Assertions.assertThat(actual).hasValue(expectedModel);

        Assumptions.assumeThat(accessRepository.findAll()).hasSize(1);
    }

    @Test
    public void shouldNotCreateAccessForInvalidUser() {
        // given
        var given = accessModel(1, userIds.getFirst());
        userRepository.deleteById(given.getUserId());
        // when
        var actual = accessService.create(given.getUserId(), given.getProjectId(), given.getProjectName());
        // then
        var expectedEntity = accessRepository.findByUserIdAndProjectId(given.getUserId(), given.getProjectId());
        Assertions.assertThat(expectedEntity).isEmpty();
        Assertions.assertThat(actual).isEmpty();
    }


    @Test
    public void shouldDeleteUser() {
        // given
        var given = accessRepository.saveAll(List.of(
                accessEntity(1, userIds.get(0)),
                accessEntity(2, userIds.get(1))
        ));
        // when
        accessRepository.deleteByUserIdAndProjectId(userIds.get(0), given.getFirst().getProjectId());
        // then
        var actual = accessService.findByUserAndProject(userIds.get(0), given.getFirst().getProjectId());
        Assertions.assertThat(actual).isEmpty();

        Assertions.assertThat(accessRepository.findAll()).hasSize(1);
    }

}

