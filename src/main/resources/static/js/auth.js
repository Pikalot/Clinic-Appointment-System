// ── SESSION ──
let activeLoginRole = "patient";

// ── UPDATE NAVBAR STATE ──
function updateNavbar() {
    const role = sessionStorage.getItem("userRole");
    const mrn = sessionStorage.getItem("userMrn");
    const userId = sessionStorage.getItem("userId");
    const firstName = sessionStorage.getItem("userFirstName");

    const userStatus = document.getElementById("user-status");
    const userAvatar = document.getElementById("user-avatar");
    const userRoleLabel = document.getElementById("user-role-label");
    const loginForm = document.getElementById("login-form");
    const logoutForm = document.getElementById("logout-form");
    const drawerTitle = document.getElementById("drawer-title");

    if (role) {
        // Logged in — show avatar, show logout form
        userStatus.style.display = "flex";
        userAvatar.textContent = role.charAt(0); // "P" or "S"
        userRoleLabel.textContent = role;

        loginForm.style.display = "none";
        logoutForm.style.display = "block";
        drawerTitle.textContent = "My Account";

        // Update logout info
        document.getElementById("logout-avatar").textContent = role.charAt(0);
        document.getElementById("logout-name").textContent = `First Name: ${firstName}`;
        document.getElementById("logout-role").textContent = role;

    } else {
        // Not logged in — hide avatar, show login form
        userStatus.style.display = "none";
        loginForm.style.display = "block";
        logoutForm.style.display = "none";
        drawerTitle.textContent = "Sign In";
    }
}

// ── TAB SWITCHING ──
function switchLoginTab(tab) {
    activeLoginRole = tab;
    document.getElementById("tab-patient-btn").classList.toggle("active", tab === "patient");
    document.getElementById("tab-staff-btn").classList.toggle("active", tab === "staff");
    document.getElementById("login-username").value = "";
    document.getElementById("login-password").value = "";
}

// ── LOGIN ──
function login() {
    const username = document.getElementById("login-username").value;
    const password = document.getElementById("login-password").value;

    if (!username || !password) {
        alert("Please enter username and password!");
        return;
    }

    const endpoint = activeLoginRole === "patient"
        ? "/auth/login/patient"
        : "/auth/login/staff";

    fetch(endpoint, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    })
    .then(res => {
        if (!res.ok) throw new Error("Invalid credentials");
        return res.json();
    })
    .then(data => {
        sessionStorage.setItem("userRole", data.role);
        sessionStorage.setItem("userMrn", data.mrn || "");
        sessionStorage.setItem("userId", data.userId || "");
        sessionStorage.setItem("userFirstName", data.firstName || "");
        toggleDrawer();
        updateNavbar();
        location.reload();
    })
    .catch(() => alert("Login failed. Please check your credentials."));
}

// ── LOGOUT ──
function logout() {
    sessionStorage.removeItem("userRole");
    sessionStorage.removeItem("userMrn");
    sessionStorage.removeItem("userId");
    toggleDrawer();
    updateNavbar();
    location.reload();
}

// ── DRAWER ──
function toggleDrawer() {
    document.getElementById('drawer').classList.toggle('open');
    document.getElementById('overlay').classList.toggle('open');
}

function setType(btn, type) {
    document.querySelectorAll('.toggle-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
}

// ── INIT ──
updateNavbar();