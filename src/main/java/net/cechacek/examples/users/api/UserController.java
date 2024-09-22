package net.cechacek.examples.users.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.cechacek.examples.users.api.dto.UserRequest;
import net.cechacek.examples.users.api.dto.UserResponse;
import net.cechacek.examples.users.services.UserService;
import net.cechacek.examples.users.util.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController()
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserResponse>> list() {
        var users = userService.findAll();
        var resp = userMapper.toResponse(users);
        return ResponseEntity.ok(resp);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable long id) {
        return userService.findById(id)
                .map(userMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserResponse> post(@RequestBody @Valid UserRequest request) {
        var user = userMapper.toModel(request);
        user = userService.create(user);
        var resp = userMapper.toResponse(user);
        return ResponseEntity.created(getUserUri(user.getId())).body(resp);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private URI getUserUri(long userId) {
        return MvcUriComponentsBuilder
                .fromMethodCall(on(UserController.class).get(userId))
                .buildAndExpand()
                .encode().toUri();
    }
}
