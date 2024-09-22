package net.cechacek.examples.users.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.cechacek.examples.users.domain.User;
import net.cechacek.examples.users.persistence.access.UserRepository;
import net.cechacek.examples.users.util.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        var entities = userRepository.findAll();
        log.debug("Found {} users", entities.size());
        return userMapper.toModel(entities);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id).map(userMapper::toModel);
    }

    public User create(User user) {
        var entity = userMapper.toEntity(user)
                .withPassword(passwordEncoder.encode(user.getPassword()));

        entity = userRepository.save(entity);
        return userMapper.toModel(entity);
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }
}
