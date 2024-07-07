let allowsNotifications = false

let socket = null

function connect() {
    if(!allowsNotifications) {
        Notification.requestPermission().then(() => {
            allowsNotifications = true
        })
    }
    console.log("Connecting....")
    socket = new WebSocket("ws://localhost:8080/simple/websocket");

    socket.addEventListener("open", (event) => {
        setConnected(true)
        console.log("Opened websocket!")
    });

    socket.addEventListener("close", (event) => {
        setConnected(false)
        console.log("Closed websocket!")
    });

    socket.addEventListener("error", (event) => {
        console.error(event)
    })

    socket.addEventListener("message", (event) => {
        console.log("Message from server ", event);
        const data = JSON.parse(event.data)
        showMessage(data);

        if(data.sender !== "you") {
            const options = {
                body: data.data,
                icon: "/websocket.png"
            }
            const notif = new Notification("New message!", options)
            document.addEventListener("visibilitychange", () => {
                if (document.visibilityState === "visible") {
                    // The tab has become visible so clear the now-stale Notification.
                    notif.close();
                }
            });
        }
    });
}


function showMessage(message) {
    $("#messages").append("<tr><td class='"+ message.sender + "'>" + message.data + "</td></tr>");
}

function sendName() {
    console.log("Sending....")
    socket.send(JSON.stringify({'data': $("#message").val()}))
}

function disconnect() {
    console.log("Disconnecting....")
    socket.close()
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#messages").html("");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => sendName());
});
