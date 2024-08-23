const $currentRoomMember = document.querySelector(".current-room-member"); // 채팅방 참여 인원
const $contentBody = document.querySelector('.content-body');
const $chatLogBox = document.querySelector('.chat-log-box');
const $chatInputFrm = document.querySelector(".chat-input-frm");
const $chatContent = document.getElementById('chat-content');


document.addEventListener("DOMContentLoaded", function() {
    scrollToBottom();
});

$chatInputFrm.addEventListener("submit", function (event) {
    event.preventDefault();
    sendMessage();
    $chatContent.value = "";
});
// beforeunload 이벤트 리스너 추가
window.addEventListener('beforeunload', function (event) {
    if (stompClient) {
        stompClient.disconnect(() => {
            console.log('Disconnected');
        });
    }
});

// 채팅 화면 스크롤 하단으로 이동
const scrollToBottom = function () {
    $contentBody.scrollTop = $contentBody.scrollHeight;
}

// 채팅방 참여 인원 노출
const updateCurrentMemberNum = function () {

}

// STOMP : 등록
const stompRegister = function () {
    const ws = new SockJS('/app/chat/stomp');
    const client = Stomp.over(ws);

    client.connect({}, (frame) => {
        console.log("Connected: ", frame);

        // 채팅방 메시지 구독
        client.subscribe(`/sub/chatroom/chat`, (message) => {
            const data = JSON.parse(message.body);
            console.log('Parsed message: ', data);

            // 메시지를 채팅 창에 추가
            $chatLogBox.appendChild(createChatElement(data));

            scrollToBottom();

            // 마지막 메시지 내역 저장
            localStorage.setItem(`lastMessage_${chatroomId}`, JSON.stringify(data));
        })
    });

    return client;
}
const stompClient = stompRegister();

// STOMP : 메세지 전송
const sendMessage = function () {
    const msg = $chatContent.value.trim();

    if (msg) {
        const msgData = {
            chatRoomId: chatroomId,
            memberId: memberId,
            nickname: nickname,
            message: msg,
        };

        stompClient.send(`/pub/chatroom/chat`, {}, JSON.stringify(msgData));

        scrollToBottom();

        // 마지막 메시지 내역 저장
        localStorage.setItem(`lastMessage_${chatroomId}`, JSON.stringify(msgData));
    }
}

// chat 요소 생성
const createChatElement = function (msgData) {
    // li 요소 생성
    const chatElement = document.createElement('li');
    chatElement.classList.add('chat');

    // 사용자가 보낸 메시지인지 확인
    if (msgData.memberId === memberId) {
        chatElement.classList.add('mine'); // 자신이 보낸 메시지인 경우
    } else {
        chatElement.classList.add('other'); // 다른 사용자가 보낸 메시지인 경우
    }

    // innerHTML로 요소 내용 설정
    chatElement.innerHTML = `
        <div class="chat-top">
            <p class="username">${msgData.nickname}</p>
            <p class="chat-time">${formatCreatedAtToTimeString(msgData.createdAt)}</p>
        </div>
        <p class="chat-content">${msgData.message}</p>
    `;

    return chatElement; // 완성된 li 요소 반환
}

// createdAt -> 시간 표시로 변경
const formatCreatedAtToTimeString = function (createdAt) {
    // Date 객체로 변환
    const date = new Date(createdAt);

    // 시간을 HH:mm:ss 형식으로 추출
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');

    // 시간 문자열 조합
    return `${hours}:${minutes}:${seconds}`;
}

//     // 스크롤 이벤트 리스너 설정
//     const messagesElement = document.getElementById('messages');
//     messagesElement.addEventListener('scroll', () => {
//         if (messagesElement.scrollTop === 0 && !loadingMessages) {
//             loadingMessages = true;
//             page++;
//
//             // AJAX 요청을 통해 이전 메시지 로드
//             const xhr = new XMLHttpRequest();
//             xhr.open('GET', `/chat-rooms/${chatRoomId}/messages?page=${page}&size=50`, true);
//             xhr.setRequestHeader('Content-Type', 'application/json');
//
//             xhr.onload = function() {
//                 if (xhr.status === 200) {
//                     const messages = JSON.parse(xhr.responseText);
//                     const fragment = document.createDocumentFragment();
//                     messages.forEach(data => {
//                         const messageElement = createMessageElement(data);
//                         fragment.appendChild(messageElement);
//                     });
//                     messagesElement.insertBefore(fragment, messagesElement.firstChild);
//                     loadingMessages = false;
//                 } else {
//                     console.error('Failed to load messages');
//                     loadingMessages = false;
//                 }
//             };
//
//             xhr.onerror = function() {
//                 console.error('Request error');
//                 loadingMessages = false;
//             };
//
//             xhr.send();
//         }
//     });