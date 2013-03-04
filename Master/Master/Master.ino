/*
 Gatech SmartLock Controller
 
 */

/*
  WiFi Web Server LED Blink
 
 A simple web server that lets you blink an LED via the web.
 This sketch will print the IP address of your WiFi Shield (once connected)
 to the Serial monitor. From there, you can open that address in a web browser
 to turn on and off the LED on pin 9.
 
 If the IP address of your shield is yourAddress:
 http://yourAddress/H turns the LED on
 http://yourAddress/L turns it off
 
 This example is written for a network using WPA encryption. For 
 WEP or WPA, change the Wifi.begin() call accordingly.
 
 Circuit:
 * WiFi shield attached
 * LED attached to pin 9
 
 created 25 Nov 2012
 by Tom Igoe
 */
#include <SPI.h>
#include <WiFi.h>

#define SPKR 5 //Digital pin for the PCS Speaker

char ssid[] = "Smart";      //  your network SSID (name) 
char pass[] = "oxford2012";   // your network password
int keyIndex = 0;                 // your network key Index number (needed only for WEP)
boolean reading = false;
String command = "";

int status = WL_IDLE_STATUS;
WiFiServer server(80);

void beep(unsigned char delayms){
  analogWrite(SPKR, 20);      // Almost any value can be used except 0 and 255
                           // experiment to get the best tone
  delay(delayms);          // wait for a delayms ms
  analogWrite(SPKR, 0);       // 0 turns it off
  delay(delayms);          // wait for a delayms ms   
}

void beepMultiple(unsigned int numTimes, unsigned char delayms){
 for (int i = 0; i < numTimes; i++){
   beep(delayms); 
 }
 
 
  
}

void setup() {
  Serial.begin(9600);      // initialize serial communication
  pinMode(9, OUTPUT);      // set the LED pin mode
  pinMode(SPKR, OUTPUT);

  // check for the presence of the shield:
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present"); 
    while(true);        // don't continue
  } 

  // attempt to connect to Wifi network:
  while ( status != WL_CONNECTED) { 
    Serial.print("Attempting to connect to Network named: ");
    Serial.println(ssid);                   // print the network name (SSID);

    // Connect to WPA/WPA2 network. Change this line if using open or WEP network:    
    status = WiFi.begin(ssid, pass);
    // wait 4 seconds for connection:
    
    delay(4000);
  } 
  server.begin();                           // start the web server on port 80
  printWifiStatus();                        // you're connected now, so print out the status
  beepMultiple(3,50);
}


void loop() {
  wifiFullLineCommands();
}


void wifiFullLineCommands(){
  WiFiClient client = server.available();   // listen for incoming clients
  if (client) {

    // an http request ends with a blank line
    boolean currentLineIsBlank = true;
    boolean sentHeader = false;
    beepMultiple(5,100);
    
    while (client.connected()) {
      if (client.available()) {

        if(!sentHeader){
          // send a standard http response header
          client.println("HTTP/1.1 200 OK");
          client.println("Content-Type: text/html");
          client.println();
          sentHeader = true;
        }

        char c = client.read();

        if(reading && c == ' ') reading = false;
        if(c == '?') reading = true; //found the ?, begin reading the info

        if(reading){
          Serial.print(c);
          if(c != '?'){
           command += c;
          }
           

        }

        if (c == '\n' && currentLineIsBlank)  break;

        if (c == '\n') {
          currentLineIsBlank = true;
        }else if (c != '\r') {
          currentLineIsBlank = false;
        }

      }
    }
    client.println(command);
    delay(1); // give the web browser time to receive the data
    client.stop(); // close the connection:

  } 
  
}

void triggerPin(int pin, WiFiClient client){
//blink a pin - Client needed just for HTML output purposes.  
  client.print("Turning on pin ");
  client.println(pin);
  client.print("<br>");

  digitalWrite(pin, HIGH);
  delay(25);
  digitalWrite(pin, LOW);
  delay(25);
}


void printWifiStatus() {
  // print the SSID of the network you're attached to:
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  // print your WiFi shield's IP address:
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);

  // print the received signal strength:
  long rssi = WiFi.RSSI();
  Serial.print("signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");
  // print where to go in a browser:
  Serial.print("To see this page in action, open a browser to http://");
  Serial.println(ip);
}
