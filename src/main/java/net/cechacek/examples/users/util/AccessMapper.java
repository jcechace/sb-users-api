package net.cechacek.examples.users.util;

import net.cechacek.examples.users.api.dto.AccessResponse;
import net.cechacek.examples.users.services.domain.Access;
import net.cechacek.examples.users.persistence.AccessEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AccessMapper {

    /**
     * Maps entity to service model instance
     * @param entity entity instance
     * @return Access service model
     */
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    Access toModel(AccessEntity entity);

    /**
     * Collection counterpart of {@link #toModel(AccessEntity)}
     */
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    List<Access> toModel(Iterable<AccessEntity> entity);

    /**
     * Maps service model to API response instance
     * @param model service model instance
     * @return API response instance
     */
    AccessResponse toResponse(Access model);

    /**
     * Collection counterpart of {@link #toResponse(Access)}
     */
    List<AccessResponse> toResponse(Iterable<Access> model);
}
