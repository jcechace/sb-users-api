package net.cechacek.examples.users.util;

import net.cechacek.examples.users.api.dto.UserRequest;
import net.cechacek.examples.users.api.dto.UserResponse;
import net.cechacek.examples.users.persistence.UserEntity;
import net.cechacek.examples.users.services.domain.AuthUser;
import net.cechacek.examples.users.services.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class UserMapperTest {

    static final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    static final UserEntity entity = new UserEntity()
            .withId(1L)
            .withName("Thomas Jasper Cat")
            .withEmail("tom@example.com")
            .withPassword("secret");

    static final User model = new User(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getPassword()
    );


    static final AuthUser authModel = new AuthUser(
            entity.getEmail(),
            entity.getPassword()
    );

    static final UserRequest request = new UserRequest(
            entity.getName(),
            entity.getEmail(),
            entity.getPassword()
    );

    static final UserResponse response = new UserResponse(
            entity.getId(),
            entity.getName(),
            entity.getEmail()
    );

    @Test
    public void testModelToEntity() {
        // when
        var actual = mapper.toEntity(model);
        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(entity);
    }

    @Test
    public void testEntityToModel() {
        // when
        var actual = mapper.toModel(entity);
        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(model);
    }

    @Test
    public void testModelToEntityNull() {
        // when
        var actual = mapper.toEntity(null);
        // then
        Assertions.assertThat(actual).isNull();
    }

    @Test
    public void testEntityToModelNull() {
        // when
        var actual = mapper.toModel((UserEntity) null);
        // then
        Assertions.assertThat(actual).isNull();
    }

    @Test
    public void testModelToResponse() {
        // when
        var actual = mapper.toResponse(model);
        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    public void testEntityToAuthModel() {
        // when
        var actual = mapper.toAuthModel(entity);
        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(authModel);
    }

    @Test
    public void testRequestToModel() {
        // when
        var actual = mapper.toModel(request);
        // then
        Assertions.assertThat(actual).usingRecursiveComparison()
                // request has no id but model field has id set
                .ignoringFields("id")
                .isEqualTo(model);
    }

}
