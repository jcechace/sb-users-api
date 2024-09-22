package net.cechacek.examples.users.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.cechacek.examples.users.api.dto.AccessResponse;
import net.cechacek.examples.users.domain.Access;
import net.cechacek.examples.users.api.dto.AccessRequest;
import net.cechacek.examples.users.services.AccessService;
import net.cechacek.examples.users.util.AccessMapper;
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
import java.util.Optional;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController()
@RequestMapping(value = "/access")
@RequiredArgsConstructor
public class AccessController {

    private final AccessService accessService;
    private final AccessMapper accessMapper;

    @GetMapping
    public ResponseEntity<List<AccessResponse>> list() {
        var accessList = accessService.findAll();
        var response = accessMapper.toResponse(accessList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<List<AccessResponse>> listAllByProject(@PathVariable String id) {
        var accessList = accessService.findAllByProject(id);
        var response = accessMapper.toResponse(accessList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<AccessResponse>> listAllByUser(@PathVariable long id) {
        var accessList = accessService.findAllByUser(id);
        var response = accessMapper.toResponse(accessList);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/users/{userId}/projects/{projectId}")
    public ResponseEntity<AccessResponse> get(@PathVariable long userId, @PathVariable String projectId) {
        return accessService.findByUserAndProject(userId, projectId)
                .map(accessMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/users/{userId}/projects/{projectId}")
    public ResponseEntity<AccessResponse> put(@PathVariable long userId, @PathVariable String projectId,
                                              @RequestBody(required = false) @Valid AccessRequest request) {
        return update(userId, projectId, request)
                .or(() -> create(userId, projectId, request))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/users/{userId}/projects/{projectId}")
    public ResponseEntity<Void> delete(@PathVariable long userId, @PathVariable String projectId) {
        accessService.deleteByUserAndProject(userId, projectId);
        return ResponseEntity.noContent().build();
    }

    private Optional<ResponseEntity<AccessResponse>> update(long userId, String projectId, AccessRequest request) {

        return accessService
                .update(userId, projectId, request.getProjectName())
                .map(accessMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    private Optional<ResponseEntity<AccessResponse>> create(long userId, String projectId, AccessRequest request) {
        return accessService
                .create(userId, projectId, request.getProjectName())
                .map(accessMapper::toResponse)
                .map(this::createdResponse);
    }

    private ResponseEntity<AccessResponse> createdResponse(AccessResponse response) {
        var uri = getAccessUri(response.getUserId(), response.getProjectId());
        return ResponseEntity.created(uri).body(response);
    }

    private URI getAccessUri(long userId, String projectId) {
        return MvcUriComponentsBuilder
                .fromMethodCall(on(AccessController.class).get(userId, projectId))
                .buildAndExpand()
                .encode().toUri();
    }

}
