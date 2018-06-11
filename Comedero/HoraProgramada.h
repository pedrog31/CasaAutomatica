class HoraProgramada {
  private:
      long hora;
      int food;
      bool isCompleted;

  public:

  HoraProgramada() {
    isCompleted = false;
  }
  
  long getHora () {
    return hora;
  }

  void setHora (String mhora) {
    hora = mhora.toInt();
  }

  int getFood () {
    return food;
  }

  void setFood (String mfood) {
    food = mfood.toInt();
  }

  bool getIsCompleted () {
    return isCompleted;
  }

  void setIsCompleted (bool misCompleted) {
    isCompleted = misCompleted;
  }
};


