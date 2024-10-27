function previewImage(input) {
    const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
    const file = input.files[0];
    const imagePreview = $('#imagePreview');

    if (file && allowedTypes.includes(file.type)) { // 파일 유형 검사
        const reader = new FileReader();
        reader.onload = function (e) {
            imagePreview.attr('src', e.target.result);
        }
        reader.readAsDataURL(file);
    } else {
        alert('이미지 파일만 업로드 가능합니다 (JPEG, PNG, GIF).');
        input.value = ''; // 유효하지 않은 파일 제거
        imagePreview.attr('src', '/img/member/member-default.png'); // 기본 이미지 경로 설정
    }
}

$(document).ready(function() {
    const imagePreview = $('#imagePreview');
    if (!imagePreview.attr('src')) { // 기본 이미지 경로 설정 확인
        imagePreview.attr('src', '/img/member/member-default.png');
    }
});


// 이메일 인증 결과 업데이트
function updateEmailVerificationResult(email) {
    $('#memberEmail').val(email);
    $('#emailVerified').val('true');
    $('#emailVerificationResult').text('이메일 인증이 완료되었습니다.').removeClass('text-danger').addClass('text-success').show();
    $('#emailError').hide();
}

// 클라이언트가 업데이트 시 필요한 검증
function validateForm(event) {
    const password = $('#password').val();
    const confirmPassword = $('#confirmPassword').val();
    if (password !== confirmPassword) {
        $('#passwordCheckResult').text('비밀번호가 일치하지 않습니다.');
        event.preventDefault();
        return false;
    }

    const file = document.getElementById('imageUpload').files[0];
    if (file && file.size > 10 * 1024 * 1024) { // 10MB 크기 제한
        alert('이미지 파일 크기는 10MB를 초과할 수 없습니다.');
        event.preventDefault();
        return false;
    }
    return true;
}

// 연락처 : 전화번호 자동 하이픈 추가
function formatPhoneNumber(input) {
    const value = input.value.replace(/[^0-9]/g, '');
    let result = '';

    if (value.startsWith('02')) {
        if (value.length < 3) {
            result = value;
        } else if (value.length < 6) {
            result = value.slice(0, 2) + '-' + value.slice(2);
        } else if (value.length < 10) {
            result = value.slice(0, 2) + '-' + value.slice(2, 5) + '-' + value.slice(5);
        } else {
            result = value.slice(0, 2) + '-' + value.slice(2, 6) + '-' + value.slice(6);
        }
    } else {
        if (value.length < 4) {
            result = value;
        } else if (value.length < 7) {
            result = value.slice(0, 3) + '-' + value.slice(3);
        } else if (value.length < 11) {
            result = value.slice(0, 3) + '-' + value.slice(3, 6) + '-' + value.slice(6);
        } else {
            result = value.slice(0, 3) + '-' + value.slice(3, 7) + '-' + value.slice(7);
        }
    }
    input.value = result;
}

// 주소 : 주소 검색창 테마
var themeObj = {
    bgColor: "#E0F0FD",
    searchBgColor: "#F8F8F8",
    pageBgColor: "#BDE4FD",
    outlineColor: "#D0E9F9"
};

// 주소 : 주소 검색 후 추출
function kakaoMap() {
    new daum.Postcode({
        theme: themeObj,
        oncomplete: function(data) {
            var roadAddr = data.roadAddress;
            var extraRoadAddr = '';

            if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                extraRoadAddr += data.bname;
            }
            if (data.buildingName !== '' && data.apartment === 'Y') {
                extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            }
            if (extraRoadAddr !== '') {
                extraRoadAddr = ' (' + extraRoadAddr + ')';
            }
            var fullAddress = '(' + data.zonecode + ') ' + roadAddr + extraRoadAddr;
            document.getElementById("memberAddress").value = fullAddress;
        }
    }).open();
}

// 이메일 : 인증 팝업 열기
function openEmailVerificationPopup() {
    window.open('/member/emailVerification', 'emailVerification', 'width=500,height=550');
}
