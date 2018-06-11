void calibrarGramera() {
  while (!gramera.readyToSend()) {
    Serial.println("No ponga peso en la gramera");
  }
}

void getPeso () {
  pesoActual = 0.0;
  for (byte i=0; i<20; i++) {
    pesoActual += (gramera.read() /escalaGramera) - taraGramera;
    delay(50);
  }
  pesoActual /= 20;
}


