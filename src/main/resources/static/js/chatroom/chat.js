document.addEventListener('DOMContentLoaded', function() {
    let page = 0;
    const size = 50; // 페이지당 메시지 수
    let loadingMessages = false; // 메시지 로딩 상태를 추적

    const ws = new SockJS('/app/chat');
    const stompClient = Stomp.over(ws);

    stompClient.connect({}, (frame) => {
        console.log("Connected: ", frame);

        // 3. 채팅방 메시지 구독
        stompClient.subscribe(`/sub/chat-rooms/${chatRoomId}`, (message) => {
            const data = JSON.parse(message.body);
            console.log('Received message: ', data);

            // ID가 제대로 포함되어 있는지 확인
            console.log("서버에서 받은 memberId:", data.memberId);
            console.log("내 memberId:", memberId);

            // 메시지를 채팅 창에 추가
            const messagesElement = document.getElementById('messages');
            const messageElement = createMessageElement(data);
            messagesElement.appendChild(messageElement);

            // 스크롤을 맨 아래로 이동
            messagesElement.scrollTop = messagesElement.scrollHeight;

            // 마지막 메시지 내역 저장
            localStorage.setItem(`lastMessage_${chatRoomId}`, JSON.stringify(data));
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

            // 마지막 메시지 내역 저장
            localStorage.setItem(`lastMessage_${chatRoomId}`, JSON.stringify(chatMessage));
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
        messagesElement.scrollTop = messagesElement.scrollHeight;
    };

    // 스크롤 이벤트 리스너 설정
    const messagesElement = document.getElementById('messages');
    messagesElement.addEventListener('scroll', () => {
        if (messagesElement.scrollTop === 0 && !loadingMessages) {
            loadingMessages = true;
            page++;

            // AJAX 요청을 통해 이전 메시지 로드
            const xhr = new XMLHttpRequest();
            xhr.open('GET', `/chat-rooms/${chatRoomId}/messages?page=${page}&size=${size}`, true);
            xhr.setRequestHeader('Content-Type', 'application/json');

            xhr.onload = function() {
                if (xhr.status === 200) {
                    const messages = JSON.parse(xhr.responseText);
                    const fragment = document.createDocumentFragment();
                    messages.forEach(data => {
                        const messageElement = createMessageElement(data);
                        fragment.appendChild(messageElement);
                    });
                    messagesElement.insertBefore(fragment, messagesElement.firstChild);
                    loadingMessages = false;
                } else {
                    console.error('Failed to load messages');
                    loadingMessages = false;
                }
            };

            xhr.onerror = function() {
                console.error('Request error');
                loadingMessages = false;
            };

            xhr.send();
        }
    });

    // 메시지 요소를 생성하는 함수
    function createMessageElement(data) {
        const messageElement = document.createElement('div');
        messageElement.className = 'message';
        if (data.memberId === memberId) {
            messageElement.style.textAlign = 'right';
        } else {
            messageElement.classList.add('other-message');
        }

        const nicknameElement = document.createElement('p');
        nicknameElement.className = 'member-name';
        nicknameElement.textContent = data.nickname;

        const contentElement = document.createElement('div');
        contentElement.className = 'content';
        contentElement.textContent = data.message;

        const timeElement = document.createElement('p');
        timeElement.className = 'sent-time';

        // const utcDate = new Date(data.now);
        // const localDate = new Date(utcDate.getTime() - (utcDate.getTimezoneOffset() * 60000));
        // timeElement.textContent = localDate.toLocaleTimeString();
        const utcDate = new Date(data.now);
        timeElement.textContent = utcDate.toLocaleString('ko-KR', {
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        });

        messageElement.appendChild(nicknameElement);
        messageElement.appendChild(contentElement);
        messageElement.appendChild(timeElement);
        return messageElement;
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
