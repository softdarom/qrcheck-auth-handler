package ru.softdarom.qrcheck.auth.handler.rest.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.softdarom.qrcheck.auth.handler.config.swagger.annotation.*;
import ru.softdarom.qrcheck.auth.handler.model.dto.request.IncomingApiKeyRequest;
import ru.softdarom.qrcheck.auth.handler.model.dto.request.OutgoingApiKeyRequest;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.IncomingApiKeyResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.OutgoingApiKeyResponse;
import ru.softdarom.qrcheck.auth.handler.service.ApiKeyService;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;

@Tag(name = "ApiKeys", description = "Контроллер для управления api key'ями")
@RestController
@RequestMapping("/apiKeys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @Autowired
    ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @ApiGetIncoming
    @GetMapping("/incoming")
    public ResponseEntity<Set<UUID>> getIncoming(@RequestParam("serviceName") String serviceName) {
        return ResponseEntity.ok(apiKeyService.getIncoming(serviceName));
    }

    @ApiGetOutgoing
    @GetMapping("/outgoing")
    public ResponseEntity<UUID> getOutgoing(@RequestParam("serviceName") String serviceName) {
        return ResponseEntity.ok(apiKeyService.getOutgoing(serviceName));
    }

    @ApiSaveIncoming
    @PostMapping("/incoming")
    public ResponseEntity<IncomingApiKeyResponse> saveIncoming(@Valid @RequestBody IncomingApiKeyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(apiKeyService.saveIncoming(request));
    }

    @ApiSaveOutgoing
    @PostMapping("/outgoing")
    public ResponseEntity<OutgoingApiKeyResponse> saveOutgoing(@Valid @RequestBody OutgoingApiKeyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(apiKeyService.saveOutgoing(request));
    }

    @ApiDeleteIncoming
    @DeleteMapping("/incoming")
    public ResponseEntity<Void> deleteIncoming(@Valid @RequestBody IncomingApiKeyRequest request) {
        apiKeyService.removeApiKeys(request.getServiceName(), request.getIncoming());
        return ResponseEntity.accepted().build();
    }

    @ApiDeletOutgoing
    @DeleteMapping("/outgoing")
    public ResponseEntity<Void> deleteOutgoing(@Valid @RequestBody OutgoingApiKeyRequest request) {
        apiKeyService.removeApiKeys(request.getServiceName(), Set.of(request.getOutgoing()));
        return ResponseEntity.accepted().build();
    }
}