// 페이지 로드 시 초기 활성화 상태 설정
$(document).ready(function() {
    var pageUrl = window.location.pathname;
    var hostid =  $('#postid').val();
    var roomid = $("#roomId").val();
             //링크항목이 두개라서 url이 다르기때문에 페이지Url 맵핑 통일시키기위함
             if(pageUrl==="/studylist" || pageUrl ==="/learnway/chat" || pageUrl ==="/enterRoom"
              || pageUrl ==="/studyadd" || pageUrl ==="/studyupdateview" 
              || pageUrl ==="/study/detail/"+hostid || pageUrl ==="/studylist/search" ){
			 pageUrl = "/study";
		 	}
		 	//링크항목이 두개라서 url이 다르기때문에 페이지Url 맵핑 통일시키기위함
		 	 if(pageUrl==="/exam/list/1" || pageUrl ==="/score/"){
			 pageUrl = "/exam";
		 	}
		 	
    $('#sidebar .sidebar-item').each(function() {
        var submenu = $(this);
        var submenuItemValue = submenu.attr('data-value');

        console.log(pageUrl);
        
		//페이지 값과 벨류값이 같은지 확인하고 active css 적용
        if (pageUrl === submenuItemValue) {
            submenu.addClass('active');
            submenu.find('.submenu').slideDown();
            submenu.find('.arrow-icon').removeClass('fa-chevron-right').addClass('fa-chevron-down');
        }
    });
});

	//클릭해서 드롭다운실행할때 화살표방향 바꾸기위한 클래스 속성 갈아끼우는 작업
    function toggleSubMenu(item) {

        // 화살표 아이콘 클래스 변경
        var icon = $(item).find('.arrow-icon');
        if (icon.hasClass('fa-chevron-right')) {
            icon.removeClass('fa-chevron-right').addClass('fa-chevron-down');
        } else {
            icon.removeClass('fa-chevron-down').addClass('fa-chevron-right');
        }
        // 다른 메뉴 아이템의 서브메뉴는 닫기
            $(item).siblings().find('.submenu').slideUp();
        
        // 다른 메뉴 아이템의 화살표 아이콘 클래스 초기화
        $(item).siblings().find('.arrow-icon').removeClass('fa-chevron-down').addClass('fa-chevron-right');
    }
 
 
