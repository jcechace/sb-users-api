package net.cechacek.examples.users.api;

import lombok.RequiredArgsConstructor;
import net.cechacek.examples.users.api.dto.UserRequest;
import net.cechacek.examples.users.domain.User;
import net.cechacek.examples.users.services.AccessService;
import net.cechacek.examples.users.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController()
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AccessService accessService;

    @GetMapping
    public ResponseEntity<List<User>> list() {
        var list = userService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> get(@PathVariable long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserRequest request) {
        var resource = userService.create(request);
        return ResponseEntity
                .created(URI.create(String.valueOf(resource.getId())))
                .body(resource);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
