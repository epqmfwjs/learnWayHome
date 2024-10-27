// 사이드바 클릭 시 호버 유지하기
$(document).ready(function() {
    // 현재 페이지 URL 가져오기
    var currentUrl = window.location.href;
    // 사이드바 메뉴 각 링크를 순회하며 현재 URL과 비교하여 활성화 상태 설정
    $('.sidebar-menu .list-group-item').each(function() {
        var menuUrl = $(this).attr('href');
        // 현재 페이지 URL과 메뉴의 URL이 일치하면 active 클래스 추가
        if (currentUrl.indexOf(menuUrl) !== -1) {
            $(this).addClass('active');
        }
    });
});
