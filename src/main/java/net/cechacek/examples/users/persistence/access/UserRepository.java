package net.cechacek.examples.users.persistence.access;

import net.cechacek.examples.users.persistence.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
