package net.cechacek.examples.users.util;

import net.cechacek.examples.users.domain.AuthUser;
import net.cechacek.examples.users.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel =  MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserAccountMapper {

    @Mapping(target = "username", source = "email")
    AuthUser fromUserModel(User model);
}
