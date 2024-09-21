package net.cechacek.examples.users.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.cechacek.examples.users.persistence.AccessEntity;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Access {
    private Long id;
    private long userId;
    private String userName;
    private String projectId;
    private String projectName;

    public static Access fromEntity(AccessEntity entity) {
        return new Access(
                entity.getId(),
                entity.getUser().getId(),
                entity.getUser().getName(),
                entity.getProjectId(),
                entity.getProjectName());
    }
}
