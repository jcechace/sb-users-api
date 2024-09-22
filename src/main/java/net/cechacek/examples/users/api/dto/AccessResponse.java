package net.cechacek.examples.users.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessResponse {
    private long userId;
    private String userName;
    private String projectId;
    private String projectName;
}
