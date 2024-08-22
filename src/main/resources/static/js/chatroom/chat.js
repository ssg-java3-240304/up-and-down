document.addEventListener('DOMContentLoaded', function() {
    let page = 0;
    const size = 50; // 페이지당 메시지 수
    let loadingMessages = false; // 메시지 로딩 상태를 추적
    let lastMessageDate = null; // lastMessageDate 변수를 정의하고 초기화합니다.

    const ws = new SockJS('/app/chat');
    const stompClient = Stomp.over(ws);

    stompClient.connect({}, (frame) => {
        console.log("Connected: ", frame);

        // 2. 채팅방 메시지 구독
        stompClient.subscribe(`/sub/chat-rooms/${chatRoomId}`, (message) => {
            const data = JSON.parse(message.body);
            console.log('Received message: ', data);

            // 메시지를 채팅 창에 추가
            const messagesElement = document.getElementById('messages');
            const messageElement = createMessageElement(data);
            messagesElement.appendChild(messageElement);

            // 스크롤을 맨 아래로 이동
            messagesElement.scrollTop = messagesElement.scrollHeight;

            // 마지막 메시지 내역 저장
            localStorage.setItem(`lastMessage_${chatRoomId}`, JSON.stringify(data));
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
                nickname: nickname,
                memberId: memberId,
                message: messageContent,
                now: new Date().toISOString()
            };
            console.log('Sending message:', chatMessage);  // 메시지 전송 로그
            stompClient.send(`/pub/chat-rooms/chat/${chatRoomId}`, {}, JSON.stringify(chatMessage));
            messageInput.innerText = '';  // 전송 후 내용 초기화

            // 스크롤을 맨 아래로 이동
            const messagesElement = document.getElementById('messages');
            setTimeout(() => {
                messagesElement.scrollTop = messagesElement.scrollHeight;
            }, 100); // 잠시 지연 후 스크롤 이동

            // 마지막 메시지 내역 저장
            localStorage.setItem(`lastMessage_${chatRoomId}`, JSON.stringify(chatMessage));
        }
    };

    // 페이지 로드 시 스크롤을 맨 아래로 이동
    window.addEventListener('load', () => {
        const messagesElement = document.getElementById('messages');
        messagesElement.scrollTop = messagesElement.scrollHeight;
    });

    // 스크롤 이벤트 리스너 설정
    const messagesElement = document.getElementById('messages');
    messagesElement.addEventListener('scroll', () => {
        if (messagesElement.scrollTop === 0 && !loadingMessages) {
            loadingMessages = true;
            page++;

            // AJAX 요청을 통해 이전 메시지 로드
            const xhr = new XMLHttpRequest();
            xhr.open('GET', `/chat-rooms/${chatRoomId}/messages?page=${page}&size=50`, true);
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

    function createMessageElement(data) {
        const messageElement = document.createElement('div');
        messageElement.className = 'message';

        // 메시지 날짜 처리
        const messageDate = new Date(data.now);
        const formattedDate = messageDate.toLocaleDateString('ko-KR', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit'
        });

        if (formattedDate !== lastMessageDate) {
            const dateSeparator = document.createElement('div');
            dateSeparator.className = 'date-separator';

            const dateParagraph = document.createElement('p');
            dateParagraph.textContent = `${formattedDate} (${messageDate.toLocaleDateString('ko-KR', { weekday: 'long' })})`;

            dateSeparator.appendChild(dateParagraph);
            messageElement.appendChild(dateSeparator);

            lastMessageDate = formattedDate;
        }

        if (data.nickname === nickname) {
            messageElement.classList.add('sent-message');
        } else {
            messageElement.classList.add('received-message');
        }

        const nicknameElement = document.createElement('p');
        nicknameElement.className = 'member-name';
        nicknameElement.textContent = data.nickname;

        const contentElement = document.createElement('div');
        contentElement.className = 'content';
        contentElement.textContent = data.message;

        const timeElement = document.createElement('p');
        timeElement.className = 'sent-time';
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
            event.preventDefault();  // 폼 제출 방지
            if (event.isComposing) {
                return;
            }
            sendMessage();
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