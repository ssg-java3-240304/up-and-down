// 선택된 카테고리 저장을 위한 배열
let selectedCategories = [];

$(document).ready(function () {
    // 탭 클릭 이벤트
    $('.nav-link').on('click', function (e) {
        e.preventDefault();
        let filter = $(this).data('tab');

        // '전체' 탭 클릭 시 list 페이지로 리다이렉트
        if (filter === 'all') {
            window.location.href = '/app/chat-rooms/list';
            return;
        }

        // 로그인 체크가 필요한 탭에 대해서는 별도의 AJAX 요청 처리
        if (filter === 'our' || filter === 'mine') {
            checkAuthenticationAndLoadTab(filter);
        } else {
            // 그 외의 탭은 바로 로드
            $('.nav-link').removeClass('active');
            $(this).addClass('active');
            loadChatRooms(filter);
        }
    });

    // 검색 버튼 클릭 이벤트
    $('#searchButton').on('click', function () {
        loadChatRooms(getCurrentFilter());
    });

    // 검색 입력 필드에서 Enter 키 눌렀을 때 검색
    $('#searchQuery').on('keypress', function (e) {
        if (e.which === 13) { // 13은 Enter 키의 코드
            e.preventDefault(); // 기본 동작 방지
            loadChatRooms(getCurrentFilter());
        }
    });

    // 카테고리 버튼 클릭 이벤트
    $('.category-button').on('click', function () {
        let category = $(this).data('category');

        // 클릭된 버튼의 active 클래스를 토글
        $(this).toggleClass('active');

        // 카테고리 선택 또는 해제
        if ($(this).hasClass('active')) {
            if (!selectedCategories.includes(category)) {
                selectedCategories.push(category); // 선택 시 추가
            }
        } else {
            selectedCategories = selectedCategories.filter(c => c !== category); // 선택 해제 시 제거
        }

        // 선택된 카테고리로 필터링된 결과 로드
        loadChatRooms(getCurrentFilter());
    });

    // 페이지네이션 클릭 이벤트
    $(document).on('click', '.pagination-link', function (e) {
        e.preventDefault();
        let page = parseInt($(this).data('page'));

        if (!isNaN(page) && page >= 0) {
            loadChatRooms(getCurrentFilter(), page);
        }
    });

    // 로그인 상태를 확인하고 탭을 로드하는 함수
    function checkAuthenticationAndLoadTab(filter) {
        $.ajax({
            url: '/app/auth/login', // 로그인 상태를 확인하는 API 엔드포인트
            type: 'GET',
            success: function (data) {
                // 사용자가 로그인되어 있다면 해당 탭 로드
                $('.nav-link').removeClass('active');
                $('.nav-link[data-tab="' + filter + '"]').addClass('active');
                loadChatRooms(filter);
            },
            error: function (xhr) {
                if (xhr.status === 401) { // 인증되지 않은 상태라면// 로그인 페이지로 리다이렉트하며, 메시지를 쿼리 파라미터로 전달
                    window.location.href = '/auth/login?message=로그인 후 이용가능합니다.';
                }
            }
        });
    }

    // 채팅방 목록을 로드하는 함수 (탭 클릭, 검색, 필터링, 페이지네이션에 사용)
    function loadChatRooms(filter, page = 0, size = 10) {
        let baseUrl = '/app/chat-rooms';

        // 필터에 따른 URL 경로 설정
        if (filter === 'our') {
            baseUrl += '/our';
        } else if (filter === 'mine') {
            baseUrl += '/my';
        }

        // 페이지네이션 파라미터 추가
        let url = `${baseUrl}?page=${page}&size=${size}`;

        // 검색 키워드와 타입 가져오기
        let keyword = $('#searchQuery').val();
        let type = $('#searchType').val();

        // 선택된 카테고리들을 콤마로 연결하여 URL에 추가
        let categories = selectedCategories.join(',');
        if (categories.length > 0) {
            url += `&categories=${encodeURIComponent(categories)}`;
        }

        // 검색어와 검색 타입이 있을 경우 URL에 추가
        if (keyword) {
            url += `&q=${encodeURIComponent(keyword)}&searchType=${encodeURIComponent(type)}`;
        }

        // AJAX 요청
        $.ajax({
            url: url,
            type: 'GET',
            success: function (data) {
                console.log("Received data:", data);
                updateChatRoomList(data, filter);
                updatePagination(data);
                updateTotalCount(data.totalElements); // 전체 수를 갱신
            },
            error: function (xhr, status, error) {
                console.error('AJAX Request Error:', error);
            }
        });
    }

    // 채팅방 목록을 업데이트하는 함수
    function updateChatRoomList(chatRooms, filter) {
        let listContainer = $('#list-container');
        let noResultsContainer = $('#no-results');
        listContainer.empty();

        if (!Array.isArray(chatRooms) || chatRooms.length === 0) {
            let noResultsText = '';
            if (filter === 'our') {
                noResultsText = '참여 중인 모임이 없습니다. 새로운 모임에 가입해보세요!';
            } else if (filter === 'mine') {
                noResultsText = '생성한 모임이 없습니다. 새로운 모임을 만들어보세요!';
            } else {
                noResultsText = '채팅방이 없습니다. 새로운 채팅방을 만들어보세요!';
            }
            noResultsContainer.text(noResultsText).removeClass('d-none');
        } else {
            noResultsContainer.addClass('d-none');

            chatRooms.forEach(chatRoom => {
                let chatRoomItem = `
                        <div class="list-item" onclick="window.location.href='/app/chat-rooms/${chatRoom.chatRoomId}'">
                            <div class="list-item-content d-flex align-items-center justify-content-between">
                                <div class="d-flex align-items-center w-100">
                                    <h3 class="list-title m-0">${chatRoom.name}</h3>
                                    <span class="list-nickname ms-3">${chatRoom.nickname}</span>
                                    <div class="list-categories ms-3">
                                        ${chatRoom.category.map(category => `<span class="category-badge">${category}</span>`).join('')}
                                    </div>
                                    <div class="list-participants ms-auto">참여 인원수: ${chatRoom.memberCount}명</div>
                                </div>
                            </div>
                        </div>`;
                listContainer.append(chatRoomItem);
            });
        }
    }

    // 페이지네이션을 업데이트하는 함수
    function updatePagination(pageData, currentTab) {
        let pagination = $('#pagination');
        let paginationList = pagination.find('.pagination');
        paginationList.empty();

        // 전체 탭에서는 페이지바를 무조건 보여줌
        if (currentTab === 'all') {
            pagination.show();
        }
        // 카테고리, 검색 결과 탭에서는 데이터가 10개를 초과할 때만 페이지바를 보여줌
        else if (pageData.totalElements > 10) {
            pagination.show();
        }
        // 우리모임/내모임 탭에서는 조건에 따라 페이지바를 숨김 또는 표시
        else if ((currentTab === 'myGroup' || currentTab === 'myChats') && pageData.isUserInvolved) {
            if (pageData.totalElements > 10) {
                pagination.show();
            } else {
                pagination.hide();
            }
        } else {
            pagination.hide();
        }

        // 페이지바를 표시하는 경우에만 버튼 생성
        if (pagination.is(':visible')) {
            // Previous 버튼
            let prevClass = pageData.number === 0 ? 'disabled' : '';
            paginationList.append(`
            <li class="page-item ${prevClass}">
                <a class="page-link pagination-link" href="#" data-page="${pageData.number - 1}" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
        `);

            // 페이지 번호
            for (let i = 0; i < pageData.totalPages; i++) {
                let activeClass = (i === pageData.number) ? 'active' : '';
                paginationList.append(`
                <li class="page-item ${activeClass}">
                    <a class="page-link pagination-link" href="#" data-page="${i}">${i + 1}</a>
                </li>
            `);
            }

            // Next 버튼
            let nextClass = pageData.number + 1 === pageData.totalPages ? 'disabled' : '';
            paginationList.append(`
            <li class="page-item ${nextClass}">
                <a class="page-link pagination-link" href="#" data-page="${pageData.number + 1}" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        `);
        }
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