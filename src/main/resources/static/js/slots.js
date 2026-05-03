// ── SLOTS DATA ──
let slots = [];

function fetchSlots() {
    fetch("/slots")
        .then(res => res.json())
        .then(data => {
            slots = data.map(slot => ({
                id: slot.id,
                date: slot.startTime.split("T")[0],
                time: formatTime(slot.startTime, slot.endTime),
                doctor: `${slot.provider.firstName} ${slot.provider.lastName}`,
                type: slot.provider.type,
                clinic: slot.clinic.nameAndAddress
            }));
            renderCalendar();
        })
        .catch(err => {
            console.error("Failed to fetch slots:", err);
        });
}

// ── SLOTS ──
function renderSlots() {
    const container = document.getElementById("slots");
    const clinic = document.getElementById("clinicFilter").value;
    const doctor = document.getElementById("doctorFilter").value;

    const filteredSlots = slots.filter(slot => {
        const matchesDate   = slot.date === selectedDate;
        const matchesDoctor = doctor === "" || slot.doctor === doctor;   // empty string check
        const matchesClinic = clinic === "" || slot.clinic === clinic;   // empty string check
        return matchesDate && matchesClinic && matchesDoctor;
    });

    container.innerHTML = "";

    if (filteredSlots.length === 0) {
        container.innerHTML = `<p>No available slots for this date.</p>`;
        return;
    }

    filteredSlots.forEach(slot => {
        let actionBtn;

        if (!role) {
            // Not logged in → prompt login
            actionBtn = `<a href="#" onclick="toggleDrawer()" class="btn-login-prompt">Log in to book</a>`;
        } else if (role === "Patient") {
            // Patient → can book
            actionBtn = `<button class="btn-edit" onclick="bookSlot(${slot.id})">Book</button>`;
        } else {
            // Staff/Provider/Admin → can manage
            actionBtn = `
                <div class="slot-actions">
                    <button class="btn-edit" onclick="editSlot(${slot.id})">Edit</button>
                    <button class="btn-delete" onclick="deleteSlot(${slot.id})">Delete</button>
                </div>
            `;
        }

        container.innerHTML += `
            <div class="slot">
                <div class="slot-info">
                    <strong>${slot.time}</strong> - ${slot.doctor} (${slot.type})
                    <br>
                    ${slot.clinic}
                </div>
                ${actionBtn}
            </div>
        `;
    });
}

// ── SLOT FUNCTIONS ──
function editSlot(slotId) {
    // TODO: open edit modal or navigate to edit page
    alert(`Edit slot ${slotId} — coming soon!`);
}

function deleteSlot(slotId) {
    if (!confirm("Are you sure you want to delete this slot?")) return;

    fetch(`/slots/${slotId}/cancel`, {
        method: "PUT"
    })
    .then(res => {
        if (!res.ok) throw new Error("Failed to delete");
        fetchSlots();
    })
    .catch(err => {
        console.error("Delete failed:", err);
        alert("Failed to delete slot.");
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
                option.value = `${provider.firstName} ${provider.lastName}`;
                option.textContent = `${provider.firstName} ${provider.lastName}`;
                select.appendChild(option);
            });
        })
        .catch(err => {
            console.error("Failed to fetch providers:", err);
        });
}

// ── CLINICS DROPDOWN ──
function loadClinics() {
    const select = document.getElementById("clinicFilter");
    fetch("/clinics")
        .then(res => res.json())
        .then(data => {
            data.forEach(clinic => {
                const option = document.createElement("option");
                option.value = clinic.nameAndAddress; // must match slot.clinic
                option.textContent = clinic.name;
                select.appendChild(option);
            });
        })
        .catch(err => {
            console.error("Failed to fetch clinics:", err);
        });
}
