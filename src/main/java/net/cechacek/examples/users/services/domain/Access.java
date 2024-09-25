package net.cechacek.examples.users.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Access {
    private long userId;
    private String userName;
    private String projectId;
    private String projectName;
}
