#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
// Set these to run example.
#define FIREBASE_HOST "comedero-automatico.firebaseio.com"
#define FIREBASE_AUTH "Qqgu0slvmPQmPS1e4vMBTaSpkZxIw0YkU74XF86r"
//Change line with your WiFi router name and password
#define WIFI_SSID "HOME-1E42"  
#define WIFI_PASSWORD "EFE70BA3EB65DEBB"
void setup() {
Serial.begin(9600);
// connect to wifi.
WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
Serial.print("connecting");
while (WiFi.status() != WL_CONNECTED) {
Serial.print(".");
delay(500);
}
Serial.println();
Serial.print("connected: ");
Serial.println(WiFi.localIP());
Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
}
int n = 0;
void loop() {
String name = Firebase.pushInt("logs", n++);
// handle error
if (Firebase.failed()) {
Serial.print("pushing /logs failed:");
Serial.println(Firebase.error());  
return;
}
Serial.print("pushed: /logs/");
Serial.println(name);
}
