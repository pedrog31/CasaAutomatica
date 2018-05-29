#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

#define FIREBASE_HOST "comedero-automatico.firebaseio.com"
#define FIREBASE_AUTH "Qqgu0slvmPQmPS1e4vMBTaSpkZxIw0YkU74XF86r"

#define WIFI_SSID "Pedro"  
#define WIFI_PASSWORD "12345678"

#define trigPinTanque D7
#define echoPinTanque D8
#define trigPinCoca D2
#define echoPinCoca D3
#define relePin D1
 
void setup(){
  Serial.begin(9600);
  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Conectando a ");
  Serial.print(WIFI_SSID);
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  pinMode(trigPinCoca, OUTPUT);
  pinMode(echoPinCoca, INPUT);
  pinMode(trigPinTanque, OUTPUT);
  pinMode(echoPinTanque, INPUT);
  pinMode(relePin, OUTPUT);
}
 
void loop(){
  Serial.println(getDistance (trigPinTanque, echoPinTanque));
  delay(2000);
  Serial.println(getDistance (trigPinCoca, echoPinCoca));
  delay(2000);
}
