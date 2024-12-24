package com.example.redissonsample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public ResponseEntity<String> consumeResource(int userId) {
        Optional<User> userOpt = resourceRepository.findById(userId);
        try {
            Thread.sleep(100); // 100ms 지연
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        User user = userOpt.get();
        user.consumeResource(1);

        resourceRepository.save(user);
        log.info("User {} 자원 {} 만큼 소비, 남은 자원: {}", userId, user.getRemainingResources());
        return ResponseEntity.ok("자원 소비. 남은 자원: " + user.getRemainingResources());
    }

    public ResponseEntity<List<String>> getResourceStatus() {
        List<User> allUsers = resourceRepository.findAll();
        List<String> statuses = allUsers.stream()
                .map(user -> "User " + user.getId() + " 남은 자원 : " + user.getRemainingResources())
                .collect(Collectors.toList());
        return ResponseEntity.ok(statuses);
    }

    @Transactional(rollbackFor = Exception.class)
    public void init(int id) {
        resourceRepository.save(new User(id));
    }
}
