const $contentBody = document.querySelector('.content-body');
const $chatLogBox = document.querySelector('.chat-log-box');
const $chatInputFrm = document.querySelector(".chat-input-frm");
const $chatContent = document.getElementById('chat-content');

document.addEventListener("DOMContentLoaded", function() {
    displayChatLog();
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

// 채팅 로그 출력
const displayChatLog = function () {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `/app/chatroom/chat/data/${chatroomId}`, true);
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.onload = function () {
        if (xhr.status === 200) {  // 성공적으로 응답을 받았을 경우
            console.log("response success!!!");
            const messages = JSON.parse(xhr.responseText); // JSON 응답을 객체로 변환
            console.log(messages);

            // 마지맞 메시지 날짜 저장
            for (const message of messages) {
                const messageDate = formatCreatedAtToDateString(message.createdAt);

                if (lastDate !== messageDate) {
                    displayDate(messageDate);
                    lastDate = messageDate; // 마지막 날짜 업데이트
                }
                $chatLogBox.appendChild(createChatElement(message));
            }

            scrollToBottom();

            // 마지막 메시지 내역 저장
            localStorage.setItem(`lastMessage_${chatroomId}`, JSON.stringify(messages));
        } else {
            console.error('Failed to load chat log');
        }
    };
    xhr.onerror = function () {
        console.error('Request error');
    }
    xhr.send();
}

// 채팅 화면 스크롤 하단으로 이동
const scrollToBottom = function () {
    $contentBody.scrollTop = $contentBody.scrollHeight;
}

// 채팅 날짜 표시
const displayDate = function (dateString) {
    const dateNoticeElement = document.createElement('li');
    dateNoticeElement.classList.add('date-notice');
    dateNoticeElement.innerHTML = `<span>${dateString}</span>`;
    $chatLogBox.appendChild(dateNoticeElement);
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
            const messageDate = formatCreatedAtToDateString(data.createdAt);

            if (lastDate !== messageDate) {
                displayDate(messageDate);
                lastDate = messageDate; // 마지막 날짜 업데이트
            }

            // 메시지를 채팅 창에 추가
            $chatLogBox.appendChild(createChatElement(data));

            scrollToBottom();

            // 마지막 메시지 내역 저장
            localStorage.setItem(`lastMessage_${chatroomId}`, JSON.stringify(data));
        });
    });

    return client;
}
const stompClient = stompRegister();

// STOMP : 메세지 전송
const sendMessage = function () {
    const msg = $chatContent.value.trim();

    if (msg) {
        const msgData = {
            chatroomId: chatroomId,
            memberId: memberId,
            nickname: nickname,
            message: msg,
        };

        stompClient.send(`/pub/chatroom/chat`, {}, JSON.stringify(msgData));

        const messageDate = formatCreatedAtToDateString(new Date().toISOString());
        if (lastDate !== messageDate) {
            displayDate(messageDate);
            lastDate = messageDate; // 마지막 날짜 업데이트
        }
        scrollToBottom();

        // 마지막 메시지 내역 저장
        localStorage.setItem(`lastMessage_${chatroomId}`, JSON.stringify(msgData));
    }
}

let lastDate = null;
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

// createdAt -> 날짜 표시로 변경
const formatCreatedAtToDateString = function (createdAt) {
    // Date 객체로 변환
    const date = new Date(createdAt);

    // 날짜 yyyy년MM월dd일 형식으로 추출
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    // 날짜 문자열 조합
    return `${year}년 ${month}월 ${day}일`;
}