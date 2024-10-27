package com.learnway.consult.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.learnway.consult.service.ConsultantDetails;
import com.learnway.consult.service.ConsultantService;

@RestController
@RequestMapping("/sse")
public class SseController {

    private Map<Long, SseEmitter> loggedInEmitters = new ConcurrentHashMap<>();
    private Map<Long, Queue<String>> notificationQueue = new ConcurrentHashMap<>();

    @Autowired
    private ConsultantService consultantService;

    @GetMapping("/subscribe/{consultantId}")
    public SseEmitter subscribe(@PathVariable("consultantId") Long consultantId,Authentication authentication) {
    	ConsultantDetails consultant = (ConsultantDetails) authentication.getPrincipal();
    	Long sessionId = consultant.getId();
        if (sessionId == null || !sessionId.equals(consultantId)) {
            // 상담사가 로그인하지 않은 상태이거나, 다른 상담사의 ID로 요청이 온 경우 처리
        	System.out.println("상담사가 로그인하지 않은 상태이거나, 다른 상담사의 ID로 요청이 온 경우 처리");
            return null;
        }
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        loggedInEmitters.put(consultantId, emitter);//키값으로 상담사 pk값 벨류로 메세지

        emitter.onCompletion(() -> {
            System.out.println("상담사밀린메세지완료: " + consultantId);
            loggedInEmitters.remove(consultantId);
        });
        emitter.onTimeout(() -> {
            System.out.println("상담사 타임아웃: " + consultantId);
            loggedInEmitters.remove(consultantId);
        });

        // 로그인 시 기존 대기중인 알림들 처리
        Queue<String> notifications = notificationQueue.get(consultantId);
        if (notifications != null) {
            while (!notifications.isEmpty()) {
                try {
                    emitter.send(SseEmitter.event().name("notification").data(notifications.poll()));
                } catch (IOException e) {
                    System.err.println("발송에러 실패 !! : " + e.getMessage());
                }
            }
            notificationQueue.remove(consultantId);
        }

        return emitter;
    }

    public void sendNotificationToConsultant(Long consultantId, String message) {
        if (loggedInEmitters.containsKey(consultantId)) {
        	System.out.println("상담사 알림 보내기");
            SseEmitter emitter = loggedInEmitters.get(consultantId);
            try {
                System.out.println("Sending notification to consultant: " + consultantId);
                emitter.send(SseEmitter.event().name("notification").data(message));
            } catch (IOException e) {
                System.err.println("Error sending notification: " + e.getMessage());
                loggedInEmitters.remove(consultantId);
            }
        } else {
        	System.out.println("상담사 로그아웃상태 맵에 담아놓기");
            // 상담사가 로그아웃 상태일 때 대기열에 저장
            Queue<String> notifications = notificationQueue.getOrDefault(consultantId, new ConcurrentLinkedQueue<>());
            notifications.offer(message);
            notificationQueue.put(consultantId, notifications);
        }
    }
}
