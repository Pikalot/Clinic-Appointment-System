// ── BOOKING ──
function bookSlot(slotId) {
    if (!role) {
        toggleDrawer(); // open drawer instead of navigating
        return;
    }
    openServiceModal(slotId);
}

function sendBookingRequest(slotId, serviceId) {
    const payload = {
        mrn: mrn,
        slotId: slotId,
        serviceId: serviceId
    };

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
            showToast(text, "error");
            return;
        }
        showToast(text);
        renderAppointments();
        fetchSlots();
    })
    .catch(err => {
        console.error("Booking failed:", err);
        showToast("Booking failed. Please try again.", "error");
    });
}



