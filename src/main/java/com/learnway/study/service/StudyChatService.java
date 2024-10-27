package com.learnway.study.service;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnway.member.domain.Member;
import com.learnway.member.domain.MemberRepository;
import com.learnway.study.domain.ChatMessage;
import com.learnway.study.domain.ChatMessageRepository;
import com.learnway.study.domain.ChatRoom;
import com.learnway.study.domain.ChatRoomMember;
import com.learnway.study.domain.ChatRoomMemberRepository;
import com.learnway.study.domain.ChatRoomRepository;
import com.learnway.study.domain.Study;
import com.learnway.study.domain.StudyChatRepository;
import com.learnway.study.dto.ChatRoomDto;
import com.learnway.study.dto.ChatRoomMemberDto;

@Service
public class StudyChatService {

	@Autowired
	private StudyChatRepository studyChatRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private ChatRoomMemberRepository chatRoomMemberRepository;
	@Autowired
	private ChatRoomRepository chatRoomRepository;
	@Autowired
	private ChatMessageRepository chatMessageRepository;
	
	
	//postId로 ChatRoomId 조회
	public List<ChatRoom> chatRoomId(int postId) {
	
		return studyChatRepository.findByStudyPostid(postId);
	}
	
	//채팅방 멤버이름값
	public String MemberName(Principal principal) {
		Member member = memberRepository.findByMemberId(principal.getName())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + principal.getName()));
		return member.getMemberName();
	}
	
	//채팅방 리스트 조회
	public List<ChatRoomMember> chatList(Principal principal) {
		
		List<ChatRoomMember> list = chatRoomMemberRepository.findByMember_MemberId(
				principal.getName());
		return list;
	}
	
	//로그인사용자가 방장인 채팅방 조회
	public List<ChatRoom> myChatList(Principal principal) {
		
		List<ChatRoom> list = chatRoomRepository.findByMember_MemberId(
				principal.getName());
		return list;
	}
	
//	채팅방 참여 메서드
	public ChatRoomMember joinChatRoom(ChatRoomDto dto,Principal principal) {
		
		//현재 로그인사용자정보 조회
		Member member = memberRepository.findByMemberId(principal.getName())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + principal.getName()));
		
		//방번호로 ChatRoom객체생성
		ChatRoom chatRoom = studyChatRepository.findById(dto.getRoomId()).get();
		
		
		ChatRoomMember room = ChatRoomMember.builder().member(member)
				  				.chatRoom(chatRoom).hasEntered(true).build();
		
		return chatRoomMemberRepository.save(room);
		
	}
	
	
	//채팅방 생성 메서드
	public ChatRoom chatRoomCreate(ChatRoomDto dto,Study study,Principal principal) {
		
		Member member = memberRepository.findByMemberId(principal.getName())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + principal.getName()));
		
		
		ChatRoom room = ChatRoom.builder().roomname(dto.getRoomname())
				.study(study).member(member).build();
		System.out.println(room.getRoomname() + "룸이름");
		
		return studyChatRepository.save(ChatRoom.builder().roomname(dto.getRoomname())
				.study(study).member(member).build());
	}
	
	//채팅방 제목 수정
	public ChatRoom chatRoomUpdate(ChatRoomDto dto,Study study,Principal principal,int postId) {
		
		Member member = memberRepository.findByMemberId(principal.getName())
				.orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + principal.getName()));
		int roomId = 0;
		List<ChatRoom> list = studyChatRepository.findByStudyPostid(postId);
		for(ChatRoom a : list) {
			roomId = a.getChatroomid();
		}
		
		return studyChatRepository.save(ChatRoom.builder().roomname(dto.getRoomname())
				.chatroomid(roomId).study(study).member(member).build());
	}
	
	
	
	//채팅 보관 메서드
	public ChatMessage storechat(ChatRoomDto dto,Principal principal) {
		
		//멤버값 넣을예정 임시로 1로지정
		Member member = memberRepository.findByMemberId(principal.getName())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + principal.getName()));
		
		ChatRoom chatRoom = studyChatRepository.findById(dto.getRoomId())
				.orElseThrow(()-> new IllegalArgumentException("채팅방아이디  " + dto.getRoomId()));
		
		ChatMessage msg = ChatMessage.builder().msg(dto.getMessage()).member(member)
										       .chatroom(chatRoom)
											   .datetime(dto.getDatetime()).build();
		return msg;
	}
	
	//채팅방 참여자 리스트(방장)
	public ChatRoom chatListHost(ChatRoomDto dto) {
		
		return chatRoomRepository.findById(dto.getRoomId()).get();
	}
	//채팅방 참여자 리스트(멤버)
	public List<ChatRoomMember> chatListGuest(ChatRoomDto dto) {
		
		return chatRoomMemberRepository.findByChatRoom_Chatroomid(dto.getRoomId());
	}
	
	public Map<String, String> getUserImagesForRoom(int roomId) {
        Map<String, String> userImages = new HashMap<>();
        
        // 방장 정보 가져오기
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow();
        userImages.put(chatRoom.getMember().getMemberName(), chatRoom.getMember().getMemberImage());
        
        // 참여자 정보 가져오기
        List<ChatRoomMember> members = chatRoomMemberRepository.findByChatRoom_Chatroomid(roomId);
        for (ChatRoomMember member : members) {
            userImages.put(member.getMember().getMemberName(), member.getMember().getMemberImage());
            System.out.println(member.getMember().getMemberName() + ": 멤버 이름");
            System.out.println(member.getMember().getMemberImage() + ": 멤버 이미지");
            
        }
        
        return userImages;
    }

	
	//이전채팅 가져오기
	public List<ChatMessage> chatMessageList(ChatRoomDto dto) {
		return chatMessageRepository.findByChatroom_Chatroomid(dto.getRoomId());
	}
	
	//채팅방 처음입장 검사 처음입장시 return값 true / 아닐시 false반환
	public boolean chatFirstEnter(ChatRoomDto dto,Principal principal) {
		List<ChatRoomMember> list = chatRoomMemberRepository.findByMember_MemberId(principal.getName());
		 ChatRoomMemberDto chatDto = new ChatRoomMemberDto();
		
		for(ChatRoomMember chat : list) {
			chatDto.setHasEntered(chat.isHasEntered());
		}
		
		if(chatDto.isHasEntered()==false){
			
			return false;
		} else {
			
			Member member = memberRepository.findByMemberId(principal.getName())
		            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + principal.getName()));
			
			ChatRoom chatRoom = studyChatRepository.findById(dto.getRoomId()).get();
			
			
			//여기부터
			List<ChatRoomMember> list1 = chatRoomMemberRepository.findByMember_MemberId(principal.getName());
			
			for(ChatRoomMember a : list1) {
				chatDto.setChatMemId(a.getChatMemId());
			}
			//
			
			
			ChatRoomMember room = ChatRoomMember.builder().member(member).chatMemId(chatDto.getChatMemId())
					  				.chatRoom(chatRoom).hasEntered(false).build();
			
			chatRoomMemberRepository.save(room);
			
			return true;
		}
			
			
	}
	
	
	//채팅방 값에따른 조회값 return
	public List<Integer> searchChatStudy(ChatRoomDto dto) {
		
		 if(dto.getRoomCheck() == 1) {
	            List<Integer> postIds = chatRoomRepository.findAllPostIds();
	            System.out.println("Post IDs: " + postIds);
	            return postIds;
	            }
		 else if(dto.getRoomCheck() == 0) {
			 List<Integer> postIds = chatRoomRepository.findStudyPostIdsWithoutChatRooms();
			 return postIds;
		 }
		 return List.of();
	}
}
