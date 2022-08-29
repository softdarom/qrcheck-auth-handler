package ru.softdarom.qrcheck.auth.handler.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.softdarom.qrcheck.auth.handler.model.dto.ProviderUserDto;

import java.util.UUID;

@FeignClient(name = "user-handler", url = "${outbound.feign.user-handler.host}")
public interface UserHandlerClient {

    @GetMapping("/users/{id}")
    ResponseEntity<ProviderUserDto> get(@RequestHeader("X-ApiKey-Authorization") UUID apiKey,
                                        @PathVariable("id") Long id);

    @PostMapping("/users")
    ResponseEntity<ProviderUserDto> save(@RequestHeader("X-ApiKey-Authorization") UUID apiKey,
                                         @RequestBody ProviderUserDto request);
}