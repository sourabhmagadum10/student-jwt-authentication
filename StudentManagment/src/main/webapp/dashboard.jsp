
<%@ page session="true" %>
<%
    if (session.getAttribute("userId") == null) {
        response.sendRedirect("login.html");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Student Dashboard</title>

    <style>
    html {
        visibility: hidden;
    }
</style>
</head>
<body>
    <h2>Welcome, <%= session.getAttribute("userName") %></h2>
    <h3>Student Dashboard</h3>

    <a href="logout">Logout</a>

    <script>
        
    if (!sessionStorage.getItem("tabId")) {
        sessionStorage.setItem(
            "tabId",
            crypto.randomUUID()
        );
    }


    const tabId = sessionStorage.getItem("tabId");

    console.log("Calling claim-tab...");
console.log("Tab ID:", tabId);
    fetch("<%= request.getContextPath() %>/claim-tab", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({
            tabId: tabId
        })
    })
    .then(response => {
        if (response.ok) {
            document.documentElement.style.visibility = "visible";
        } else {
            document.body.innerHTML =
                "<h2>Access Denied</h2>" +
                "<p>This account is already active in another tab.</p>" +
                "<p>Please use the original tab or log out first.</p>";

            document.documentElement.style.visibility = "visible";
        }
    })
    .catch(error => {
        document.body.innerHTML =
            "<h2>Unable to verify your tab. Please try again.</h2>";

        document.documentElement.style.visibility = "visible";
    });
</script>
<script>
    function sendHeartbeat() {
        fetch("<%= request.getContextPath() %>/heartbeat", {
            method: "POST"
        })
        .then(response => {
            if (response.status === 401) {
                window.location.href =
                    "<%= request.getContextPath() %>/login.html?error=sessionexpire";
            }
        })
        .catch(error => {
            console.log("Heartbeat failed:", error);
        });
    }

    // Send heartbeat immediately
    sendHeartbeat();

    // Send heartbeat every 5 seconds
    setInterval(sendHeartbeat, 5000);
</script>
</body>
</html>
