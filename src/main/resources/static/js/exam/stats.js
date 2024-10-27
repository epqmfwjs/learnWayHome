const getToday = () => {
    const today = new Date();
    return today.toISOString().substr(0, 10);
};
async function fetchTypeStats(examType, startDate, endDate, retryCount = 0, maxRetries = 2) {
    try {

        const response = await fetch(`/api/stats/${examType}/${startDate}/${endDate}`);
        if (!response.ok) throw new Error('Failed to fetch stats');
        return response.json();
    } catch (error){
        console.error('Error fetching data:', error);
        if (retryCount < maxRetries) {
            setTimeout(() => fetchTypeStats(examType, startDate, endDate, retryCount + 1, maxRetries), 300); // Retry after 1 second
        } else {
            location.href = "/"                
        }
    }
}

const statsRoot = document.getElementById("stats-root");

render();

function render() {
    ReactDOM.render(<Stats />, statsRoot);
}

function Stats() {
    
    const [cat, setCat] = React.useState("score");
    const [startDate,  setStartDate] = React.useState(getToday())
    const [endDate,  setEndDate] = React.useState(getToday())

    
    const [examType, setExamType] = React.useState("all");
    const [examTypeList, setExamTypeList] = React.useState(["all"]);
    const [scoreOption, setScoreOption] = React.useState();
    const [gradeOption, setGradeOption] = React.useState();
    const [stdOption, setStdOption] = React.useState();
    const [isOption, setIsOption] = React.useState(false);

    const fetchData = async (retryCount = 0) => {
        try {
            const statdata = await fetchTypeStats(examType, startDate, endDate);
            
            const subjects = await fetch("/api/subject/").then(res => {
                if (!res.ok) throw new Error('Failed to fetch subjects');
                return res.json();
            });
    
            let scoreSeries = subjects.map(item => ({ name: item.subject, data: [] }));
            let gradeSeries = subjects.map(item => ({ name: item.subject, data: [] }));
            let stdSeries = subjects.map(item => ({ name: item.subject, data: [] }));
    
            let xasisCat = statdata.map(exam => exam.examDate);
    
            const addDataToSeries = (series, dataKey) => {
                statdata.forEach(exam => {
                    console.log("in forEach exam",exam)
                    series?.forEach(seriesData => {
                        exam.scoreList?.forEach(score => {
                            if (seriesData.name === score.subject.subject) {
                                console.log("seriesData.name subject.subject", dataKey, seriesData.name, score.subject.subject);
                                seriesData.data.push(score[dataKey]);
                            }
                        });
                    });
                });
            };
    
            addDataToSeries(scoreSeries, 'scoreScore');
            addDataToSeries(gradeSeries, 'scoreGrade');
            addDataToSeries(stdSeries, 'scoreStdScore');
    
            setScoreOption(createOption(scoreSeries, xasisCat, 0, 100, "시험 일자", "점수", false));
            setGradeOption(createOption(gradeSeries, xasisCat, 1, 9, "시험 일자", "등급", true));
            setStdOption(createOption(stdSeries, xasisCat, 0, 150, "시험 일자", "표준점수", false));

        } catch (error) {
            console.error('Error fetching data:', error);
            if (retryCount < 5) {
                setTimeout(() => fetchData(retryCount + 1), 1000);
            }
        }
    };

    const fetchExamTypeData = async (retryCount = 0) => {
        try {
            const response = await fetch("/api/examtype/all");
            if (!response.ok) throw new Error('Failed to fetch exam types');

            const examTypeData = await response.json();
            setExamTypeList(prev => examTypeData ? examTypeData : prev);
        } catch (error) {
            console.error('Error fetching exam types:', error);
            if (retryCount < 5) {
                setTimeout(() => fetchExamTypeData(retryCount + 1), 1000);
            }
        }
    };

    React.useEffect(() => {
        setIsOption(scoreOption || gradeOption || stdOption ? true : false);
    }, [scoreOption, gradeOption, stdOption]);

    React.useEffect(() => {
        fetchData();
    }, [examType, startDate, endDate]);
    
    React.useEffect(() => {
        fetchExamTypeData();
    }, []);


    const handleExamTypeChange = (event) => {
        setExamType(event.target.value);
    }

    const handleStartDateChange  = (event) => {
        setStartDate(event.target.value)
    }
    const handleEndDateChange  = (event) => {
        setEndDate(event.target.value)
    }
    return (
        <>
            <div className="stats__btn-box">
                <div className="stats__btn-box__col">
                    <select className="form-control examtype-selector" onChange={handleExamTypeChange} defaultValue={"all"}>
                        <option value="all">All</option>
                        {examTypeList?.map(item =>
                            <option key={item.examTypeId} value={item.examTypeName}>{item.examTypeName}</option>
                        )}
                    </select>
                    <div className="stats__date-input">
                        <input type="date"
                            className="form-control"
                            max="2030-01-01"
                            min="1900-01-01"
                            value={startDate}
                            onChange={handleStartDateChange}
                            name="startDate"
                        />
                        <div className="stats__input-divider">
                            <span>~</span>
                        </div>
                        <input type="date"
                            className="form-control"
                            max="2030-01-01"
                            min="1900-01-01"
                            value={endDate}
                            onChange={handleEndDateChange}
                            name="endDate"
                        />
                    </div>
                </div>
                <div className="stats__btn-box__col">
                    <button className={cat ==="score" ? "btn btn-outline-secondary selected" : "btn btn-outline-secondary"} onClick={() => setCat("score")}>점수</button>
                    <button className={cat ==="std" ? "btn btn-outline-secondary selected" : "btn btn-outline-secondary"} onClick={() => setCat("std")}>표준점수</button>
                    <button className={cat ==="grade" ? "btn btn-outline-secondary selected" : "btn btn-outline-secondary"} onClick={() => setCat("grade")}>등급</button>
                </div>
            </div>
            
            <div className="chart-container">
                {isOption ?
                    <ChartType cat={cat} option={
                        cat === "score" ? scoreOption :
                            cat === "grade" ? gradeOption :
                                cat === "std" ? stdOption : null
                    } />
                    : null}
            </div>
        </>
    )
}

function ChartType({ cat, option }) {
    const chartRef = React.useRef(null);

    React.useEffect(() => {
        const chart = new ApexCharts(chartRef.current, option);
        chart.render();
        return () => {
            chart.destroy();
        };
    }, [cat, option]);
    return (
        <>
        <div className="chart" ref={chartRef}></div>
        </>
    )
}

function createOption(series, xaxisCat, min, max, xaxisTitle, yaxisTitle, reversed) {
    return {
        series: series,
        chart: {
            height: "100%",
            width: "100%",
            type: 'line',
            dropShadow: {
                enabled: true,
                color: '#000',
                top: 18,
                left: 7,
                blur: 10,
                opacity: 0.2
            },
            zoom: {
                enabled: false
            },
            toolbar: {
                show: false
            }
        },
        colors: ['#77B6EA', '#545454', '#ECEE81', '#8DDFCB', '#EDB7ED', '#FF0060'],
        dataLabels: {
            enabled: true,
        },
        stroke: {
            curve: 'smooth'
        },
        title: {
            text: '성적',
            align: 'left'
        },
        grid: {
            borderColor: '#e7e7e7', row: {
                colors: ['#f3f3f3', 'transparent'],
                opacity: 0.5
            },
        },
        markers: {
            size: 1
        },
        xaxis: {
            categories: [...xaxisCat],
            title: {
                text: xaxisTitle
            },
            labels: {
                rotate: -45
            }
        },
        yaxis: {
            title: {
                text: yaxisTitle
            },
            min: min,
            max: max,
            reversed: reversed
        },
        legend: {
            position: 'top',
            horizontalAlign: 'right',
            floating: true,
            offsetY: -25,
            offsetX: -5
        }
    };
}
