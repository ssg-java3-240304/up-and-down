$(document).ready(function() {
    // 기본적으로 전체 탭의 채팅방 목록을 로드
    loadChatRooms("all", "", []);

    // 탭 클릭 시 채팅방 목록 로드
    $(".nav-link").on("click", function (event) {
        event.preventDefault();
        var tab = $(this).data("tab");

        $(".nav-link").removeClass("active");
        $(this).addClass("active");

        // 채팅방 목록을 로드
        loadChatRooms(tab, $("#searchQuery").val(), getSelectedCategories());
    });

    // 검색 버튼 클릭 시
    $("#searchButton").on("click", function () {
        var query = $("#searchQuery").val();
        var searchType = $("#searchType").val(); // 검색 타입 (제목/제목+내용)
        loadChatRooms(getCurrentFilter(), query, getSelectedCategories());
    });

    // 카테고리 버튼 클릭 시
    $(".category-button").on("click", function () {
        $(this).toggleClass("active");
        loadChatRooms(getCurrentFilter(), $("#searchQuery").val(), getSelectedCategories());
    });

    // 현재 필터를 가져오는 함수
    function getCurrentFilter() {
        var tab = $(".nav-link.active").data("tab");
        return tab === "all" ? "" : tab;
    }

    // 선택된 카테고리를 가져오는 함수
    function getSelectedCategories() {
        var categories = [];
        $(".category-button.active").each(function () {
            categories.push($(this).data("category"));
        });
        return categories;
    }

    // 채팅방 목록을 로드하는 함수
    function loadChatRooms(filter, keyword, categories, page = 1) {
        var queryParams = {
            filter: filter,
            page: page
        };

        if (keyword) {
            queryParams.keyword = keyword;
        }

        if (categories && categories.length > 0) {
            queryParams.category = categories.join(',');
        }

        $.ajax({
            url: '/chatroom/list',
            type: 'GET',
            data: queryParams,
            success: function (response) {
                if (response && response.chatRoomPage) {
                    renderChatRooms(response.chatRoomPage);
                    renderPagination(response.pageCriteria);
                } else {
                    console.error('Invalid response format:', response);
                    // 사용자에게 알림 또는 기본 메시지 표시
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error('AJAX Error:', textStatus, errorThrown);
                // 사용자에게 알림 또는 에러 메시지 표시
            }
        });
    }

    // 현재 필터를 가져오는 함수
    function getCurrentFilter() {
        var tab = $(".nav-link.active").data("tab");
        return tab === "all" ? "all" : tab; // 기본값으로 "all" 반환
    }

    // 선택된 카테고리를 가져오는 함수
    function getSelectedCategories() {
        var categories = [];
        $(".category-button.active").each(function () {
            categories.push($(this).data("category"));
        });
        return categories;
    }

    // 페이지 네비게이션 클릭 시
    $(document).on("click", ".page-link", function (event) {
        event.preventDefault();
        var page = $(this).data("page");
        loadChatRooms(getCurrentFilter(), $("#searchQuery").val(), getSelectedCategories(), page + 1);
    });

    // 초기 페이지 로드 시 전체 탭의 채팅방 목록을 로드
    $(document).ready(function () {
        loadChatRooms("all", "", []);

        // 탭 클릭 시 채팅방 목록 로드
        $(".nav-link").on("click", function (event) {
            event.preventDefault();
            var tab = $(this).data("tab");

            $(".nav-link").removeClass("active");
            $(this).addClass("active");

            // 채팅방 목록을 로드
            loadChatRooms(tab, $("#searchQuery").val(), getSelectedCategories());
        });

        // 검색 버튼 클릭 시
        $("#searchButton").on("click", function () {
            var query = $("#searchQuery").val();
            loadChatRooms(getCurrentFilter(), query, getSelectedCategories());
        });

        // 카테고리 버튼 클릭 시
        $(".category-button").on("click", function () {
            $(this).toggleClass("active");
            loadChatRooms(getCurrentFilter(), $("#searchQuery").val(), getSelectedCategories());
        });
    });
});


