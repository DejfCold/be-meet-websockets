const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/stomp/websocket'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame, frame);
    stompClient.subscribe('/topic/greetings', (greeting) => {
        console.log(greeting.body)
        showGreeting(JSON.parse(greeting.body).content);
    });
    stompClient.subscribe('/user/queue/reply', (message) => {
        console.log("Reply:", message)
        showEcho(JSON.parse(message.body).content)
    })
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    console.log("Connecting....")
    stompClient.activate();
}

function disconnect() {
    console.log("Disconnecting....")
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    console.log("Sending....")
    stompClient.publish({
        destination: "/app/hello",
        body: JSON.stringify({'name': $("#name").val()})
    });
}
function sendEcho() {
    stompClient.publish({
        destination: "/app/personal",
        body: JSON.stringify({'name': $("#echoName").val()})
    })
}

function showGreeting(message, sender) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}
function showEcho(message, sender) {
    $("#echos").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
    $( "#sendEcho" ).click(() => sendEcho());
});