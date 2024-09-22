package net.cechacek.examples.users.util;

import net.cechacek.examples.users.api.dto.UserRequest;
import net.cechacek.examples.users.api.dto.UserResponse;
import net.cechacek.examples.users.domain.User;
import net.cechacek.examples.users.persistence.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel =  ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {
    User toModel(UserRequest request);
    User toModel(UserEntity entity);
    List<User> toModel(Iterable<UserEntity> entity);

    UserEntity toEntity(User model);

    UserResponse toResponse(User model);
    List<UserResponse> toResponse(Iterable<User> model);

}
