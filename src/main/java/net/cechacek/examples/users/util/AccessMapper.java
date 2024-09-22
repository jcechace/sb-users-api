package net.cechacek.examples.users.util;

import net.cechacek.examples.users.api.dto.AccessResponse;
import net.cechacek.examples.users.domain.Access;
import net.cechacek.examples.users.persistence.AccessEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AccessMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    Access toModel(AccessEntity entity);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    List<Access> toModel(Iterable<AccessEntity> entity);

    AccessResponse toResponse(Access model);
    List<AccessResponse> toResponse(Iterable<Access> model);
}
