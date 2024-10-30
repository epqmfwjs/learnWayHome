// 날짜와 시간을 형식화하는 함수
function formatDateTime(dateString) {
    const options = { year: 'numeric', month: 'short', day: 'numeric', hour: 'numeric', minute: 'numeric' };
    return new Date(dateString).toLocaleDateString('ko-KR', options);
}

$(document).ready(function() {
    // 예약 리스트 링크 클릭 시
    $('.reservationList-link').click(function(event) {
        event.preventDefault();
        $('#reservationModal').modal('show');
        fetchReservations(); // 모달 열 때 예약 데이터 가져오기
    });

    
// 예약 데이터를 가져와서 화면에 표시하는 함수
function fetchReservations() {
    $.ajax({
        url: '/api/myReservations',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            console.log('Received reservations data:', data); // 예약 데이터 콘솔 출력
            var reservationList = $('#reservationList');
            reservationList.empty();

            const currentTime = new Date(); // 현재 시간

            data.forEach(function(reservation) {
                // 예약 시간을 포맷팅
                var bookingStartTime = new Date(reservation.bookingStart);
                var bookingEndTime = new Date(reservation.bookingEnd);
                var formattedStartTime = formatDateTime(reservation.bookingStart);
                var formattedEndTime = formatDateTime(reservation.bookingEnd);
                var formattedTime = `${formattedStartTime} ~ ${formattedEndTime}`;
                var roomId = reservation.counselor.id;
                var counselorName = reservation.counselor.name; // counselor에서 직접 접근
                var subject = reservation.counselor.subject; // counselor에서 직접 접근
				var memberId = reservation.member.memberId;
				
                // 현재 시간이 예약 시간 내에 있는지 확인
                var isWithinBookingTime = currentTime >= bookingStartTime && currentTime <= bookingEndTime;
                var linkText = isWithinBookingTime ? '입장하기' : '입장불가';
                var row = `
                    <tr>
                        <td>${subject}</td>
                        <td>${counselorName} 상담사</td>
                        <td>${formattedTime}</td>
						<td><button type="button" class="btn ${isWithinBookingTime ? 'btn-outline-success join-link' : 'btn-outline-secondary disabled-link'}" data-room-id="${roomId}" ${isWithinBookingTime ? '' : 'disabled'}>${linkText}</button></td>

                    </tr>
                `;
                /*  입장조건 반대로
                      	<td>
						  <button 
						    type="button" 
						    class="btn ${!isWithinBookingTime ? 'btn-outline-success join-link' : 'btn-outline-secondary disabled-link'}" 
						    data-room-id="${roomId}" 
						    ${!isWithinBookingTime ? '' : 'disabled'}
						  >
						    ${linkText}
						  </button>
						</td>
                */
                
						//정상작동코드
						//<td><button type="button" class="btn ${isWithinBookingTime ? 'btn-outline-success join-link' : 'btn-outline-secondary disabled-link'}" data-room-id="${roomId}" ${isWithinBookingTime ? '' : 'disabled'}>${linkText}</button></td>
                reservationList.append(row);
            });
        },
        error: function(xhr, status, error) {
            console.error('예약 데이터를 가져오는 데 실패했습니다. 오류: ', error);
        }
    });
}

    // 입장하기 링크 클릭 시의 이벤트 핸들러
    $(document).on('click', '.join-link', function(e) {
        e.preventDefault();
        var roomId = $(this).data('room-id');
        var myKey = $(this).data('member-id');
        console.log("myKey : "+ myKey);
        // WebSocket 연결 설정
        //var socket = new SockJS('/signaling/video');
        //var socket = new SockJS('https://192.168.219.105:443/signaling/video');
        var socket = new SockJS('https://gogolckh.ddns.net/signaling/video');//aws 테스트
        var stompClient = Stomp.over(socket);
        stompClient.debug = null;

        // 서버와의 WebSocket 연결
        stompClient.connect({}, function () {
            console.log('WebSocket 연결 성공');

            // 서버로 roomId 전송
            stompClient.send(`/app/join/room/${roomId}/${myKey}`, {}, JSON.stringify({}));

            // 서버로부터의 응답 처리
            stompClient.subscribe(`/topic/join/room/${roomId}/${myKey}`, function (response) {
                var message = response.body.trim();
                console.log('서버 응답:', message);

                // "successfully"와 "full" 문자열에 따른 처리
                if (message === 'successfully') {
                    console.log("방 들어가기 요청 성공");
                    //window.open(`http://localhost:8080/video?roomId=${roomId}`, '_blank');
					window.open(`https://43.202.58.56:443/member/video?roomId=${roomId}`, '_blank');//aws 테스트
					//window.open(`https://192.168.219.105:443/member/video?roomId=${roomId}`, '_blank');
                } else if (message === 'full') {
                    console.log("방 들어가기 요청 실패");
                    alert('이미 방이 가득 찼습니다.');
                } else {
                    console.log("서버 응답 형식 오류:", message);
                    // 필요한 추가 처리를 여기에 추가
                }

                // WebSocket 연결 종료
                stompClient.disconnect();
            });
        }, function (error) {
            console.error('WebSocket 연결 실패:', error);
        });
    });
});
