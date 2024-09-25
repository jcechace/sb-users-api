package net.cechacek.examples.users.util;

import net.cechacek.examples.users.api.dto.AccessRequest;
import net.cechacek.examples.users.api.dto.AccessResponse;
import net.cechacek.examples.users.persistence.AccessEntity;
import net.cechacek.examples.users.persistence.UserEntity;
import net.cechacek.examples.users.services.domain.Access;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class AccessMapperTest {

    static final AccessMapper mapper = Mappers.getMapper(AccessMapper.class);

    static final UserEntity userEntity = new UserEntity()
            .withId(1L)
            .withName("Thomas Jasper Cat")
            .withEmail("tom@example.com")
            .withPassword("secret");

    static final AccessEntity entity = new AccessEntity()
            .withId(1L)
            .withUser(userEntity)
            .withProjectId("mouse-trap")
            .withProjectName("How to catch a mouse");

    static final Access model = new Access(
            userEntity.getId(),
            userEntity.getName(),
            entity.getProjectId(),
            entity.getProjectName()
    );

    static final AccessResponse response = new AccessResponse(
            userEntity.getId(),
            userEntity.getName(),
            entity.getProjectId(),
            entity.getProjectName()
    );

    @Test
    public void testEntityToModel() {
        // when
        var actual = mapper.toModel(entity);
        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(model);
    }

    @Test
    public void testEntityToModelNull() {
        // when
        var actual = mapper.toModel((AccessEntity) null);
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
}
