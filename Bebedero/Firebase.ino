void sendFirebaseNotification() {
  while (WiFi.status() != WL_CONNECTED) {
    Serial.println("Conectando");
    delay(500);
  }
  Serial.println();
  Serial.print("Conectado: ");
  Serial.println(WiFi.localIP());
  HTTPClient http;
  http.begin("http://fcm.googleapis.com/fcm/send");
  http.addHeader("Content-Type", "application/json");
  http.addHeader("Authorization", FIREBASE_SERVER);
  http.addHeader("Host", "fcm.googleapis.com");
  String body = String("{")
                +    "\"to\": \"/topics/" + MY_ID + "\","
                +    "\"data\": {"
                +       "\"ID\": \"" + MY_ID + "\","
                +       "\"Porcentaje\": " + porcentajeTanque
                +     "}}";
  Serial.println(body);
  int httpCode = http.POST(body);
   String payload = http.getString();
   Serial.println(httpCode);
   Serial.println(payload);
   Serial.println();
   http.end();
}

/*
String[] saveFirebaseTokens() {
  String node = "Dispositivos/" + String(MY_ID);
  FirebaseObject child = Firebase.get(node);
  if (Firebase.failed()) {
      Serial.print("Firebase failed:");
      Serial.println(Firebase.error());  
      return;
  }
  JsonObject& root = child.getJsonVariant();
  char jsonChar[200];
  root.printTo((char*)jsonChar, root.measureLength() + 1);
  char *users[4];
  char *p = jsonChar;
  char *str;
  byte i=0;
  byte j=0;
  while ((str = strtok_r(p, "\"", &p)) != NULL) {
    if (i % 2 == 1) {
      users[j] = str;
      j++;
    }
   i++;
  }
  Serial.println(users[0]);
  Serial.println(users[1]);  
  String token[j];
  for (i=0; i<j; i++) {
    token[i] = Firebase.getString(String("Token/") + users[i]);
    if (Firebase.failed()) {
      Serial.print("Firebase failed:");
      Serial.println(Firebase.error());  
      return;
    }
    Serial.println(token[i]);
  }
  return token;
}*/
