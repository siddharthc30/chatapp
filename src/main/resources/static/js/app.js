var stompClient = null;

function connect(username) {
    var socket = new SockJS('/hello');
    stompClient = Stomp.over(socket);

    stompClient.connect({ username: username }, function(frame) {
        console.log('Web Socket is connected: ' + frame);

        // Subscribe to the user-specific message queue
        stompClient.subscribe('/users/queue/messages', function(message) {
            // Display the message in the message area
            var messageDiv = document.createElement('div');
            messageDiv.textContent = message.body;
            document.getElementById("message").appendChild(messageDiv);
        });
    }, function(error) {
        console.error('WebSocket connection error: ' + error);
    });
}

// Event listeners for form submission and button clicks
document.addEventListener('DOMContentLoaded', function() {
    var connectButton = document.getElementById("connect");
    var sendButton = document.getElementById("send");
    var usernameInput = document.getElementById("username");
    var messageInput = document.getElementById("name");

    connectButton.addEventListener('click', function() {
        var username = usernameInput.value;
        if (username) {
            connect(username);
        } else {
            alert("Please enter a username.");
        }
    });

    sendButton.addEventListener('click', function() {
        var messageContent = messageInput.value;
        if (messageContent && stompClient) {
            stompClient.send("/app/hello", {}, messageContent);
            messageInput.value = ''; // Clear the input field after sending
        } else {
            alert("Please enter a message to send.");
        }
    });

    // Prevent form submission
    document.querySelector("form").addEventListener('submit', function(e) {
        e.preventDefault();
    });
});
