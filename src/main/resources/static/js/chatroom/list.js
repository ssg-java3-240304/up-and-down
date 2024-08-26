// 선택된 카테고리 저장을 위한 배열
let selectedCategories = [];


$(document).ready(function () {
    console.log('js가 로드됐습니다.')
    // 탭 클릭 이벤트
    $('.nav-link').on('click', function (e) {
        e.preventDefault();

        let filter = 'all'
        filter = $(this).data('tab');
        console.log(`탭이 클릭되었습니다. Filter: ${filter}`);
        // '전체' 탭 클릭 시 list 페이지로 리다이렉트
        // if (filter === 'all') {
        //     window.location.href = '/app/chat-rooms/list';
        //     return;
        // }

    // 로그인 체크가 필요한 탭에 대해서는 별도의 AJAX 요청 처리
        if (filter === 'our' || filter === 'mine') {
            console.log(filter + '탭이 클릭됐습니다.')
            checkAuthenticationAndLoadTab(filter);
        } else {
            // 그 외의 탭은 바로 로드
            $('.nav-link').removeClass('active');
            $(this).addClass('active');
            ourChatLoad(filter);
        }
    });

    // 로그인 상태를 확인하고 탭을 로드하는 함수
    function checkAuthenticationAndLoadTab(filter) {
        $.ajax({
            url: '/app/chat-rooms/loginCheck', // 로그인 상태를 확인하는 API 엔드포인트
            type: 'GET',
            success: function (data) {
                console.log("로그인 확인을 완료했습니다!")
                // 사용자가 로그인되어 있다면 해당 탭 로드
                if (data === "Permission") {
                    console.log('로그인 상태 : ' +data)
                    $('.nav-link').removeClass('active');
                    $('.nav-link[data-tab="' + filter + '"]').addClass('active');
                    ourChatLoad(filter);
                }
            },
            error: function (xhr) {
                if (xhr.status === 401) { // 인증되지 않은 상태라면// 로그인 페이지로 리다이렉트하며, 메시지를 쿼리 파라미터로 전달
                    window.location.href = '/auth/login?message=로그인 후 이용가능합니다.';
                }
            }
        });
    }

    //일단 우리 모임 탭만 눌렀을 때 채팅방 로드해주는 리스트 작성
    function ourChatLoad(filter){
        // let url = '/app/chat-rooms/chatRoomList/' + filter;
        let url = '/app/chat-api-rooms/' + filter;
        console.log('ourCHatLoad 시작');
        $.ajax({
            url: url,
            type: 'GET',
            // 속한 채팅방이 존재할 때
            success: function(data){
                console.log("Data : ",data)
                uploadData(data,filter)
                updateTotalCount(data.length);
            },
            //속한 채팅방이 존재하지 않을 때
            error: function (data){
                console.log('데이터가 존재하지 않습니다.')
            }
        })
    }

    //채팅방을 보여주는 함수
    function uploadData(chatRooms, filter){
        console.log('uploadData를 시작합니다.')

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
                            <span class="list-nickname ms-3">${chatRoom.nickName}</span>
                            <div class="list-categories ms-3">
                                ${chatRoom.categories.map(category => `<span class="category-badge">${category}</span>`).join('')}
                            </div>
                            <div class="list-participants ms-auto">참여 인원수: ${chatRoom.memberCount}명</div>
                        </div>
                    </div>
                </div>`;
                listContainer.append(chatRoomItem);
            });
        }
    }
    // 채팅방 목록을 로드하는 함수 (카테고리 선택, 검색)
    function chatRoomUpdate(filter) {
        let baseUrl = '/app/chat-api-rooms/'
        if (filter === 'our') {
            baseUrl += filter;
        }else if (filter === 'mine') {
            baseUrl += filter;
        }else {
            baseUrl += filter;
        }

        console.log('검색어 입력이 들어왔습니다.'+baseUrl);
        //카테고리, 검색어 입력
        let url = `${baseUrl}`;
        let keyword = $('#searchQuery').val();
        console.log("검색어 : ", keyword);
        let categories = selectedCategories.join(',');

        //카테고리 URL에 연결
        if (categories.length > 0) {
            url += `?categories=${encodeURIComponent(categories)}`;
        }
        //검색어 URL에 연결
        if (keyword) {
            // 만약 categories가 추가되지 않았으면 `?`로 시작하고, 추가되었다면 `&`로 이어지도록 설정
            url += (url.includes('?') ? '&' : '?') + `keyword=${encodeURIComponent(keyword)}`;
        }

        console.log("console URL : " + url);
        $.ajax({
            url: url,
            type: 'GET',
            success: function (data) {
                console.log("Received data:", data);
                uploadData(data,filter)
            },
            error: function (xhr, status, error) {
                console.error('AJAX Request Error:', error);
            }
        });

    }

    // 채팅방 목록을 로드하는 함수 (탭 클릭, 검색, 필터링, 페이지네이션에 사용)
    //

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
    // 검색에 값이 입력 할 때마다 실시간 변경
    $('#searchQuery').on('input', function () {
        chatRoomUpdate(getCurrentFilter());
    });

    // 검색 버튼 클릭 이벤트
    $('#searchButton').on('click', function () {
        chatRoomUpdate(getCurrentFilter());
    });

    // 검색 입력 필드에서 Enter 키 눌렀을 때 검색
    $('#searchQuery').on('keypress', function (e) {
        if (e.which === 13) { // 13은 Enter 키의 코드
            e.preventDefault(); // 기본 동작 방지
            chatRoomUpdate(getCurrentFilter());
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
        chatRoomUpdate(getCurrentFilter());
    });

    // 페이지네이션 클릭 이벤트
    $(document).on('click', '.pagination-link', function (e) {
        e.preventDefault();
        let page = parseInt($(this).data('page'));

        if (!isNaN(page) && page >= 0) {
            loadChatRooms(getCurrentFilter(), page);
        }
    });


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
        return $('.nav-link.active').data('tab');
    }

    function getSelectedCategories() {
        let categories = [];
        $('.category-button.active').each(function () {
            categories.push($(this).data('category'));
        });
        return categories;
    }
});
