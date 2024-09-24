package net.cechacek.examples.users.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.cechacek.examples.users.domain.Access;
import net.cechacek.examples.users.persistence.AccessEntity;
import net.cechacek.examples.users.persistence.access.AccessRepository;
import net.cechacek.examples.users.persistence.access.UserRepository;
import net.cechacek.examples.users.util.AccessMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing user access to projects.
 */
@Service
@RequiredArgsConstructor
@Transactional(Transactional.TxType.SUPPORTS)
public class AccessService {

    private final AccessRepository accessRepository;
    private final UserRepository userRepository;
    private final AccessMapper accessMapper;

    /**
     * Find all access grants.
     * @return list of acess grants
     */
    public List<Access> findAll() {
        var entities =  accessRepository.findAll();
        return accessMapper.toModel(entities);
    }

    /**
     * Find all access grants for a user.
     * @param userId user id
     * @return list of access grants for given user
     */
    public List<Access> findAllByUser(long userId) {
        var entities = accessRepository.findAllByUserId(userId);
        return accessMapper.toModel(entities);
    }

    /**
     * Find all access grants for a project.
     * @param projectId project id
     * @return list of access grants for given project
     */
    public List<Access> findAllByProject(String projectId) {
        var entities = accessRepository.findAllByProjectId(projectId);
        return accessMapper.toModel(entities);
    }

    /**
     * Find access grant for a user and project.
     * @param userId user id
     * @param projectId project id
     * @return access grant for given user and project
     */
    public Optional<Access> findByUserAndProject(long userId, String projectId) {
        return accessRepository.findByUserIdAndProjectId(userId, projectId)
                .map(accessMapper::toModel);
    }


    /**
     * Update project name for a user and project.
     * @param userId user id
     * @param projectId project id
     * @param projectName new project name
     * @return updated access grant
     */
    @Transactional(Transactional.TxType.REQUIRED)
    public Optional<Access> update(long userId, String projectId, String projectName) {
        return accessRepository
                .findByUserIdAndProjectId(userId, projectId)
                .map(access -> access.withProjectName(projectName))
                .map(accessRepository::save)
                .map(accessMapper::toModel);
    }

    /**
     * Create access grant for a user and project.
     * @param userId user id
     * @param projectId project id
     * @param projectName project name
     * @return created access grant
     */
    @Transactional(Transactional.TxType.REQUIRED)
    public Optional<Access> create(long userId, String projectId, String projectName) {
        return userRepository.findById(userId)
                .map(user -> new AccessEntity(null, user, projectId, projectName))
                .map(accessRepository::save)
                .map(accessMapper::toModel);
    }

    /**
     * Delete access grant for a user and project.
     * @param userId user id
     * @param projectId project id
     */
    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteByUserAndProject(long userId, String projectId) {
        accessRepository.deleteByUserIdAndProjectId(userId, projectId);
    }
}
