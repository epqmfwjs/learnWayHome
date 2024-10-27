/*// 이미지 : 프로필 이미지 미리보기 및 유효성 검사
function previewImage(input) {
    // 허용되는 파일 타입
    const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
    const file = input.files[0];

    if (file && allowedTypes.includes(file.type)) { // 파일 유형 검사
        const reader = new FileReader();
        reader.onload = function (e) {
            $('#imagePreview').attr('src', e.target.result);
        }
        reader.readAsDataURL(file);
    } else {
        alert('이미지 파일만 업로드 가능합니다 (JPEG, PNG, GIF).');
        input.value = ''; // 유효하지 않은 파일 제거
        $('#imagePreview').attr('src', '/img/member/member-default.png'); // 기본 이미지 경로
    }
}*/

// S3
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

// ID : ID 중복 체크
function checkUsername() {
    const username = $('#username').val();               // 입력된 ID 값 받아서 확인
    const usernamePattern = /^[a-zA-Z0-9]{4,20}$/; // 영문만 가능, 4자 이상 20자 이하 정규식
    // ID 입력 확인
    if (username === '') {  // 공백 입력 시
        $('#idCheckResult').text('ID를 입력해주세요.').removeClass('text-success').addClass('text-danger');
        return;
    }
    if (!usernamePattern.test(username)) { // ID 패턴 유효성 검사
        $('#idCheckResult').text('ID는 영문,숫자 4자 이상, 20자 이하로 입력해주세요.').removeClass('text-success').addClass('text-danger');
        return;
    }
    // restController 통한 중복 체크
    $.get('/api/member/check-id', {username: username}, function(response) {
        // idCheckResult -> hidden 속성 input
        $('#idCheckResult').text(response).removeClass('text-success text-danger')
            .addClass(response === '사용 가능한 ID입니다.' ? 'text-success' : 'text-danger');
        $('#isUsernameChecked').val(response === '사용 가능한 ID입니다.');
    }).fail(function(xhr) {
        $('#idCheckResult').text(xhr.responseText).removeClass('text-success').addClass('text-danger');
        $('#isUsernameChecked').val('false');
    });
}

// 이메일 인증 결과 업데이트
function updateEmailVerificationResult(email) {
    $('#email').val(email);
    $('#emailVerified').val('true');
    $('#emailVerificationResult').text('이메일 인증이 완료되었습니다.').removeClass('text-danger').addClass('text-success').show();
    $('#emailError').hide();
}

// ID 입력란이 변경될 때 중복 체크 결과 초기화 -> 유효성 등 에러 메세지 1개만 출력 (다중 출력 X)
function resetUsernameCheck() {
    $('#isUsernameChecked').val('false');
}

// 클라이언트가 JOIN 시 필요한 검증
// (중복 체크, 비밀번호 확인 일치 체크, 이메일 인증 여부, 프로필 이미지 파일 크기)
function validateForm(event) {
    const isUsernameChecked = $('#isUsernameChecked').val();
    // ID 중복 체크가 진행되어야 진행이 되도록 중복체크 여부 확인
    if (isUsernameChecked !== 'true') {
        $('#idCheckResult').text('ID 중복 체크를 해주세요.').removeClass('text-success').addClass('text-danger');
        event.preventDefault();
        return false;
    }
    // 비밀번호, 비밀번호 확인 인풋 일치 여부 확인
    const password = $('#password').val();
    const confirmPassword = $('#confirmPassword').val();
    if (password !== confirmPassword) {
        $('#passwordCheckResult').text('비밀번호가 일치하지 않습니다.');
        event.preventDefault();
        return false;
    }
    // 프로필 이미지 파일 크기 검사
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
    const value = input.value.replace(/[^0-9]/g, ''); // 숫자만 남기고 제거
    let result = ''; // 초기화

    // 분기점 02로 시작할 경우 첫번째 하이픈까지 2자리를 유지해야 함 -> ex.02-000-0000
    if (value.startsWith('02')) { // 서울 번호일 경우 (02로 시작할 경우)
        if (value.length < 3) {   // 길이가 3보다 작으면 그대로 출력
            result = value;
        } else if (value.length < 6) {   // 길이가 3 이상 6 미만일 경우 02-000 형식
            result = value.slice(0, 2) + '-' + value.slice(2);
        } else if (value.length < 10) {  // 길이가 6 이상 10 미만일 경우 02-000-0000 형식
            result = value.slice(0, 2) + '-' + value.slice(2, 5) + '-' + value.slice(5);
        } else {
            result = value.slice(0, 2) + '-' + value.slice(2, 6) + '-' + value.slice(6);
        }
    } else { // 서울 외 번호 입력
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
    // 세팅된 값
    input.value = result;
}

// 주소 : 주소 검색창 테마
var themeObj = {
    bgColor: "#E0F0FD",  // 바탕 배경색
    searchBgColor: "#F8F8F8",  // 검색창 배경색
    pageBgColor: "#BDE4FD",  // 페이지 배경색
    outlineColor: "#D0E9F9"  // 테두리
};

// 주소 : 주소 검색 후 추출
function kakaoMap() {
    var width = 500; //팝업의 너비
    var height = 600; //팝업의 높이

    new daum.Postcode({
        theme: themeObj,  // 테마 적용
        oncomplete: function(data) {
            var roadAddr = data.roadAddress;  // 도로명 주소 변수
            var extraRoadAddr = '';  // 참고 항목 변수

            // 법정동명 여부 확인 후 추가
            if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                extraRoadAddr += data.bname;
            }
            // 건물명 여부 확인 후 추가
            if (data.buildingName !== '' && data.apartment === 'Y') {
                extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            }
            // 참고 항목 여부 확인 후 추가 + 괄호 ex. (양재동)
            if (extraRoadAddr !== '') {
                extraRoadAddr = ' (' + extraRoadAddr + ')';
            }
            // 주소 인풋에 최종적으로 들어갈 내용
            var fullAddress = '(' + data.zonecode + ') ' + roadAddr + extraRoadAddr;
            document.getElementById("address").value = fullAddress;
        }
    }).open({
        left: (window.screen.width / 2) - (width / 2),
        top: (window.screen.height / 2) - (height / 2)
    });
}

// 생년월일 : 현재 날짜를 기준으로 초등학생 입학 연도 계산하여 기본값 설정
document.addEventListener("DOMContentLoaded", function() {
    const birthInput = document.getElementById('birth');
    const today = new Date();
    const currentYear = today.getFullYear();
    const schoolEntryYear = currentYear - 7;  // 초등학교 입학 연도 계산 (현재 연도 - 7)
    birthInput.value = `${schoolEntryYear}-01-01`;  // 1월 1일로 설정
});

// 이메일 : 인증 팝업 열기
function openEmailVerificationPopup() {
    // 팝업 창 크기
    var popupWidth = 500;
    var popupHeight = 550;
    // 화면 중앙에 위치시키기 위한 위치 계산
    var screenWidth = window.screen.width;
    var screenHeight = window.screen.height;
    var left = (screenWidth - popupWidth) / 2;
    var top = (screenHeight - popupHeight) / 2;
    // 팝업 창 열기
    var popup = window.open('/member/emailVerification', 'emailVerification', 'width=' + popupWidth + ', height=' + popupHeight + ', left=' + left + ', top=' + top);
    // 팝업 창이 차단되었는지 확인하기 위한 코드
    if (popup) {
        popup.focus();
    } else {
        alert('팝업 차단이 감지되었습니다. 팝업 창을 허용해주세요.');
    }
}