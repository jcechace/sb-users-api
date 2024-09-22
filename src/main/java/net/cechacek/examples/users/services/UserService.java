package net.cechacek.examples.users.services;

import lombok.RequiredArgsConstructor;
import net.cechacek.examples.users.api.dto.UserRequest;
import net.cechacek.examples.users.domain.User;
import net.cechacek.examples.users.persistence.access.UserRepository;
import net.cechacek.examples.users.util.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public List<User> findAll() {
        var entities = userRepository.findAll();
        return userMapper.toModel(entities);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id).map(userMapper::toModel);
    }

    public User create(User user) {
        var entity = userMapper.toEntity(user);
        entity = userRepository.save(entity);
        return userMapper.toModel(entity);
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }
}
