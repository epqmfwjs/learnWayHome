function validateImage(input) {
    const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
    const file = input.files[0];

    if (file && allowedTypes.includes(file.type)) {
        if (file.size > 10 * 1024 * 1024) {
            alert('이미지 파일 크기는 10MB를 초과할 수 없습니다.');
            input.value = '';
            return false;
        }
        const reader = new FileReader();
        reader.onload = function(e) {
            $('#imagePreview').attr('src', e.target.result);
        }
        reader.readAsDataURL(file);
    } else {
        alert('이미지 파일만 업로드 가능합니다 (JPEG, PNG, GIF).');
        input.value = '';
        return false;
    }
    return true;
}

// ID 중복 체크
function checkUsername() {
    const username = $('#username').val();
    const usernamePattern = /^[a-zA-Z0-9]{4,20}$/;

    if (username === '') {
        $('#idCheckResult').text('ID를 입력해주세요.').removeClass('text-success').addClass('text-danger');
        return;
    }
    if (!usernamePattern.test(username)) {
        $('#idCheckResult').text('ID는 영문,숫자 4자 이상, 20자 이하로 입력해주세요.').removeClass('text-success').addClass('text-danger');
        return;
    }

    // restController 통한 중복 체크
    $.get('/api/member/check-id', { username: username }, function(response) {
        $('#idCheckResult').text(response).removeClass('text-success text-danger')
            .addClass(response === '사용 가능한 ID입니다.' ? 'text-success' : 'text-danger');
        $('#isUsernameChecked').val(response === '사용 가능한 ID입니다.');
    }).fail(function(xhr) {
        $('#idCheckResult').text(xhr.responseText).removeClass('text-success').addClass('text-danger');
        $('#isUsernameChecked').val('false');
    });
}

// ID 입력란이 변경될 때 중복 체크 결과 초기화
function resetUsernameCheck() {
    $('#isUsernameChecked').val('false');
}

// 클라이언트가 JOIN 시 필요한 검증
function validateForm(event) {
    const isUsernameChecked = $('#isUsernameChecked').val();
    if (isUsernameChecked !== 'true') {
        $('#idCheckResult').text('ID 중복 체크를 해주세요.').removeClass('text-success').addClass('text-danger');
        event.preventDefault();
        return false;
    }

    const password = $('#password').val();
    const confirmPassword = $('#confirmPassword').val();
    if (password !== confirmPassword) {
        $('#passwordCheckResult').text('비밀번호가 일치하지 않습니다.');
        event.preventDefault();
        return false;
    }
}