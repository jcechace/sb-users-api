package net.cechacek.examples.users.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.cechacek.examples.users.domain.Access;
import net.cechacek.examples.users.api.dto.AccessRequest;
import net.cechacek.examples.users.persistence.AccessEntity;
import net.cechacek.examples.users.persistence.access.AccessRepository;
import net.cechacek.examples.users.persistence.access.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(Transactional.TxType.SUPPORTS)
public class AccessService {

    private final AccessRepository accessRepository;
    private final UserRepository userRepository;

    public List<Access> findAll() {
        return accessRepository.findAll()
                .stream()
                .map(Access::fromEntity)
                .toList();
    }

    public List<Access> findAllByUser(long userId) {
        return accessRepository.findAllByUserId(userId)
                .stream()
                .map(Access::fromEntity)
                .toList();
    }

    public List<Access> findAllByProject(String projectId) {
        return accessRepository.findAllByProjectId(projectId)
                .stream()
                .map(Access::fromEntity)
                .toList();
    }

    public Optional<Access> findByUserAndProject(long userId, String projectId) {
        return accessRepository.findByUserIdAndProjectId(userId, projectId)
                .map(Access::fromEntity);
    }


    @Transactional(Transactional.TxType.REQUIRED)
    public Optional<Access> update(long userId, String projectId, AccessRequest request) {
        return accessRepository
                .findByUserIdAndProjectId(userId, projectId)
                .map(e -> {
                    e.setProjectName(request.getProjectName());
                    return e;})
                .map(accessRepository::save)
                .map(Access::fromEntity);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Access create(long userId, String projectId, AccessRequest request) {
        var user = userRepository.getReferenceById(userId);
        var entity = new AccessEntity(null, user, projectId, request.getProjectName());
        entity = accessRepository.save(entity);

        return Access.fromEntity(entity);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteByUserAndProject(long userId, String projectId) {
        accessRepository.deleteByUserIdAndProjectId(userId, projectId);
    }
}
