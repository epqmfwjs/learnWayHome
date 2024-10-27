$(document).ready(function() {
    // 페이지 로드 시 실행되는 코드
    getLoggedInConsultantInfo(); // 로그인한 사용자 정보를 가져와서 사이드바에 표시

    function getLoggedInConsultantInfo() {
		var counselor_id = document.getElementById("counselor_id").value;
        // Ajax를 사용하여 서버에서 로그인한 사용자 정보를 가져옵니다.
        $.ajax({
            url: '/api/consultantInfo',
            type: 'GET',
            success: function(data) {
                // 요청이 성공하면 반환된 데이터에서 사용자 이름을 추출합니다.
				console.log(data)
                var userName =  data.name + " 상담사" // 예시: 서버에서 반환하는 사용자 이름 필드
                var subject = "📚 " + data.subject
                var description = data.description
                var imageUrl =data.imageUrl
                var titleName = "📋 " + data.name + " 상담사 예약페이지"
                // 사용자 이름을 사이드바에 있는 #loggedInUser 요소에 적용합니다.
                $('#loginCon').text(userName);
                $('#subject').text(subject);
                $('#description').text(description);
                $('.consultantIMG').attr('src', imageUrl);
                console.log("이미지 주소 : "+ imageUrl);
                $('#titleName').text(titleName);
            },
            error: function(xhr, status, error) {
                console.error('로그인한 사용자 정보를 가져오는 도중 오류 발생:', error);
                // 오류 처리 로직 추가 가능
            }
        });
    }
});
