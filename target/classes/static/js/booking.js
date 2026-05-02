// ── SESSION ──
const role = sessionStorage.getItem("userRole");
const mrn = sessionStorage.getItem("userMrn");
const userId = sessionStorage.getItem("userId");

// ── SLOTS DATA ──
let slots = [];

function fetchSlots() {
    fetch("/slots")
        .then(res => res.json())
        .then(data => {
            slots = data.map(slot => ({
                id: slot.id,
                date: slot.startTime.split("T")[0],        // "2026-03-02T12:00:00" → "2026-03-02"
                time: formatTime(slot.startTime, slot.endTime), // "12:00 PM - 1:00 PM"
                doctor: `${slot.provider.title}. ${slot.provider.lastName}`,
                type: slot.provider.type,
                clinic: slot.clinic.nameAndAddress
            }));
            renderSlots(); // ✅ render after data is fetched
        })
        .catch(err => {
            console.error("Failed to fetch slots:", err);
        });
}

// Helper to format datetime to readable time
function formatTime(start, end) {
    const startTime = new Date(start).toLocaleTimeString("en-US", { hour: "numeric", minute: "2-digit" });
    const endTime = new Date(end).toLocaleTimeString("en-US", { hour: "numeric", minute: "2-digit" });
    return `${startTime} - ${endTime}`;
}

// ── CALENDAR STATE ──
const today = new Date();
let currentMonth = today.getMonth();
let currentYear = today.getFullYear();
let selectedDate = today.toISOString().split("T")[0];

// ── APPOINTMENTS ──
function renderAppointments() {
    const container = document.getElementById("appointments");
    container.innerHTML = "";

    // Not logged in → show message only
    if (!role) {
        container.innerHTML = `<p>Please <a href="/login">log in</a> to view your appointments.</p>`;
        return;
    }

    // PATIENT → fetch own appointments
    if (role === "PATIENT") {
        fetch(`/patients/${mrn}/appointments`)
            .then(res => res.json())
            .then(data => {
                if (data.length === 0) {
                    container.innerHTML = `<p>No upcoming appointments.</p>`;
                    return;
                }
                data.forEach(appt => {
                    container.innerHTML += `
                        <div class="appointment-card">
                            <strong>
                                ${appt.availableSlot.startTime} - ${appt.availableSlot.endTime}
                                ${appt.availableSlot.provider.title}. ${appt.availableSlot.provider.lastName}
                            </strong>
                            <p>${appt.service} — ${appt.status}</p>
                        </div>
                    `;
                });
            })
            .catch(err => {
                console.error("Failed to fetch patient appointments:", err);
                container.innerHTML = `<p>Failed to load appointments.</p>`;
            });
        return;
    }

    // USER / STAFF / ADMIN → fetch all appointments
    fetch("/appointments")
        .then(res => res.json())
        .then(data => {
            if (data.length === 0) {
                container.innerHTML = `<p>No appointments found.</p>`;
                return;
            }
            data.forEach(appt => {
                container.innerHTML += `
                    <div class="appointment-card">
                        <strong>
                            ${appt.availableSlot.startTime} - ${appt.availableSlot.endTime},
                            ${appt.availableSlot.provider.title}. ${appt.availableSlot.provider.lastName}
                        </strong>
                        <p>Patient: ${appt.patient.firstName} ${appt.patient.lastName}</p>
                        <p>Service: ${appt.service} — ${appt.status}</p>
                    </div>
                `;
            });
        })
        .catch(err => {
            console.error("Failed to fetch appointments:", err);
            container.innerHTML = `<p>Failed to load appointments.</p>`;
        });
}

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

    renderSlots();
}

function selectDate(date) {
    selectedDate = date;
    renderCalendar();
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

// ── SLOTS ──
function renderSlots() {
    const container = document.getElementById("slots");
    const clinic = document.getElementById("clinicFilter").value;
    const doctor = document.getElementById("doctorFilter").value;

    const filteredSlots = slots.filter(slot => {
        const matchesDate = slot.date === selectedDate;
        const matchesDoctor = doctor === "" || slot.doctor === doctor;
        const matchesClinic = clinic === "" || slot.clinic === clinic;
        return matchesDate && matchesClinic && matchesDoctor;
    });

    container.innerHTML = "";

    if (filteredSlots.length === 0) {
        container.innerHTML = `<p>No available slots for this date.</p>`;
        return;
    }

    filteredSlots.forEach(slot => {
        // Show book button only if logged in
        const bookBtn = role
            ? `<button class="btn-book" onclick="bookSlot(${slot.id})">Book</button>`
            : `<a href="/login" class="btn-login-prompt">Log in to book</a>`;

        container.innerHTML += `
            <div class="slot">
                <div class="slot-info">
                    <strong>${slot.time}</strong> - ${slot.doctor} (${slot.type})
                    <br>
                    ${slot.clinic}
                </div>
                ${bookBtn}
            </div>
        `;
    });
}

// ── BOOKING ──
function bookSlot(slotId) {
    if (!role) {
        window.location = "/login";
        return;
    }

    const payload = {
        mrn: mrn,
        slotId: slotId
    };

    const endpoint = role === "PATIENT"
        ? `/patients/${mrn}/booking`
        : `/appointments/booking`;

    fetch(endpoint, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
    .then(res => res.text())
    .then(data => {
        alert(data);
        renderAppointments(); // refresh appointments after booking
        renderSlots();        // refresh slots after booking
    })
    .catch(err => {
        console.error("Booking failed:", err);
        alert("Booking failed. Please try again.");
    });
}

// ── DOCTORS DROPDOWN ──
function loadDoctors() {
    const select = document.getElementById("doctorFilter");
    fetch("/providers")
        .then(res => res.json())
        .then(data => {
            data.forEach(provider => {
                const option = document.createElement("option");
                option.value = `${provider.title}. ${provider.lastName}`; 
                option.textContent = `${provider.title}. ${provider.lastName}`;
                select.appendChild(option);
            });
        });
}

// ── INIT ──
loadDoctors();
renderAppointments();
setupMonthYear();
fetchSlots();      // ✅ fetch slots from API
renderCalendar();  // calendar renders immediately, slots load async