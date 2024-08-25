const $shareBtn = document.querySelector('.share-btn');
const $alert = document.querySelector('.share-alert');


// 이벤트 리스너에서 메소드 호출
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

const showCopyNotification = function () {
    $alert.classList.add('notify');

    setTimeout(() => {
        $alert.classList.remove('notify');
    }, 2000); // 2초 후에 알림이 서서히 사라지도록 설정
}
