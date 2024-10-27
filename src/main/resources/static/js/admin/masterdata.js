// 기준정보 등록 시 URL 메세지 확인 후 모달 창 표시
window.onload = function() {
    const urlParams = new URLSearchParams(window.location.search); // URL 매개변수를 가져옴
    let message = ''; // 표시할 메시지

    if (urlParams.get('message') === 'success') {
        message = '등록이 완료되었습니다.';
    } else if (urlParams.get('message') === 'updateSuccess') {
        message = '수정이 완료되었습니다.';
    } else if (urlParams.get('message') === 'deleteSuccess') {
        message = '삭제가 완료되었습니다.';
    }

    if (message !== '') {
        $('#modalMessageBody').text(message); // 메시지를 모달 본문에 설정
        $('#messageModal').modal('show'); // 모달을 표시

        // URL에서 매개변수 제거
        window.history.replaceState({}, document.title, window.location.pathname); // URL을 원래 상태로 되돌림
    }
};

let currentPage = 0; // 현재 페이지
let totalPages = 0; // 총 페이지 수
const pageSize = 5; // 한 페이지에 표시할 항목 수

// 데이터를 가져와서 테이블과 페이지네이션을 업데이트하는 함수
function ViewMasterData(page = 0) {
    const category = $('#viewCategory').val(); // 선택된 카테고리를 가져옴
    console.log("Selected category:", category); // 선택된 카테고리를 로그로 출력

    // 카테고리가 선택되지 않은 경우 테이블과 페이지네이션을 비움
    if (category === '') {
        $('#masterDataTableBody').empty(); // 테이블 비움
        $('#pagination').empty(); // 페이지네이션 비움
        return;
    }

    // 선택된 카테고리에 해당하는 데이터를 서버로부터 가져옴
    $.get(`/admin/${category}?page=${page}&size=${pageSize}`, function(data) {
        currentPage = page; // 현재 페이지를 업데이트
        totalPages = data.totalPages; // 총 페이지 수를 업데이트

        let rows = ''; // 테이블 행들을 저장할 변수
        // 데이터를 반복하여 각 행을 생성
        data.content.forEach(item => {
            const code = item.code || item.materialCode || item.studywayCode || item.subjectCode || item.examCode; // 항목 코드
            const name = item.name || item.material || item.studyway || item.subject || item.exam;                 // 항목 이름
            const note = item.note || item.materialNote || item.studywayNote || item.subjectNote || item.examNote || '미입력'; // 항목 노트 // 항목 노트

            // 행을 생성하여 rows에 추가
            rows += `<tr>
                <td>${code}</td>
                <td>${name}</td>
                <td class="truncate">${note}</td> <!-- truncate 클래스를 사용하여 텍스트 길이 제한 -->
                <td>
                <a href="/admin/updateMasterData/${category.slice(0, -1)}/${code}" class="btn btn-outline-success btn-block" >수정</a>
                </td>
                <td>
                <form action="/admin/deleteMasterData/${category.slice(0, -1)}/${code}" method="post">
                    <button type="submit" class="btn btn-outline-danger btn-block">삭제</button>
                </form>
                </td>
            </tr>`;
        });
        $('#masterDataTableBody').html(rows); // 생성된 행들을 테이블에 추가

        renderPagination(); // 페이지네이션 렌더링
    });
}

// 페이지네이션 관련
function renderPagination() {
    let pagination = ''; // 페이지네이션 저장하는 변수
    const startPage = Math.floor(currentPage / pageSize) * pageSize; // 시작 페이지
    const endPage = Math.min(startPage + pageSize, totalPages);   // 끝 페이지

    // "이전" 버튼 추가 (현재 페이지가 0보다 클 때만)
    pagination += `<li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
        <a class="page-link" href="#" onclick="ViewMasterData(${currentPage - 1})">◀</a>
    </li>`;
    // 현재 페이지 그룹의 각 페이지 버튼을 추가
    for (let i = startPage; i < endPage; i++) {
        pagination += `<li class="page-item ${i === currentPage ? 'active' : ''}">
            <a class="page-link" href="#" onclick="ViewMasterData(${i})">${i + 1}</a>
        </li>`;
    }
    // 다음 버튼 추가 (현재 페이지가 총 페이지 수 - 1보다 작을 때만)
    pagination += `<li class="page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}">
        <a class="page-link" href="#" onclick="ViewMasterData(${currentPage + 1})">▶</a>
    </li>`;
    $('#pagination').html(pagination); // 생성된 페이지네이션을 추가
}

// 페이지 로드 시 초기 데이터 로드 및 카테고리 변경 시 데이터 로드
$(document).ready(function() {
    $('#viewCategory').change(function() {
        ViewMasterData(); // 카테고리가 변경되면 데이터를 다시 로드
    });
    ViewMasterData(); // 초기 페이지 로드 시 데이터를 로드
});
