package net.cechacek.examples.users.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.cechacek.examples.users.persistence.UserEntity;


@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class User {
    private final Long id;
    private final String name;
    private final String email;

    public static User fromEntity(UserEntity entity) {
        return new User(entity.getId(), entity.getName(), entity.getEmail());
    }
}