#include <UIPEthernet.h>

// --- Network Configuration ---
// MAC address for the Ethernet module (can be arbitrary, but must be unique on your local network)
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };

EthernetClient client;

// --- Backend Configuration ---
// Replace with the local IP address of your computer running Spring Boot
IPAddress server(192, 168, 0, 37);
int port = 8080;

// --- Timer Variables ---
unsigned long lastConnectionTime = 0;             // Last time you connected to the server, in milliseconds
const unsigned long postingInterval = 5000;       // Delay between updates, in milliseconds (5 seconds)

int dummySensorData = 100;







void setup() {
  Serial.begin(9600);
  Serial.println("Initializing ENC28J60 Ethernet...");

  // Force a static IP address in the 192.168.0.x range.
  // Make sure .50 isn't already used by another device on your network.
  IPAddress staticIP(192, 168, 0, 50);

  // Initialize Ethernet with the MAC and Static IP, bypassing DHCP entirely
  Ethernet.begin(mac, staticIP);

  Serial.print("Arduino IP Address forced to: ");
  Serial.println(Ethernet.localIP());

  delay(1000);
}





void loop() {
  // 1. Read incoming data from the server
  // It is crucial to read the response from the server, even if you do nothing with it.
  // Otherwise, the incoming buffer fills up and the ENC28J60 will freeze.
  if (client.available()) {
    char c = client.read();
    Serial.print(c); // Print the server response (e.g., HTTP 200 OK) to the Serial Monitor
  }

  // 2. Check if 5 seconds have passed since the last request
  // We use millis() instead of delay(5000) so the Arduino doesn't completely freeze
  // between requests, allowing it to process the server response above.
  if (millis() - lastConnectionTime > postingInterval) {

    // Simulate a changing value for the sake of the demonstration
    dummySensorData++;
    if (dummySensorData > 250) dummySensorData = 100;

    sendPostRequest(dummySensorData);
  }
}

void sendPostRequest(int valToSend) {
  // Ensure any previous connection is closed before attempting a new one
  client.stop();

  Serial.print("\nConnecting to Spring Boot server... Sending value: ");
  Serial.println(valToSend);

  // Attempt to connect to the backend
  if (client.connect(server, port)) {
    Serial.println("Connected!");

    // Construct the payload as x-www-form-urlencoded (e.g., "value=123")
    String postData = "value=" + String(valToSend);

    // --- Send standard HTTP POST request headers ---
    client.println("POST /data HTTP/1.1");
    client.print("Host: ");
    client.println(server);
    client.println("Content-Type: application/x-www-form-urlencoded");
    client.println("Connection: close"); // Tell server to drop connection after responding
    client.print("Content-Length: ");
    client.println(postData.length());

    // An empty line marks the end of the headers and the beginning of the body
    client.println();

    // --- Send the payload ---
    client.println(postData);

    // Record the time this request was sent to reset the 5-second timer
    lastConnectionTime = millis();

    Serial.println("POST request dispatched.");
  } else {
    Serial.println("Connection failed. Is the Spring Boot server running and accessible?");
  }
}
