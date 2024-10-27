async function fetchExamData(examId) {
    const response = await fetch(`/api/exam/${examId}`);
    if (!response.ok) throw new Error('시험 데이터를 불러오는데 실패했습니다');
    return response.json();
}

document.addEventListener('DOMContentLoaded', function() {
    var today = new Date().toISOString().split('T')[0];
    document.getElementById('exam__modal__form-date').value = today;
});


const modBtns = document.querySelectorAll(".exam__modㅡbtn");
const modModal = document.getElementById("examModModal");

modBtns.forEach(btn => {
    btn.addEventListener("click", async function(event) {
        const examId = event.target.value;
        try {
            const examData = await fetchExamData(examId);
            console.log("시험 데이터:", examData);

            modModal.querySelector('#exam__mod-modal__form-examId').value = examData.examId;
            modModal.querySelector('#exam__mod-modal__form-name').value = examData.examName;
            modModal.querySelector('#exam__mod-modal__form-type').value = examData.examType.examTypeName;
            modModal.querySelector('#exam__mod-modal__form-date').value = examData.examDate;
            modModal.querySelector('#exam__mod-modal__form-end-date').value = examData.examEndDate;
            modModal.querySelector('#exam__mod-modal__form-memo').value = examData.examMemo;

        } catch (error) {
            console.error('시험 데이터를 불러오는 중 오류가 발생했습니다:', error);
            // 필요에 따라 오류 처리를 추가합니다
        }
    });
});
