package com.learnway.study.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.learnway.study.domain.ChatMessage;
import com.learnway.study.domain.ChatMessageRepository;
import com.learnway.study.domain.StudyChatRepository;
import com.learnway.study.dto.ChatRoomDto;
import com.learnway.study.service.StudyChatService;



@Controller
public class StudyChatController {

	@Autowired
	StudyChatRepository studyChatRepository;
	@Autowired
	ChatMessageRepository chatMessageRepository;
	@Autowired
	StudyChatService studyChatService;
	@Autowired
	private  SimpMessagingTemplate template;
	
	
	@GetMapping(value="/createRoom")
	public String createRoom() {
		return "study/createRoom";
	}
	
	// 리스트에 멤버id로 채팅방목록 가져와야함
	@GetMapping(value="/learnway/chat")
	public String chatList(Principal principal,Model model) {

		model.addAttribute("list",studyChatService.chatList(principal));
		model.addAttribute("myList",studyChatService.myChatList(principal));
		return "study/chatList";
	}
	
	//채팅방 첫입장
	@GetMapping(value="/joinRoom")
	public String addRoomTest(ChatRoomDto dto,
			Model model,Principal principal) {
		
		
//		studyChatService.joinChatRoom(dto, principal);
		
		//채팅방 입장
		System.out.println("save");
		
		model.addAttribute("name",principal.getName()); 
		model.addAttribute("roomId",dto.getRoomId()); 
		model.addAttribute("roomName",dto.getRoomname()); 
		//채팅방 저장
		
		
		return "study/mychat";
	}
	
	// 입장되어있는 채팅방 입장
	@GetMapping(value="/enterRoom")
	public String enterRoom(ChatRoomDto dto,
			Model model,Principal principal) {
		
		
		model.addAttribute("name",studyChatService.MemberName(principal)); 
		model.addAttribute("roomId",dto.getRoomId()); 
		model.addAttribute("roomName",dto.getRoomname()); 
		Map<String, String> userImages = studyChatService.getUserImagesForRoom(dto.getRoomId());

		
		//채팅방 멤버리스트 (방장,입잠멤버)
		model.addAttribute("chatListHost",studyChatService.chatListHost(dto));
		model.addAttribute("chatListGuest",studyChatService.chatListGuest(dto));
		model.addAttribute("userImages", userImages);
        model.addAttribute("currentUserName", principal.getName());
		
		model.addAttribute("chatMessageList",studyChatService.chatMessageList(dto));
		
		
		return "study/mychat";
	}
	
	@GetMapping("/chatroom/{roomId}/messages")
	@ResponseBody
	public List<ChatMessage> getChatMessages(@PathVariable("roomId") int roomId) {
	    return chatMessageRepository.findByChatroom_Chatroomid(roomId);
	}
	
	
	//문제정답후 로직
	@PostMapping(value="/joinRoom")
	public String addRoom(@RequestBody ChatRoomDto dto,
			Model model,Principal principal) {
		System.out.println("진입");
		System.out.println(dto.getRoomId()+"룸아이디");
		System.out.println(dto.getPostId()+"게시글아이디");
	
		studyChatService.joinChatRoom(dto, principal);
		//채팅방 입장
		System.out.println("save");
		
	

		return "study/mychat";
	}
	
	
	// 채팅방 입장 출력메세지 (첫입장에만 출력되게 수정예정)
	@MessageMapping(value="/chat/enter")
	public void chatenter(ChatRoomDto dto,Principal principal) {
		
		boolean result = studyChatService.chatFirstEnter(dto,principal);
		
		if(result == true) {
		dto.setName(studyChatService.MemberName(principal));
		dto.setMessage(dto.getName() + "님이 채팅방에 입장하셨습니다.");
		LocalDateTime datetime = LocalDateTime.parse(dto.getDate(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		dto.setDatetime(datetime);
		chatMessageRepository.save(studyChatService.storechat(dto,principal));
		template.convertAndSend("/sub/chat/room/"+dto.getRoomId(),dto);
		}
		
	}
	
	@MessageMapping(value="/chat/message")
	public void message(ChatRoomDto dto,Principal principal) {
		System.out.println(dto.getName() + "보낸사람");
		
		
		LocalDateTime datetime = LocalDateTime.parse(dto.getDate(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		dto.setDatetime(datetime);
		
		System.out.println(dto.getRoomId()+ "채팅방 아이디");
		chatMessageRepository.save(studyChatService.storechat(dto,principal));
		
		System.out.println("채팅 저장됨");
		template.convertAndSend("/sub/chat/room/"+dto.getRoomId(),dto);

	}
	
	
}
