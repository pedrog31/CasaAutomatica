void sendFirebaseNotification(String messaje) {
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }
  HTTPClient http;
  http.begin("http://fcm.googleapis.com/fcm/send");
  http.addHeader("Content-Type", "application/json");
  http.addHeader("Authorization", FIREBASE_SERVER);
  http.addHeader("Host", "fcm.googleapis.com");
  String body = String("{")
                +    "\"to\": \"/topics/" + MY_ID + "\","
                +    "\"data\": {"
                +       "\"ID\": \"" + MY_ID + "\","
                +       "\"Porcentaje\": " + porcentajeTanque + ","
                +       "\"Messaje\": \"" + messaje + "\""
                +     "}}";
  Serial.println(body);
  int httpCode = http.POST(body);
   String payload = http.getString();
   Serial.println(httpCode);
   Serial.println(payload);
   Serial.println();
   http.end();
}

void cuadrarHora () {
   HTTPClient http;
   while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }
   http.begin(HOUR_URL);
   int httpCode = http.GET();
   String payload = http.getString();
   if (httpCode == 200) {
    int minutes = payload.substring(217,219).toInt();
    int hours = payload.substring(214,216).toInt();
    setupMillis = (hours * 3600000) + (minutes * 60000);
    Serial.print("Hora inicial");
    Serial.println(hours);
   }else {
    setupMillis = 0;
    Serial.print("Error obteniendo hora");
   }
   http.end();
}

//Agregar validacion de que si apago y vuelvo a a prender se me borra las comidas que ya di
void actualizarProgramacion() {
  String node = "Dispositivos/" + String(MY_ID);
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print("Reconectando.");
    delay(500);
  }
  String child = Firebase.getString(node);
  if (Firebase.failed()) {
      Serial.print("Firebase failed:");
      Serial.println(Firebase.error());  
      return;
  }
  int index = child.indexOf(",");
  int aux = -1;
  int i = index;
  do {
    HoraProgramada hora;
    hora.setHora(child.substring(aux+1, index));
    aux = index;
    index = child.indexOf(",", index+1);
    hora.setFood(child.substring(aux+1, index));
    aux = index;
    index = child.indexOf(",", index+1);
    list.Add(hora);
  }while (index != i);
  list.Trim();
}


