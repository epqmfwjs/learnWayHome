package com.learnway.consult.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Component
public class SignalingController {

	//roomId 는 상담사 pk 값으로 정함
	//camKey 는 로그인사용자 아이디(String)로함 <상담사 아이디 와 멤버 아이디> 
	
    // 방(Room)별로 참여자 수를 관리하기 위한 맵
    private Map<String, Set<String>> roomParticipants = new HashMap<>();
    
    // offer 정보를 주고 받기 위한 websocket
    @MessageMapping("/peer/offer/{camKey}/{roomId}")
    @SendTo("/topic/peer/offer/{camKey}/{roomId}")
    public String PeerHandleOffer(@Payload String offer,
                                  @DestinationVariable(value = "roomId") String roomId,
                                  @DestinationVariable(value = "camKey") String camKey) {
        System.out.println("1");
        System.out.println("1번의 roomId" + roomId);
        log.info("[OFFER] {} : {}", camKey, offer);
        return offer;
    }

    // iceCandidate 정보를 주고 받기 위한 webSocket
    @MessageMapping("/peer/iceCandidate/{camKey}/{roomId}")
    @SendTo("/topic/peer/iceCandidate/{camKey}/{roomId}")
    public String PeerHandleIceCandidate(@Payload String candidate,
                                         @DestinationVariable(value = "roomId") String roomId,
                                         @DestinationVariable(value = "camKey") String camKey) {
        System.out.println("2");
        System.out.println("2번의 roomId" + roomId);
        log.info("[ICECANDIDATE] {} : {}", camKey, candidate);
        return candidate;
    }

    // peer answer 정보를 주고 받기 위한 webSocket
    @MessageMapping("/peer/answer/{camKey}/{roomId}")
    @SendTo("/topic/peer/answer/{camKey}/{roomId}")
    public String PeerHandleAnswer(@Payload String answer,
                                   @DestinationVariable(value = "roomId") String roomId,
                                   @DestinationVariable(value = "camKey") String camKey) {
        System.out.println("3");
        System.out.println("3번의 roomId" + roomId);
        log.info("[ANSWER] {} : {}", camKey, answer);
        return answer;
    }

    // camKey를 받기위해 신호를 보내는 webSocket
    @MessageMapping("/call/key")
    @SendTo("/topic/call/key")
    public String callKey(@Payload String message) {
        System.out.println("4");
        log.info("[Key] : {}", message);
        return message;
    }

    // 자신의 camKey를 모든 연결된 세션에 보내는 webSocket
    @MessageMapping("/send/key")
    @SendTo("/topic/send/key")
    public String sendKey(@Payload String message) {
        System.out.println("5");
        log.info("[Send Key] : {}", message);
        return message;
    }

    // 방(Room)에 참여하는 인원을 추적하고, 제한 조건을 설정할 메서드
    @MessageMapping("/join/room/{roomId}/{camKey}")
    @SendTo("/topic/join/room/{roomId}/{camKey}")
    public String joinRoom(@DestinationVariable(value = "roomId") String roomId,
                           @DestinationVariable(value = "camKey") String camKey) {
        log.info("여기들어와?? : roomId={}, camKey={}", roomId, camKey);
        System.out.println("join room 의 roomId : " + roomId);

        // 해당 camKey에 대한 참여자 Set을 가져옴
        roomParticipants.putIfAbsent(roomId, new HashSet<>());
        Set<String> participants = roomParticipants.get(roomId);
        log.info("현재 인원 수 : {}", participants.size());

        // 방(Room)에 이미 camKey가 있는지 확인
        if (participants.contains(camKey)) {
            log.info("이미 참여 중인 사용자: {}", camKey);
            return "successfully";
        }
 
        // 방(Room)에 참여 중인 인원 수 확인 현재 참여인원을 2명으로 제한
        if (participants.size() >= 2) {
            // 방이 가득 찼음을 클라이언트에게 알립니다.
            return "full";
        } else {
            // 방(Room)에 참여자를 추가하고, 성공 메시지를 반환
            participants.add(camKey);
            System.out.println("입장 캠키 "+camKey);
            return "successfully";
        }
    }

    // 방(Room)에서 사용자가 나가는 메서드
    @MessageMapping("/leave/room/{roomId}/{camKey}")
    @SendTo("/topic/leave/room/{roomId}/{camKey}")
    public String leaveRoom(@DestinationVariable(value = "roomId") String roomId,
                            @DestinationVariable(value = "camKey") String camKey) {
        log.info("leaveRoom: roomId={}, camKey={}", roomId, camKey);
        Set<String> participants = roomParticipants.get(roomId);
        if (participants != null) {
            participants.remove(camKey);
            System.out.println("나가기 캠키 "+camKey);
            log.info("참여자가 방 나감: {}", camKey);
            if (participants.isEmpty()) {
                roomParticipants.remove(roomId);
                log.info("방 참여인원없음 방삭제 : {}", roomId);
            }
            return "left";
        } else {
            log.info("해당 방 존재 ㄴㄴ: {}", roomId);
            return "not_found";
        }
    }
}
