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
        return res.text();
    })
    .then(msg => {
        showToast(msg);
        fetchSlots();
    })
    .catch(err => {
        console.error("Delete failed:", err);
        showToast(err, "error");
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

// ── CREATE SLOT MODAL ──
function openCreateSlotModal() {
    document.getElementById("errorModal").textContent = "";
    document.getElementById("slotDate").value = selectedDate;
    populateModalProviders();
    document.getElementById("createSlotModal").style.display = "flex";
}

function closeCreateSlotModal() {
    document.getElementById("errorModal").style.display = "";
    document.getElementById("createSlotModal").style.display = "none";
}

function populateModalProviders() {
    const select = document.getElementById("slotProvider");
    select.innerHTML = '<option value="">Select Provider</option>';
    fetch("/providers")
        .then(res => res.json())
        .then(data => {
            data.forEach(p => {
                select.innerHTML += `<option value="${p.id}">${p.firstName} ${p.lastName}</option>`;
            });
        });
}

function submitCreateSlot() {
    const date     = document.getElementById("slotDate").value;
    const start    = document.getElementById("slotStart").value;
    const duration = parseInt(document.getElementById("slotDuration").value);
    const provider = document.getElementById("slotProvider").value;

    if (!date || !start || !provider) {
        document.getElementById("errorModal").textContent = "Please fill in all fields.";
        return;
    }

    const [hours, minutes] = start.split(":").map(Number);
    const endDate = new Date(0, 0, 0, hours, minutes + duration);
    const end = `${String(endDate.getHours()).padStart(2, "0")}:${String(endDate.getMinutes()).padStart(2, "0")}`;

    const payload = {
        startTime:  `${date}T${start}:00`,
        endTime:    `${date}T${end}:00`,
        providerId: provider
    };

    fetch("/slots", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
    .then(res => {
        if (!res.ok) throw new Error("Failed to create slot");
        return res.text();
    })
    .then(msg => {
        showToast(msg || "Slot created successfully!");
        closeCreateSlotModal();
        fetchSlots();
    })
    .catch(err => {
        console.error("Create slot failed:", err);
        document.getElementById("errorModal").textContent = "Error: Failed to create slot. Please try again";
    });
}

// Close modal on overlay click
document.getElementById("createSlotModal").addEventListener("click", function (e) {
    if (e.target === this) closeCreateSlotModal();
});