$(document).ready(function () {
    // 필터링, 탭 클릭, 페이징 등을 위한 AJAX 요청만 처리

    // 탭 클릭 이벤트
    $('.nav-link').on('click', function (e) {
        e.preventDefault();
        let filter = $(this).data('tab');
        $('.nav-link').removeClass('active');
        $(this).addClass('active');
        loadChatRooms(filter);
    });

    // 검색 버튼 클릭 이벤트
    $('#searchButton').on('click', function () {
        loadChatRooms(getCurrentFilter());
    });

    // 카테고리 버튼 클릭 이벤트
    $('.category-button').on('click', function () {
        $(this).toggleClass('active');
        loadChatRooms(getCurrentFilter());
    });

    // 페이지네이션 클릭 이벤트
    $(document).on('click', '.pagination-link', function (e) {
        e.preventDefault();
        let page = parseInt($(this).data('page'));

        // 페이지 번호가 0보다 작거나 전체 페이지보다 큰 경우, 이벤트 중지
        // if (page < 0 || page >= $('#pagination .pagination').children().length - 2) {
        //     return;
        // }
        if (!isNaN(page) && page >= 0) { // 페이지 번호가 유효하면
            loadChatRooms(getCurrentFilter(), page);
        }

        loadChatRooms(getCurrentFilter(), page);
    });

    // 채팅방 목록을 로드하는 함수 (탭 클릭, 검색, 필터링, 페이지네이션에 사용)
    function loadChatRooms(filter, page = 0, size = 10) {
        let url = `/app/chat-rooms?page=${page}&size=${size}`;

        let keyword = $('#searchQuery').val();
        let type = $('#searchType').val();
        let categories = getSelectedCategories();

        let data = {};
        if (keyword) {
            data.q = keyword;
            data.searchType = type;
        }
        if (categories.length > 0) {
            data.categories = categories;
        }

        $.ajax({
            url: url,
            type: 'GET',
            data: data,
            success: function (data) {
                updateChatRoomList(data.content);
                updatePagination(data);
                updateTotalCount(data.totalElements);
            },
            error: function (xhr, status, error) {
                console.error('AJAX Request Error:', error);
                console.log('Status:', status);
                console.log('XHR Object:', xhr);
            }
        });
    }

    function updateChatRoomList(chatRooms) {
        let listContainer = $('#list-container');
        listContainer.empty();

        chatRooms.forEach(chatRoom => {
            let chatRoomItem = `
            <div class="list-item" onclick="window.location.href='/app/chat-rooms/${chatRoom.chatRoomId}'">
                <div class="list-item-content d-flex align-items-center justify-content-between">
                    <div class="d-flex align-items-center w-100">
                        <!-- 제목 -->
                        <h3 class="list-title m-0">${chatRoom.name}</h3>
                        <!-- 닉네임 -->
                        <span class="list-nickname ms-3">${chatRoom.nickname}</span>
                        <!-- 카테고리 -->
                        <div class="list-categories ms-3">
                            ${chatRoom.category.map(category => `<span class="category-badge">${category}</span>`).join('')}
                        </div>
                        <!-- 참여 인원수 (오른쪽 정렬) -->
                        <div class="list-participants ms-auto">참여 인원수: ${chatRoom.memberCount}명</div>
                    </div>
                </div>
            </div>`;
            listContainer.append(chatRoomItem);
        });
    }

    function updatePagination(pageData) {
        let pagination = $('#pagination .pagination');
        pagination.empty();

        // Previous 버튼
        let prevClass = pageData.number === 0 ? 'disabled' : '';
        pagination.append(`
        <li class="page-item ${prevClass}">
            <a class="page-link pagination-link" href="#" data-page="${pageData.number - 1}" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
            </a>
        </li>
    `);

        // 페이지 번호
        for (let i = 0; i < pageData.totalPages; i++) {
            let activeClass = (i === pageData.number) ? 'active' : '';
            pagination.append(`
            <li class="page-item ${activeClass}">
                <a class="page-link pagination-link" href="#" data-page="${i}">${i + 1}</a>
            </li>
        `);
        }

        // Next 버튼
        let nextClass = pageData.number + 1 === pageData.totalPages ? 'disabled' : '';
        pagination.append(`
        <li class="page-item ${nextClass}">
            <a class="page-link pagination-link" href="#" data-page="${pageData.number + 1}" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
            </a>
        </li>
    `);
    }

    function updateTotalCount(totalCount) {
        $('#totalCount').text(totalCount);
    }

    function getCurrentFilter() {
        return $('.nav-link.active').data('tab') || 'all';
    }

    function getSelectedCategories() {
        let categories = [];
        $('.category-button.active').each(function () {
            categories.push($(this).data('category'));
        });
        return categories;
    }
});
