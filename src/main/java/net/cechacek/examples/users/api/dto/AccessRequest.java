package net.cechacek.examples.users.api.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequest {
    @Size(min = 3, max = 50, message = "{invalid.field}")
    private String projectName;
}