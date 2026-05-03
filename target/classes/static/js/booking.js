// ── BOOKING ──
function bookSlot(slotId) {
    if (!role) {
        toggleDrawer(); // open drawer instead of navigating
        return;
    }
    openServiceModal(slotId);
}

function sendBookingRequest(slotId, serviceId) {
    const payload = { mrn, slotId, serviceId };

    fetch('/appointments', {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
    .then(res => {
        return res.text().then(text => ({ ok: res.ok, text }));
    })
    .then(({ ok, text }) => {
        if (!ok) {
            alert(text);
            return;
        }
        alert(text);
        renderAppointments();
        fetchSlots();
    })
    .catch(err => {
        console.error("Booking failed:", err);
        alert("Booking failed. Please try again.");
    });
}

// ── CREATE SLOT MODAL ──
function openCreateSlotModal() {
    document.getElementById("slotDate").value = selectedDate;
    populateModalProviders();
    document.getElementById("createSlotModal").style.display = "flex";
}

function closeCreateSlotModal() {
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

function populateModalClinics() {
    const select = document.getElementById("slotClinic");
    select.innerHTML = '<option value="">Select Clinic</option>';
    fetch("/clinics")
        .then(res => res.json())
        .then(data => {
            data.forEach(c => {
                select.innerHTML += `<option value="${c.id}">${c.name}</option>`;
            });
        });
}

function submitCreateSlot() {
    const date     = document.getElementById("slotDate").value;
    const start    = document.getElementById("slotStart").value;
    const duration = parseInt(document.getElementById("slotDuration").value);
    const provider = document.getElementById("slotProvider").value;

    if (!date || !start || !provider) {
        alert("Please fill in all fields.");
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
        alert(msg || "Slot created successfully!");
        closeCreateSlotModal();
        fetchSlots();
    })
    .catch(err => {
        console.error("Create slot failed:", err);
        alert("Failed to create slot. Please try again.");
    });
}

// Close modal on overlay click
document.getElementById("createSlotModal").addEventListener("click", function (e) {
    if (e.target === this) closeCreateSlotModal();
});

