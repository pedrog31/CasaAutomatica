#include <FirebaseArduino.h>
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <Q2HX711.h>
#include "ListLib.h"
#include "HoraProgramada.h"

#define MY_ID "C-001"
#define FIREBASE_SERVER "key=AAAANPJGjYg:APA91bEYF1DMuycUuAeVBM2mCcgjLi3ljb6a5HdsD__JI3-nutS0Fn6gc4G0enREO1_PNCsfWT9ELk4ZrOIq8I7Wui-MAn4e9MyrMN7VgnIEQ1W9U8VTWU58k1Z4xGJB9nUh9qBbV7uz"
#define FIREBASE_HOST "comedero-automatico.firebaseio.com"
#define FIREBASE_AUTH "Qqgu0slvmPQmPS1e4vMBTaSpkZxIw0YkU74XF86r"
#define HOUR_URL "http://api.geonames.org/timezoneJSON?lat=6.2530408&lng=-75.5645737&username=pedrog31"

//#define WIFI_SSID "HOME-1E42"
//#define WIFI_PASSWORD "EFE70BA3EB65DEBB"

#define WIFI_SSID "Pedro"
#define WIFI_PASSWORD "12345678"

#define trigPinTanque D8
#define echoPinTanque D7
#define DTPinCoca D6
#define SCKPinCoca D5

#define encoderPin D0

#define motorSinFinAVelocidad D1
#define motorSinFinADireccion D3
#define motorAuxBVelocidad D2
#define motorAuxBDireccion D4

#define distanciaTanqueVacio 26
#define distanciaTanqueLleno 4
#define escalaGramera 2036.9
#define taraGramera 4362.9
#define diaMillis 86400000

float distanciaTanque;
unsigned int porcentajeTanque;
unsigned int lastPorcentajeTanque;
float pesoActual;
long setupMillis;
List<HoraProgramada> list;
Q2HX711 gramera(DTPinCoca,SCKPinCoca);

void setup() {
  Serial.begin(74880);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  cuadrarHora ();
  actualizarProgramacion();
  calibrarGramera();
  Serial.println();
  Serial.print("Conectado: ");
  Serial.println(WiFi.localIP());
  pinMode(trigPinTanque, OUTPUT);
  pinMode(echoPinTanque, INPUT);
  pinMode(encoderPin, INPUT);
  pinMode(motorSinFinAVelocidad, OUTPUT);
  pinMode(motorSinFinADireccion, OUTPUT);
  pinMode(motorAuxBVelocidad, OUTPUT);
  pinMode(motorAuxBDireccion, OUTPUT);
  actualizarDatos();
  if (porcentajeTanque <= 10) {
    sendFirebaseNotification("null");
  }
  attachInterrupt(digitalPinToInterrupt(encoderPin), counter, RISING);
}

void loop() {
  delay (1000);
  if (setupMillis + millis() < diaMillis) {
    actualizarDatos();
    verificarProgramacion();
    if (porcentajeTanque > 100) return;
    int diferencia = porcentajeTanque - lastPorcentajeTanque;
    Serial.print("Dif: ");
    Serial.println(diferencia);
    if (diferencia > 20 || diferencia < -5) {
      lastPorcentajeTanque = porcentajeTanque;
      sendFirebaseNotification("null");
    }
  } else {
    setupMillis -= diaMillis;
    actualizarProgramacion();
  }
  getPeso();
  Serial.print("Peso actual");
  Serial.println(pesoActual);
}

void llenarCoca(int food) {
  getPeso();
  if (pesoActual >= food) {
    sendFirebaseNotification(String("Tu mascota no a comido su anterior comida"));
  } else {
    sendFirebaseNotification(String("Estamos alimentando a tu mascota"));
    int i = 0;
    analogWrite(motorSinFinAVelocidad, 800);
    digitalWrite(motorSinFinADireccion, HIGH);
    delay(2000);
    while (pesoActual < food) {
      analogWrite(motorSinFinAVelocidad, 650);
      digitalWrite(motorSinFinADireccion, HIGH);
      analogWrite(motorAuxBVelocidad, 700);
      digitalWrite(motorAuxBDireccion, HIGH);
      delay (150);
      digitalWrite(motorAuxBDireccion, LOW);
      delay (150);
      analogWrite(motorAuxBVelocidad, LOW);
      delay (3000);
      digitalWrite(motorSinFinAVelocidad, HIGH);
      digitalWrite(motorSinFinADireccion, LOW);
      delay(1500);
      analogWrite(motorSinFinAVelocidad, 800);
      digitalWrite(motorSinFinADireccion, HIGH);
      delay(2500);
      digitalWrite(motorSinFinAVelocidad, HIGH);
      digitalWrite(motorSinFinADireccion, LOW);
      delay (500);
      getPeso();
    }
    analogWrite(motorAuxBVelocidad, 0);
    analogWrite(motorSinFinAVelocidad, 0);
  }
}

void actualizarDatos () {
  distanciaTanque = getDistance (trigPinTanque, echoPinTanque);
  porcentajeTanque = 100 - (100 * (distanciaTanque - distanciaTanqueLleno) / (distanciaTanqueVacio - distanciaTanqueLleno));
  Serial.print("Distancia tanque: ");
  Serial.println(distanciaTanque);
  Serial.print("Porcentaje tanque: ");
  Serial.println(porcentajeTanque);
}

void verificarProgramacion() {
  Serial.print("Verificando programacion");
  Serial.println(list.Count());
  for (int i = 0; i < list.Count(); i++) {
    if (!list[i].getIsCompleted()) {
      Serial.print("Hora");
      Serial.println(list[i].getHora());
      int dif = list[i].getHora() - (setupMillis + millis());
      Serial.print("Diferencia:");
      Serial.println(dif);
      if (dif <= 0) {
        if (dif >= -60000) llenarCoca(list[i].getFood ());
        list[i].setIsCompleted(true);
      }
    }
  }
}



