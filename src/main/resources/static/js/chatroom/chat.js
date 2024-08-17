const chatRoomId = /*[[${chatRoomId}]]*/ '1'; // 서버에서 전달된 채팅방 ID
// const chatRoomName = "[[${chatRoomName}]]" ;// 채팅방 제목
// const memberCount = [[${memberCount}]] ;// 채팅방 참여 인원수
// const chatRoomCategories = [[${chatRoomCategories}]]; // 카테고리
const memberId = /*[[${memberId}]]*/ '123';
const username = /*[[${username}]]*/ 'user1'; // 서버에서 전달된 사용자 이름

document.addEventListener('DOMContentLoaded', function() {
    // 1. SockJS 객체 생성 후 STOMP 연결 요청
    const ws = new SockJS('/app/chat');
    const stompClient = Stomp.over(ws);

    // 2. 서버와 연결되면 호출되는 connect 핸들러
    stompClient.connect({}, (frame) => {
        console.log("Connected: ", frame);

        // 3. 구독 신청
        stompClient.subscribe(`/sub/chat-rooms/${chatRoomId}`, (message) => {
            const data = JSON.parse(message.body);
            console.log('Received message: ', data);

            // 메시지를 채팅 창에 추가
            const messagesElement = document.getElementById('messages');
            const messageElement = document.createElement('div');
            messageElement.className = 'message';

            const nicknameElement = document.createElement('p');
            nicknameElement.className = 'member-name';
            nicknameElement.textContent = data.nickname;

            const contentElement = document.createElement('div');
            contentElement.className = 'content';
            contentElement.textContent = data.message;

            const timeElement = document.createElement('p');
            timeElement.className = 'sent-time';

            const utcDate = new Date(data.now);
            const localDate = new Date(utcDate.getTime() - (utcDate.getTimezoneOffset() * 60000));
            timeElement.textContent = new Date(data.now).toLocaleTimeString();

            if (data.memberId === memberId) {
                messageElement.style.textAlign = 'right';
                contentElement.classList.add('chat-right');
            } else {
                contentElement.classList.add('chat-left');
            }

            messageElement.appendChild(nicknameElement);
            messageElement.appendChild(contentElement);
            messageElement.appendChild(timeElement);
            messagesElement.appendChild(messageElement);

            // 스크롤을 맨 아래로 이동
            messagesElement.scrollTop = messagesElement.scrollHeight;
        });

        // member 입장 메시지 구독 신청 (새로운 member가 들어왔을 때)
        stompClient.subscribe(`/sub/${chatRoomId}/join`, (message) => {
            const data = JSON.parse(message.body);
            console.log('Member joined: ', data.nickname);
            addMemberJoinMessage(data.nickname);
        });
    });

    // 메시지 전송
    const sendMessage = () => {
        console.log('Send button clicked');  // 버튼 클릭 로그
        const messageInput = document.getElementById('message-input');
        const messageContent = messageInput.innerText.trim();
        if (messageContent && stompClient) {
            const chatMessage = {
                chatRoomId: chatRoomId,
                nickname: username,
                memberId: memberId,
                message: messageContent,
                now: new Date().toISOString()
            };
            console.log('Sending message:', chatMessage);  // 메시지 전송 로그
            stompClient.send(`/pub/chat-rooms/chat/${chatRoomId}`, {}, JSON.stringify(chatMessage));
            messageInput.innerText = '';  // 전송 후 내용 초기화

            // 스크롤을 맨 아래로 이동
            const messagesElement = document.getElementById('messages');
            messagesElement.scrollTop = messagesElement.scrollHeight;
        }
    };

    // member 입장 시 메시지 추가
    const addMemberJoinMessage = (nickname) => {
        const messagesElement = document.getElementById('messages');
        const messageElement = document.createElement('div');
        messageElement.className = 'member-join-message';
        messageElement.textContent = `${nickname}님이 들어왔습니다.`;
        messagesElement.appendChild(messageElement);

        // 스크롤을 맨 아래로 이동
        messagesElement.scrollTop = messageElement.scrollHeight;
    }

    // 버튼 클릭 이벤트 리스너 설정
    document.getElementById('send-button').addEventListener('click', sendMessage);

    // Enter 키 이벤트 리스너 설정
    document.getElementById('message-input').addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            sendMessage();
            event.preventDefault();  // 폼 제출 방지
        }
    });

    // beforeunload 이벤트 리스너 추가
    window.addEventListener('beforeunload', (event) => {
        if (stompClient) {
            stompClient.disconnect(() => {
                console.log('Disconnected');
            });
        }
    });
});
