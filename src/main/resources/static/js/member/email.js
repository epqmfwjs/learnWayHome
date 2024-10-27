// email 발송
function sendVerificationEmail() {
    const email = $('#email').val();
    $.post('/api/email/send', {email: email}, function(response) {
        alert(response);
    }).fail(function(xhr) {
        alert(xhr.responseText);
    });
}

function verifyEmailCode() {
    const email = $('#email').val();
    const code = $('#verificationCode').val();
    $.post('/api/email/verify', {email: email, code: code}, function(response) {
        $('#verificationResult').text(response).show();
        // 인증 성공 시 부모 창의 이메일 필드 업데이트
        window.opener.updateEmailVerificationResult(email);
        window.close();
    }).fail(function(xhr) {
        alert(xhr.responseText);
    });
}
