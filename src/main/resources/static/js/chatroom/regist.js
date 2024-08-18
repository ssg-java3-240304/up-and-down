$(document).ready(function() {
    let selectedCategories = [];
    const form = $('form');

    // 카테고리 버튼 클릭 이벤트
    $('.category-button').on('click', function() {
        const category = $(this).data('category');
        $(this).toggleClass('active');
        if ($(this).hasClass('active')) {
            if (!selectedCategories.includes(category)) {
                selectedCategories.push(category);
            }
        } else {
            selectedCategories = selectedCategories.filter(c => c !== category);
        }
        console.log('Selected Categories:', selectedCategories);
    });
});

