document.addEventListener('DOMContentLoaded', function() {
    var Calendar = FullCalendar.Calendar;
    var calendarEl = document.getElementById('calendar');
    var consultantId = new URLSearchParams(window.location.search).get('consultant');
    var calendar;
    var consultantData; // 상담사 정보를 저장할 변수
    let loginUserId = document.getElementById("memberId").value;
    let loginUserName = document.getElementById("memberName").value;
    
    
    // 상담사 정보를 가져오는 함수
    function fetchConsultantInfo(consultantId) {
        return $.ajax({
            url: '/api/consultants/' + consultantId,
            type: 'GET',
            dataType: 'json'
        }).done(function(data) {
            console.log("상담사 정보: ", data);
            return data;
        }).fail(function(xhr, status, error) {
            console.error('상담사 정보를 가져오는 데 실패했습니다. 오류: ', error);
            return null;
        });
    }

    // 예약 데이터 생성
    function fetchReservations(consultantId) {
        return $.ajax({
            url: '/api/reservations?consultant=' + consultantId,
            type: 'GET',
            dataType: 'json'
        }).done(function(data) {
            console.log("예약 데이터: ", data);
            return data;
        }).fail(function(xhr, status, error) {
            console.error('예약 데이터를 가져오는 데 실패했습니다. 오류: ', error);
            return [];
        });
    }

    // 달력 초기화 함수
    function initializeCalendar(consultantName) {
        calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'timeGridWeek',
            initialDate: new Date(),
            locale: 'ko',
            timeZone: 'local',
            headerToolbar: {
                left: 'customButton',
                center: 'title',
                right: 'timeGridWeek'
            },
             customButtons: {
	            customButton: {
	                text: consultantName + ' 상담사', // 버튼 텍스트 설정
	                click: function() {
	                    // 상담사 정보 모달 표시
	                    showConsultantInfoModal();
	                }
	            }
	        },
            businessHours: [
                {
                    daysOfWeek: [1, 2, 3, 4, 5],
                    startTime: '09:00',
                    endTime: '12:00'
                },
                {
                    daysOfWeek: [1, 2, 3, 4, 5],
                    startTime: '13:00',
                    endTime: '18:00'
                }
            ],
            slotMinTime: '09:00:00',
            slotMaxTime: '18:00:00',
            slotDuration: '01:00:00',
            allDaySlot: false,
            selectConstraint: {
                start: '09:00',
                end: '17:00',
                daysOfWeek: [1, 2, 3, 4, 5]
            },
            events: function(fetchInfo, successCallback, failureCallback) {
                fetchReservations(consultantId).done(function(data) {
                    var events = transformEventData(data);
                    successCallback(events);
                }).fail(function() {
                    failureCallback();
                });
            },
            //지난 날짜를 안보여주며 현재 날짜를기준으로 첫번째 슬롯에 놓고 7일뒤까지 보여주도록 설정
            validRange: { // 현재 날짜부터 7일 후까지의 범위로 제한
                start: new Date(),
                end: new Date(new Date().getTime() + 7 * 24 * 60 * 60 * 1000)
            },
            firstDay: new Date().getDay(), // 현재 요일을 기준으로 첫 번째 슬롯에 표시될 요일 설정
            
            dateClick: function(info) {
                var clickedDate = new Date(info.dateStr);
                var currentDate = new Date();
                var day = clickedDate.getDay();
                var hour = clickedDate.getHours();

                if (clickedDate < currentDate) {
                    alert('예약 가능한 시간이 아닙니다.');
                    return;
                }
				
                if (day === 0 || day === 6) {
                    alert('토요일/일요일/공휴일 은 예약이 불가능합니다.');
                    return;
                }
				
                if (hour >= 12 && hour < 13) {
                    alert('12:00 ~ 01:00 까지 점심시간 입니다.');
                    return;
                }

                var events = calendar.getEvents();
                var overlap = events.some(function(event) {
                    var eventStart = new Date(event.start);
                    var eventEnd = new Date(event.end);
                    var clickedEnd = new Date(clickedDate);
                    clickedEnd.setMinutes(clickedEnd.getMinutes() + 60);

                    return (eventStart < clickedEnd && eventEnd > clickedDate);
                });

                if (overlap) {
                    alert('해당 시간대에는 이미 예약이 있습니다.');
                    return;
                } else {
                    $("#calendarModal").modal("show");
                }

                var startTime = new Date(info.date);
                var localStartTime = formatDateToLocalISO(startTime);
                $("#startTime").val(localStartTime);

                var endDate = new Date(info.date);
                endDate.setHours(endDate.getHours() + 1);
                var localEndTime = formatDateToLocalISO(endDate);
                $("#endTime").val(localEndTime);
            },
            eventClick: function(info) {
                var deleteObj = info.event;
                $("#deleteEventTitle").text(deleteObj.title + '을(를) 취소하시겠습니까?');
                $("#deleteEventId").val(deleteObj.id);
                $("#deleteEventModal").modal("show");
            }
        });

        calendar.render();
    }

function transformEventData(data) {
    return data.map(event => {
        console.log("예약데이터의 멤버아이디 : " + event.member.memberId);
        console.log("로그인한 멤버아이디 : " + loginUserId);
        let boxColor;
        let titleContents;
        let isDisabled = false;

        if (event.member.memberId === loginUserId) {
            boxColor = '#C5E1A5'; // 본인 예약
            titleContents = loginUserName + '님 예약';
        } else {
            boxColor = 'gray'; // 다른 사람 예약
            titleContents = event.counselor.name + ' 상담사';
            isDisabled = true;
        }

        return {
            id: event.id,
            title: titleContents,
            start: event.bookingStart,
            end: event.bookingEnd,
            backgroundColor: boxColor,
            textColor: 'black',
            borderColor: 'white',
            className: `event ${isDisabled ? 'disabled-event' : ''}`
        };
    });
}

    // 상담사 정보를 가져와서 달력을 초기화
    fetchConsultantInfo(consultantId).done(function(data) {
        consultantData = data;
        var consultantName = data ? data.name : '상담사';
        console.log("data : "+data)
        initializeCalendar(consultantName);
    });

    function formatDateToLocalISO(date) {
        const tzOffset = date.getTimezoneOffset() * 60000;
        const localISOTime = new Date(date - tzOffset).toISOString().slice(0, 19);
        return localISOTime;
    }

    // 예약 추가하기 요청
    $("#addCalendar").on("click", function() {
        var counselor = $("#counselor").val();
        var reservationContent = $("#reservationContent").val();
        var start = new Date($("#startTime").val());
        var end = new Date($("#endTime").val());
		console.log(counselor);
        if (!reservationContent) {
            alert("상담하고싶은 내용을 간단하게 적어주세요");
            return;
        }

        var obj = {
            "reservationContent": reservationContent,
            "counselor": parseInt(counselor),
            "bookingStart": formatDateToLocalISO(start),
            "bookingEnd": formatDateToLocalISO(end)
        };

        $.ajax({
            url: "/api/reservations",
            type: "POST",
            data: JSON.stringify(obj),
            contentType: "application/json",
            success: function(response) {
                console.log("예약 성공: ", response);
                $("#calendarModal").modal("hide");
                calendar.refetchEvents();
                // 페이지 상태 초기화
                resetModalState();
            },
            error: function(xhr, status, error) {
                console.log("예약 실패: ", error);
                alert("예약에 실패했습니다. 다시 시도해주세요.");
            }
        });
    });

    $("#deleteEvent").on("click", function() {
        var deleteId = $("#deleteEventId").val();

        $.ajax({
            url: "/api/reservations/" + deleteId,
            type: "DELETE",
            success: function(response) {
                console.log("삭제 성공: ", response);
                $("#deleteEventModal").modal("hide");
                calendar.refetchEvents();
            },
            error: function(xhr, status, error) {
                console.log("삭제 실패: ", error);
                $("#deleteEventModal").modal("hide");
                alert("삭제에 실패했습니다. 다시 시도해주세요.");
            }
        });
    });

    $("#calendarModal").on("hidden.bs.modal", function() {
        resetModalState();
    });

    function resetModalState() {
        $("#counselor").val(consultantId);
        $("#reservationContent").val("");
        $("#startTime").val("");
        $("#endTime").val("");
    }

    // 모달 초기화 시 consultantId 설정
    $("#calendarModal").on("show.bs.modal", function() {
        $("#counselor").val(consultantId);
    });

    resetModalState();
    
	// 상담사 정보 모달 표시 함수
	function showConsultantInfoModal() {
	    if (consultantData) {
	        $("#consultantPhoto").attr("src", consultantData.imageUrl); // 사진 URL 설정
	        console.log("이미지경로 : " + consultantData.imageUrl);
	        $("#consultantName").text(consultantData.name + " 상담사");
	        $("#consultantSubject").text(consultantData.subject);

	        // description을 <br> 태그로 줄 바꿈
	        let formattedDescription = consultantData.description.replace(/\n/g, "<br>");
	        $("#consultantDescription").html(formattedDescription);
	    } else {
	        $("#consultantPhoto").attr("src", ""); // 사진 URL 초기화
	        $("#consultantName").text("상담사 정보를 가져올 수 없습니다.");
	        $("#consultantSubject").text("");
	        $("#consultantDescription").text("");
	    }
	    $("#consultantInfoModal").modal("show");
	}
});
