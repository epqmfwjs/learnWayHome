document.addEventListener('DOMContentLoaded', function() {
  var calendarEl = document.getElementById('calendar');

  var calendar = new FullCalendar.Calendar(calendarEl, {
    initialView: 'dayGridMonth',
    editable: true,
    selectable: true,
    slotMinTime: '06:00:00',
    slotMaxTime: '29:59:59', // 다음날 오전 6시를 의미합니다
    nextDayThreshold: '06:00:00',
    initialDate: new Date(),
    eventContent: function(arg) {
	    if (arg.event.title === "●") {
	      return {
      html: '<img src="/img/schedule/check3.png" alt="Check" style="width: 23px; height: 23px;  margin: 0 auto; display: block;">'
			};
	    }
	  },
    events: function(info, successCallback, failureCallback) {
      var currentDate = new Date(); // 현재 날짜 가져오기
      var year = currentDate.getFullYear(); // 현재 연도
      var month = currentDate.getMonth() + 1; // 현재 월 (0부터 시작하므로 1을 더해줌)
       	  
      $.ajax({
        url: "/login/getMonthlySchedule",
        type: "GET",
        dataType: "json",
        data: {
		    year: year,
		    month: month,
		},
        success: function(data) {
          var events = data.map(function(dto) {
            return {
              title: dto.scheduleId != null ? "●" : "",
              start: dto.startTime
            };
          });
          successCallback(events);
        },
        error: function(xhr, status, error) {
          failureCallback(error);
        }
      });
    }
  });

  calendar.render();
});