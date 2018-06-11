#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <FirebaseCloudMessaging.h>

#define MY_ID "B-001"
#define FIREBASE_SERVER "key=AAAANPJGjYg:APA91bEYF1DMuycUuAeVBM2mCcgjLi3ljb6a5HdsD__JI3-nutS0Fn6gc4G0enREO1_PNCsfWT9ELk4ZrOIq8I7Wui-MAn4e9MyrMN7VgnIEQ1W9U8VTWU58k1Z4xGJB9nUh9qBbV7uz"

//#define WIFI_SSID "HOME-1E42"  
//#define WIFI_PASSWORD "EFE70BA3EB65DEBB"

#define WIFI_SSID "Pedro"  
#define WIFI_PASSWORD "12345678"

#define trigPinTanque D5
#define echoPinTanque D6
#define trigPinCoca D8
#define echoPinCoca D7
#define relePin D1

#define distanciaCocaVacia 26
#define distanciaTanqueVacio 23
#define distanciaCocaLlena 17
#define distanciaTanqueLleno 4

float distanciaCoca;
float distanciaTanque;
unsigned int porcentajeTanque;
unsigned int porcentajeCoca;
unsigned int lastPorcentajeTanque;
 
void setup(){
  Serial.begin(74880);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Conectando a ");
  Serial.print(WIFI_SSID);
  pinMode(trigPinCoca, OUTPUT);
  pinMode(echoPinCoca, INPUT);
  pinMode(trigPinTanque, OUTPUT);
  pinMode(echoPinTanque, INPUT);
  pinMode(relePin, OUTPUT);
}
 
void loop(){
  delay (3000);

  actualizarDatos();
  
  if (porcentajeTanque > 100 || porcentajeCoca > 100) return;
  
  if (porcentajeCoca < 50) {
    llenarCoca();
    return;
  }
  int diferencia = porcentajeTanque - lastPorcentajeTanque;
  Serial.print("Dif: ");
  Serial.println(diferencia);
  if (diferencia > 20 || diferencia < -5) {
    lastPorcentajeTanque = porcentajeTanque;
    sendFirebaseNotification();
  }
}

void llenarCoca() {
  digitalWrite(relePin, HIGH);
  while (porcentajeCoca < 90) {
    distanciaCoca = getDistance (trigPinCoca, echoPinCoca);
    porcentajeCoca = 100 - (100 * (distanciaCoca - distanciaCocaLlena) / (distanciaCocaVacia - distanciaCocaLlena));
    Serial.print("LLenando....Distancia Coca: ");
    Serial.println(distanciaCoca);
    Serial.print("LLenando....Porcentaje  coca: ");
    Serial.println(porcentajeCoca);
    delay(500);
  }
  digitalWrite(relePin, LOW);
}

void actualizarDatos () {
  distanciaTanque = getDistance (trigPinTanque, echoPinTanque);
  porcentajeTanque = 100 - (100 * (distanciaTanque - distanciaTanqueLleno) / (distanciaTanqueVacio - distanciaTanqueLleno));
  distanciaCoca = getDistance (trigPinCoca, echoPinCoca);
  porcentajeCoca = 100 - (100 * (distanciaCoca - distanciaCocaLlena) / (distanciaCocaVacia - distanciaCocaLlena));
  Serial.print("Distancia tanque: ");
  Serial.println(distanciaTanque);
  Serial.print("Porcentaje tanque: ");
  Serial.println(porcentajeTanque);
  Serial.print("Distancia Coca: ");
  Serial.println(distanciaCoca);
  Serial.print("Porcentaje  coca: ");
  Serial.println(porcentajeCoca);
}


