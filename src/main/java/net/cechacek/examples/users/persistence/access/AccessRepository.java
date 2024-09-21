package net.cechacek.examples.users.persistence.access;

import net.cechacek.examples.users.persistence.AccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccessRepository extends JpaRepository<AccessEntity, Long> {
    void deleteByUserIdAndProjectId(Long userId, String projectId);

    List<AccessEntity> findAllByUserId(Long userId);

    List<AccessEntity> findAllByProjectId(String projectId);

    Optional<AccessEntity> findByUserIdAndProjectId(Long userId, String projectId);
}
