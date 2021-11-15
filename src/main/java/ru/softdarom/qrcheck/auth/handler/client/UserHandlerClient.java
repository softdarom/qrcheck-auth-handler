package ru.softdarom.qrcheck.auth.handler.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.softdarom.qrcheck.auth.handler.model.dto.ProviderUserDto;

@FeignClient(name = "user-handler", url = "${outbound.feign.user-handler.host}")
public interface UserHandlerClient {

    @PostMapping("/users")
    ResponseEntity<ProviderUserDto> save(@RequestHeader("X-ApiKey-Authorization") String apiKey,
                                         @RequestBody ProviderUserDto request);
}