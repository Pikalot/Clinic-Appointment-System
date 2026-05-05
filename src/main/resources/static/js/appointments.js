// ── APPOINTMENTS ──
function renderAppointments() {
    const container = document.getElementById("appointments");
    container.innerHTML = "";

    // Not logged in → show message only
    if (!role) {
        container.innerHTML = `<p>Please <a href="#" onclick="toggleDrawer()">log in</a> to view your appointments.</p>`;
        return;
    }

    // PATIENT → fetch own appointments
    if (role === "Patient") {
        fetch(`/patients/${mrn}/appointments`)
            .then(res => res.json())
            .then(data => {
                if (data.length === 0) {
                    container.innerHTML = `<p>No upcoming appointments.</p>`;
                    return;
                }
                renderHelper(data);
            })
            .catch(err => {
                console.error("Failed to fetch patient appointments:", err);
                container.innerHTML = `<p>Failed to load appointments.</p>`;
            });
        return;
    }

    // STAFF / ADMIN → fetch all appointments
    fetch("/appointments")
        .then(res => res.json())
        .then(data => {
            if (data.length === 0) {
                container.innerHTML = `<p>No appointments found.</p>`;
                return;
            }
            renderHelper(data);
        })
        .catch(err => {
            console.error("Failed to fetch appointments:", err);
            container.innerHTML = `<p>Failed to load appointments.</p>`;
        });
}

function renderHelper(data) {
    const container = document.getElementById("appointments");

    data.forEach(appt => {
        const isScheduled = appt.status.toLowerCase() === 'scheduled';

        container.innerHTML += `
            <div class="appointment-card">
                <a href="/appointment/${appt.id}">
                    <div class="appt-date">
                        📅 ${formatDate(appt.availableSlot.startTime)}
                    </div>
                    <strong>
                        🕐 ${formatTime(appt.availableSlot.startTime, appt.availableSlot.endTime)}
                        — ${appt.availableSlot.provider.title}. ${appt.availableSlot.provider.lastName}
                    </strong>
                    <p>👤 ${appt.patient.firstName} ${appt.patient.lastName}</p>
                    <p>🏥 ${appt.service} — <span class="status-${appt.status.toLowerCase()}">${appt.status}</span></p>
                </a>
                <button 
                    class="btn-delete" 
                    onclick="${isScheduled ? `deleteAppt(${appt.id})` : ''}"
                    ${isScheduled ? '' : 'disabled'}
                >Cancel Appointment</button>
            </div>
        `;
    });
}

function deleteAppt(apptId) {
    if (!confirm("Are you sure you want to cancel this appointment?")) return;

    fetch(`/appointments/${apptId}/cancel`, {
        method: "PUT"
    })
    .then(res => {
        if (!res.ok) throw new Error("Failed to cancel");
        return res.text();
    })
    .then(msg => {
        if (window.location.pathname.startsWith("/appointment/")) {
            showToast(msg);
            setTimeout(() => window.location.reload(), 1500);
        } else {
            showToast(msg);
            fetchSlots();
            renderAppointments();
        }
    })
    .catch(err => {
        console.error("Delete failed:", err);
        showToast("Failed to cancel appointment.", "error");
    });
}