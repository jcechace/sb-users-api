package net.cechacek.examples.users.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.cechacek.examples.users.persistence.UserEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotEmpty(message = "{required.field")
    @Size(min = 3, max = 50, message = "{invalid.field}")
    private String name;
    @Email(message = "{invalid.field}")
    private String email;
    @NotEmpty(message = "{required.field")
    @Size(min = 3, max = 50, message = "{invalid.field}")
    private String password;

    public UserEntity toEntity() {
        var entity = new UserEntity();
        entity.setName(name);
        entity.setEmail(email);
        entity.setPassword(password);
        return entity;
    }
}