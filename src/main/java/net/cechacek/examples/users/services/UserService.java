package net.cechacek.examples.users.services;

import lombok.RequiredArgsConstructor;
import net.cechacek.examples.users.api.dto.UserRequest;
import net.cechacek.examples.users.domain.User;
import net.cechacek.examples.users.persistence.access.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll()
                .stream()
                .map(User::fromEntity)
                .toList();
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id)
                .map(User::fromEntity);
    }

    public User create(UserRequest request) {
        var entity = request.toEntity();
        entity = userRepository.save(entity);
        return User.fromEntity(entity);
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }
}
