unsigned int rpm = 0;           // Revoluciones por minuto calculadas.
volatile byte pulses = 0;       // Número de pulsos leidos por el Arduino en un segundo
unsigned long timeold = 0;  // Tiempo 
unsigned int pulsesperturn = 20; // Número de muescas que tiene el disco del encoder.
const int wheel_diameter = 64;   // Diámetro de la rueda pequeña[mm]
static volatile unsigned long debounce = 0; // Tiempo del rebote.
float velocidad = 0.0;

bool getVelocity() {
      noInterrupts(); //Don't process interrupts during calculations 
      rpm = (60 * 1000 / pulsesperturn )/ (millis() - timeold)* pulses; // Calculamos las revoluciones por minuto
      timeold = millis(); // Almacenamos el tiempo actual.
      pulses = 0;  // Inicializamos los pulsos.
      interrupts(); // Restart the interrupt processing
      velocidad = rpm * 3.1416 * wheel_diameter * 0.00001666666; // Cálculo de la velocidad en [m/s]
      Serial.print("Velocidad: ");
      Serial.println(velocidad);
      return velocidad != 0.0;
 }
  
 void counter() {
  if (digitalRead (encoderPin) && (micros()-debounce > 500) && digitalRead (encoderPin)) {
    debounce = micros();
    pulses++;
  }
 }
