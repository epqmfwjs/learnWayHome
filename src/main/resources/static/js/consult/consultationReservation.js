document.addEventListener('DOMContentLoaded', function() {
    $('.sidebar-item').click(function() {
        $(this).find('.submenu').slideToggle(300);
    });

    $('.consult-link').click(function(event) {
        event.preventDefault(); // 링크의 기본 동작을 막음
        openModal();
    });

    $('#myModal .close span, #myModal').click(function(event) {
        if (event.target == this) {
            $('#myModal').modal('hide');
        }
    });
});

function openModal() {
    $('#myModal').modal('show');
    loadConsultants();
}

let consultantsData = [];
let currentPage = 1;
const pageSize = 4;

// 모달창에 상담사리스트 불러오기 요청
function loadConsultants() {
    fetch('/api/consultants')
        .then(response => response.json())
        .then(data => {
            consultantsData = data;
            renderPage(currentPage);
            renderPagination();
        });
}

function renderPage(page) {
    const consultantList = document.getElementById('consultantList');
    consultantList.innerHTML = '';

    const startIndex = (page - 1) * pageSize;
    const endIndex = startIndex + pageSize;
    const pageConsultants = consultantsData.slice(startIndex, endIndex);

    pageConsultants.forEach(consultant => {
        const consultantBox = document.createElement('div');
        consultantBox.className = 'modal-consultant-box';

        const img = document.createElement('img');
        img.src = consultant.imageUrl;
        img.alt = `상담사 ${consultant.name} 사진`;

        const consultantHeader = document.createElement('div');
        consultantHeader.className = 'modal-consultant-header';

        const name = document.createElement('h3');
        name.textContent = `${consultant.name} 상담사`;

        consultantHeader.appendChild(name);

        const consultantBody = document.createElement('div');
        consultantBody.className = 'modal-consultant-body';

        const subject = document.createElement('p');
        subject.textContent = `상담과목: ${consultant.subject}`;
        subject.style.marginTop = '10px';
        subject.style.fontWeight = 'bold';

        const description = document.createElement('p');
        description.textContent = `${consultant.description}`;
        description.style.marginTop = '50px';
        description.style.fontWeight = 'bolder';

        consultantBody.appendChild(subject);
        consultantBody.appendChild(description);

        const consultantFooter = document.createElement('div');
        consultantFooter.className = 'modal-consultant-footer';

        const link = document.createElement('button');
        link.className = 'btn btn-outline-success fixed-size-button';
        link.type = 'button';
        link.textContent = '예약하기';
        link.addEventListener('click', function() {
            window.location.href = `/reservationBoard?consultant=${consultant.id}`;
        });

        consultantFooter.appendChild(link);

        consultantBox.appendChild(img);
        consultantBox.appendChild(consultantHeader);
        consultantBox.appendChild(consultantBody);
        consultantBox.appendChild(consultantFooter);

        consultantList.appendChild(consultantBox);
    });
}

function renderPagination() {
    const paginationContainer = document.getElementById('pagination');
    paginationContainer.innerHTML = '';

    const pageCount = Math.ceil(consultantsData.length / pageSize);

    if (pageCount > 1) {
        const prevButton = document.createElement('button');
        prevButton.textContent = '≪';
        prevButton.className = 'page-link btn btn-info';
        prevButton.disabled = currentPage === 1;
        prevButton.addEventListener('click', function() {
            if (currentPage > 1) {
                currentPage--;
                renderPage(currentPage);
                renderPagination();
            }
        });
        paginationContainer.appendChild(prevButton);

        for (let i = 1; i <= pageCount; i++) {
            const pageButton = document.createElement('button');
            pageButton.textContent = i;
            pageButton.className = 'page-link btn btn-info';
            pageButton.classList.toggle('active', i === currentPage);
            pageButton.addEventListener('click', function() {
                currentPage = i;
                renderPage(currentPage);
                renderPagination();
            });
            paginationContainer.appendChild(pageButton);
        }

        const nextButton = document.createElement('button');
        nextButton.textContent = '≫';
        nextButton.className = 'page-link btn btn-info';
        nextButton.disabled = currentPage === pageCount;
        nextButton.addEventListener('click', function() {
            if (currentPage < pageCount) {
                currentPage++;
                renderPage(currentPage);
                renderPagination();
            }
        });
        paginationContainer.appendChild(nextButton);
    }


}
