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

@Service
@RequiredArgsConstructor
@Transactional(Transactional.TxType.SUPPORTS)
public class AccessService {

    private final AccessRepository accessRepository;
    private final UserRepository userRepository;
    private final AccessMapper accessMapper;

    public List<Access> findAll() {
        var entities =  accessRepository.findAll();
        return accessMapper.toModel(entities);
    }

    public List<Access> findAllByUser(long userId) {
        var entities = accessRepository.findAllByUserId(userId);
        return accessMapper.toModel(entities);
    }

    public List<Access> findAllByProject(String projectId) {
        var entities = accessRepository.findAllByProjectId(projectId);
        return accessMapper.toModel(entities);
    }

    public Optional<Access> findByUserAndProject(long userId, String projectId) {
        return accessRepository.findByUserIdAndProjectId(userId, projectId)
                .map(accessMapper::toModel);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Optional<Access> update(long userId, String projectId, String projectName) {
        return accessRepository
                .findByUserIdAndProjectId(userId, projectId)
                .map(access -> access.withProjectName(projectName))
                .map(accessRepository::save)
                .map(accessMapper::toModel);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Optional<Access> create(long userId, String projectId, String projectName) {
        return userRepository.findById(userId)
                .map(user -> new AccessEntity(null, user, projectId, projectName))
                .map(accessRepository::save)
                .map(accessMapper::toModel);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteByUserAndProject(long userId, String projectId) {
        accessRepository.deleteByUserIdAndProjectId(userId, projectId);
    }
}
