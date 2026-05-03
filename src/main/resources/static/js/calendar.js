// ── CALENDAR STATE ──
const today = new Date();
let currentMonth = today.getMonth();
let currentYear = today.getFullYear();
let selectedDate = today.toISOString().split("T")[0];

// ── CALENDAR ──
function setupMonthYear() {
    const monthSelect = document.getElementById("monthSelect");
    const yearSelect = document.getElementById("yearSelect");

    const months = [
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    ];

    months.forEach((month, index) => {
        monthSelect.innerHTML += `<option value="${index}">${month}</option>`;
    });

    for (let y = currentYear - 1; y <= currentYear + 2; y++) {
        yearSelect.innerHTML += `<option value="${y}">${y}</option>`;
    }

    monthSelect.value = currentMonth;
    yearSelect.value = currentYear;
}

function renderCalendar() {
    const calendar = document.getElementById("calendar");
    const monthSelect = document.getElementById("monthSelect");
    const yearSelect = document.getElementById("yearSelect");

    currentMonth = Number(monthSelect.value);
    currentYear = Number(yearSelect.value);

    calendar.innerHTML = "";

    // Day headers
    const days = ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"];
    days.forEach(day => {
        calendar.innerHTML += `<div class="day-name">${day}</div>`;
    });

    // Empty cells before first day
    const firstDay = new Date(currentYear, currentMonth, 1).getDay();
    for (let i = 0; i < firstDay; i++) {
        calendar.innerHTML += `<div></div>`;
    }

    // Day cells
    const daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();
    for (let day = 1; day <= daysInMonth; day++) {
        const dateValue = `${currentYear}-${String(currentMonth + 1).padStart(2, "0")}-${String(day).padStart(2, "0")}`;
        const isSelected = dateValue === selectedDate ? "selected" : "";
        const isToday = dateValue === today.toISOString().split("T")[0] ? "today" : "";

        calendar.innerHTML += `
            <div class="date ${isSelected} ${isToday}" onclick="selectDate('${dateValue}')">
                ${day}
            </div>
        `;
    }

    renderSlots(); // always render slots after calendar
}

function selectDate(date) {
    selectedDate = date;
    renderCalendar();

    if (role && role !== "Patient" && role != "Unknown") {
        const label = document.getElementById("selectedDateLabel");
        const row = document.getElementById("selectedDateRow");
        label.textContent = new Date(date).toLocaleDateString("en-US", {
            weekday: "long", month: "long", day: "numeric"
        });
        row.style.display = "flex";
    }
}

function changeMonth(amount) {
    currentMonth += amount;

    if (currentMonth < 0) {
        currentMonth = 11;
        currentYear--;
    }

    if (currentMonth > 11) {
        currentMonth = 0;
        currentYear++;
    }

    document.getElementById("monthSelect").value = currentMonth;
    document.getElementById("yearSelect").value = currentYear;

    renderCalendar();
}
