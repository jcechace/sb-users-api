package net.cechacek.examples.users.util;

import net.cechacek.examples.users.api.dto.UserRequest;
import net.cechacek.examples.users.api.dto.UserResponse;
import net.cechacek.examples.users.services.domain.AuthUser;
import net.cechacek.examples.users.services.domain.User;
import net.cechacek.examples.users.persistence.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel =  ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {

    /**
     * Maps entity to authentication model instance
     * @param entity entity instance
     * @return AuthUser model
     */
    @Mapping(target = "username", source = "email")
    AuthUser toAuthModel(UserEntity entity);

    /**
     * Maps API request to service model instance
     * @param request request instance
     * @return User service model
     */
    User toModel(UserRequest request);

    /**
     * Maps entity to service model instance
     * @param entity entity instance
     * @return User service model
     */
    User toModel(UserEntity entity);

    /**
     * Collection counterpart of {@link #toModel(UserEntity)}
     */
    List<User> toModel(Iterable<UserEntity> entity);

    /**
     * Maps service model to entity instance
     * @param model service model instance
     * @return entity instance
     */
    UserEntity toEntity(User model);

    /**
     * Maps service model to API response instance
     * @param model service model instance
     * @return API response instance
     */
    UserResponse toResponse(User model);

    /**
     * Collection counterpart of {@link #toResponse(User)}
     */
    List<UserResponse> toResponse(Iterable<User> model);
}
