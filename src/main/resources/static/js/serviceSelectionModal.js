function openServiceModal(slotId) {
    const container = document.querySelector(".service-options");
    container.innerHTML = ""; // clear previous

    fetch('/services')
        .then(res => res.json())
        .then(data => {
            data.forEach(s => {
                container.innerHTML += `
                    <label class="service-option">
                        <input type="radio" name="service" value="${s.name}" />
                        <span class="service-name">${s.name}</span>
                        <span class="service-duration">${s.duration} hour</span>
                        <span class="service-fee">$${s.fee}</span>
                    </label>
                `;
            });
        })
        .catch(err => {
            console.error("Failed to load services:", err);
            container.innerHTML = "<p>Failed to load services.</p>";
        });

    document.getElementById("confirmBookingBtn").onclick = function() {
        const service = document.querySelector('input[name="service"]:checked')?.value;
        if (!service) {
            alert("Please select a service.");
            return;
        }
        closeServiceModal();
        sendBookingRequest(slotId, service);
    };

    document.getElementById("serviceModal").style.display = "flex";
}

function closeServiceModal() {
    document.getElementById("serviceModal").style.display = "none";
}