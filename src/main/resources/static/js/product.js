const $likeBtn = document.querySelector(".like-btn");
const $shareBtn = document.querySelector('.share-btn');
const $alert = document.querySelector('.share-alert');


$likeBtn.addEventListener('click', function () {
   like();
});
$shareBtn.addEventListener('click', function (event) {
    navigator.clipboard.writeText(window.location.href)
        .then(() => {
            if (!$alert.classList.contains('notify')) {
                showCopyNotification();
            }
        })
        .catch(err => {
            console.error('Failed to copy URL: ', err);
            alert('Failed to copy the URL. Please try again.');
        });
});

const like = function () {
    fetch(`/product/like/${productGroupId}`, {
        method: 'GET', // 좋아요 기능은 간단한 GET 요청으로 처리
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json(); // JSON 데이터를 파싱
        })
        .then(data => {
            if (data.authentication) {
                if (data.likeState === "DO_LIKE") {
                    $likeBtn.querySelector('img').src = "https://kr.object.ncloudstorage.com/up-bucket/product/heart-fill.png";
                } else {
                    $likeBtn.querySelector('img').src = "https://kr.object.ncloudstorage.com/up-bucket/product/heart-outline.png";
                }
            } else {
                if (confirm("로그인이 필요한 서비스입니다. 로그인하시겠습니까?")) {
                    const url = encodeURIComponent(window.location.href);
                    console.log(`/auth/login?url=${url}`);
                    window.location.href = `/auth/login?url=${url}`;
                }
            }
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
};

const showCopyNotification = function () {
    $alert.classList.add('notify');

    setTimeout(() => {
        $alert.classList.remove('notify');
    }, 2000); // 2초 후에 알림이 서서히 사라지도록 설정
};