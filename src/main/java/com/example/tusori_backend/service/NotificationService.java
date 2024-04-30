package com.example.tusori_backend.service;

import com.example.tusori_backend.domain.dto.response.NotificationResponse;
import com.example.tusori_backend.domain.entity.Notification;
import com.example.tusori_backend.domain.entity.User;
import com.example.tusori_backend.repository.EmitterRepository;
import com.example.tusori_backend.repository.NotificationRepository;
import com.example.tusori_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.time.*;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private static final Long DEFAULT_TIMEOUT = 600L * 1000 * 60;

    @Transactional
    public SseEmitter subscribe(int userId) {
        SseEmitter emitter = createEmitter(userId);
        sendToClient(userId, "EventStream Created. [userId=" + userId + "]", "sse 접속 성공");
        return emitter;
    }

    @Transactional
    public List<NotificationResponse> getNotificationHistory(int userId) {
    List<Notification> notifications = notificationRepository.findByUserUserId(userId);
        return notifications.stream()
                .sorted(Comparator.comparing(Notification::getNotificationId).reversed())
                .map(notification ->
                        new NotificationResponse(notification.getContent(),
                                notification.isRead(),
                                notification.getCreatedAt().toString()))
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificationResponse notify(int userId, String notificationName) {
        User user = userRepository.findByUserId(userId);
        String comment1 = "거래 체결 요청이 완료되었습니다.";
        String comment2 = "거래 체결이 완료되었습니다.";
        String content1 = "[" + notificationName + "] " + comment1;
        String content2 = "[" + notificationName + "] " + comment2;

        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        LocalTime startTime = LocalTime.of(9, 0); // 오전 9시
        LocalTime endTime = LocalTime.of(15, 30); // 오후 3시 30분

        Notification notification1 = createNotification(content1, user, today);
        sendNotificationToClient(userId, notification1);

        // 현재 시간 오전 9시 ~ 오후 3시반
        if (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
            LocalDateTime nextTime = LocalDateTime.now().plusSeconds(10); // 10초 뒤
            scheduleNextNotification(userId, nextTime, content2, today);
        } else {
            LocalDateTime nextTime = LocalDateTime.of(today.plusDays(1), LocalTime.of(9, 0)); // 다음날 오전 9시
            scheduleNextNotification(userId, nextTime, content2, today);
        }

        return new NotificationResponse(notification1.getContent(), notification1.isRead(), notification1.getCreatedAt().toString());
    }

    private void scheduleNextNotification(int userId, LocalDateTime nextTime, String content2, LocalDate today) {
        Instant instant = nextTime.atZone(ZoneId.systemDefault()).toInstant();

        TaskScheduler scheduler = new ConcurrentTaskScheduler();
        scheduler.schedule(() -> {
            Notification notification2 = createNotification(content2, userRepository.findByUserId(userId), today);
            sendNotificationToClient(userId, notification2);
        }, Date.from(instant));
    }

    private void sendToClient(int userId, Object data, String comment) {
        SseEmitter emitter = emitterRepository.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(userId))
                        .name("sse")
                        .data(data)
                        .comment(comment));
            } catch (IOException e) {
                emitterRepository.deleteById(userId);
                emitter.completeWithError(e);
            }
        }
    }

    private SseEmitter createEmitter(int userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(userId));
        emitter.onTimeout(() -> emitterRepository.deleteById(userId));
        return emitter;
    }

    private Notification createNotification(String content, User user, LocalDate createdAt) {
        Notification notification = Notification.builder()
                .content(content)
                .createdAt(createdAt)
                .isRead(false)
                .user(user)
                .build();
        return notificationRepository.save(notification);
    }

    private void sendNotificationToClient(int userId, Notification notification) {
        String content = notification.getContent();
        sendToClient(userId, new NotificationResponse(content, notification.isRead(), notification.getCreatedAt().toString()), content);
    }
}
