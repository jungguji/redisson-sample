package com.example.redissonsample;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping("{userId}/consume")
    public ResponseEntity<String> consumeResource(@PathVariable("userId") int userId) {
        return resourceService.consumeResource(userId);
    }

    @GetMapping("/status")
    public ResponseEntity<?> getResourceStatus() {
        return resourceService.getResourceStatus();
    }

    @PostMapping("{userId}")
    public ResponseEntity<String> init(@PathVariable("userId") int userId) {
        resourceService.init(userId);
        return ResponseEntity.ok("유저 자원 초기화");
    }
}