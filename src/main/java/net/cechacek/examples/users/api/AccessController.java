package net.cechacek.examples.users.api;

import lombok.RequiredArgsConstructor;
import net.cechacek.examples.users.domain.Access;
import net.cechacek.examples.users.api.dto.AccessRequest;
import net.cechacek.examples.users.services.AccessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController()
@RequestMapping(value = "/access")
@RequiredArgsConstructor
public class AccessController {

    private final AccessService accessService;

    @GetMapping
    public ResponseEntity<List<Access>> list() {
        var list = accessService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<List<Access>> listAllByProject(@PathVariable String id) {
        var list = accessService.findAllByProject(id);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<Access>> listAllByUser(@PathVariable long id) {
        var list = accessService.findAllByUser(id);
        return ResponseEntity.ok(list);
    }

    @GetMapping(path = "/users/{userId}/projects/{projectId}")
    public ResponseEntity<Access> get(@PathVariable long userId, @PathVariable String projectId) {
        return accessService.findByUserAndProject(userId, projectId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/users/{userId}/projects/{projectId}")
    public ResponseEntity<Access> put(@PathVariable long userId, @PathVariable String projectId,
                                      @RequestBody(required = false) AccessRequest request) {
        return accessService.update(userId, projectId, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    var resource = accessService.create(userId, projectId, request);
                    var uri = getAccessUri(userId, projectId);
                    return ResponseEntity.created(uri).body(resource);
                });
    }

    @DeleteMapping(path = "/users/{userId}/projects/{projectId}")
    public ResponseEntity<Void> delete(@PathVariable long userId, @PathVariable String projectId) {
        accessService.deleteByUserAndProject(userId, projectId);
        return ResponseEntity.noContent().build();
    }

    private URI getAccessUri(long userId, String projectId) {
        return MvcUriComponentsBuilder
                .fromMethodCall(on(AccessController.class).get(userId, projectId))
                .buildAndExpand()
                .encode().toUri();
    }

}
