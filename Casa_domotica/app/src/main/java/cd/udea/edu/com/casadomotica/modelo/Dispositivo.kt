package cd.udea.edu.com.casadomotica.modelo

open class Dispositivo(key: String, toString: String) {
    var id: String = key
    var nombre: String = toString
}

class Bebedero(key: String, toString: String): Dispositivo(key, toString) {
    var nivelTanque: Int = 0

    override fun toString(): String {
        return "$nombre,$nivelTanque"
    }
}

class Comedero(key: String, toString: String): Dispositivo(key, toString) {
    var nivelTanque: Int = 0
    var nivelCoca: Int = 0
    var Programacion: MutableList<Long> = mutableListOf()

    override fun toString(): String {
        return "$nombre,$nivelTanque,$nivelCoca,$Programacion"
    }
}