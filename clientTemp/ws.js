const socket = new SockJS("http://localhost:8080/ws");
const stompClient = Stomp.over(socket);

stompClient.connect({}, () => {
    stompClient.subscribe("/topic/sensorData", (message) => {
        // TODO: received data . message.body
    });

    stompClient.subscribe("/topic/deviceStatus", (message) => {
        // TODO: received data . message.body
    });
})