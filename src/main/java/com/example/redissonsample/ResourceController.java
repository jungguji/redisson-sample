package com.example.redissonsample;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@RestController
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;
    private final RedissonClient redissonClient;

    @PostMapping("{userId}/consume")
    public ResponseEntity<String> consumeResource(@PathVariable("userId") int userId) {
        String lockName = "user:" + userId + ":resource:lock";

        RLock lock = redissonClient.getLock(lockName);
        int remainingResources = 0;
        try {
            boolean isLocked = lock.tryLock(3, 10, TimeUnit.SECONDS);

            if (isLocked) {
                remainingResources = resourceService.consumeResource(userId);
            }
        } catch (RuntimeException | InterruptedException e) {
            throw new RuntimeException("Lock 획득 실패", e);
        } finally {
            if (lock.isHeldByCurrentThread()) { // 락 소유 여부 확인
                lock.unlock();
            }
        }

        return ResponseEntity.ok("자원 소비. 남은 자원: " + remainingResources);
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