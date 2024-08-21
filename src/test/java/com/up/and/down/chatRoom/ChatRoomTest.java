package com.up.and.down.chatRoom;

import com.up.and.down.chatroom.dto.ShowChatRoomDto;
import com.up.and.down.chatroom.entity.ChatRoom;
import com.up.and.down.chatroom.repository.ChatRoomRepository;
import com.up.and.down.chatroom.service.ChatRoomService;
import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.product.repository.ProductGroupRepository;
import com.up.and.down.user.member.entity.Member;
import com.up.and.down.user.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ChatRoomTest {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRoomService chatRoomService;

    @Test
    @DisplayName("모든 채팅방 조회 ")
    void test() {
        List<ShowChatRoomDto> list = chatRoomService.findAllChatRooms();

        for (ShowChatRoomDto chatRoom : list) {
            System.out.println(chatRoom.toString());
        }
        Assertions.assertThat(list).isNotNull();
    }

    @Test
    @DisplayName("채팅룸 생성 ")
    void test1() {
        ChatRoom chatRoom = ChatRoom.builder()
                .creatorId(3L)
                .description("안녕하세요 축구하는 채팅방입니다.")
                .name("놀사람 구해요")
                .memberIdList(Set.of(3L))
                .build();
        chatRoomRepository.save(chatRoom);
        int count = 1;
        Assertions.assertThat(count).isEqualTo(chatRoom.getMemberCount());

    }





//    @Test
//    @DisplayName("채팅 DTO로 변경해서 사용 ")
//    void testDto() {
//
//        List<ShowChatRoomDto> list = chatRoomService.findAllChatRoomList();
//        for (ShowChatRoomDto dto : list) {
//            System.out.println(dto.toString());
//        }
//        Assertions.assertThat(list).isNotNull();
//    }

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("아이디 조회")
    void test2() {

        Long userId = 1L;
        List<ChatRoom> list = chatRoomRepository.findByMemberIdListContaining(userId);

        for (ChatRoom chatRoom : list) {
            System.out.println(chatRoom.toString());
        }
        Assertions.assertThat(list).isNotNull();
    }

    @Test
    @DisplayName("creatorId에 해당하는 닉네임 가져오기")
    void test3() {

        String nickname = "";
        Long creatorId = 1L;
        Optional<Member> member = memberRepository.findById(creatorId);
        if (member.isPresent()) {
            nickname = member.get().getNickname();
        }
        System.out.println(nickname);
        Assertions.assertThat(nickname).isEqualTo("오행동");

    }
}
