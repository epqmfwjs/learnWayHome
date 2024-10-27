$(document).ready(function() {
    // 비밀번호 찾기 폼 제출
    $('#password-find-form').submit(function(event) {
        event.preventDefault();

        var username = $('#username').val();
        var email = $('#email').val();

        // 서버로 POST 요청 아이디와 이메일을 확인
        $.post('/password/find', { username: username, email: email }, function(data) {
            if (data === 'OK') {
                // 아이디와 이메일이 일치 시 난수 입력 모달 표시
                $('#verificationModal').modal('show');
            } else {
                // 일치하지 않으면 오류 모달
                $('#errorModal').modal('show');
            }
        }).fail(function() {
            // 요청 실패 시 오류 모달
            $('#errorModal').modal('show');
        });
    });

    // 인증 번호 확인 버튼
    $('#verifyCodeButton').click(function() {
        var email = $('#email').val();
        var code = $('#verificationCode').val();

        // 서버로 POST 요청, 인증 코드 확인
        $.post('/password/verify', { email: email, code: code }, function(data) {
            if (data === 'OK') {
                // 인증 코드가 올바르면 비밀번호 재설정 URL 로 이동
                window.location.href = '/password/reset';
            } else {
                // 인증 코드가 올바르지 않으면 오류 메시지 small 로 하기 표시
                $('#verificationError').show();
            }
        }).fail(function() {
            // 요청 실패 시 오류 메시지 표시
            $('#verificationError').show();
        });
    });

    // URL에 error=true가 포함되어 있으면 오류 메세지 하기 표시
    if (window.location.search.includes('error=true')) {
        $('#errorModal').modal('show');
    }
});