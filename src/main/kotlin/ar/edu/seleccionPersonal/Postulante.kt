package ar.edu.seleccionPersonal

import java.time.LocalDate
import java.time.temporal.ChronoUnit

abstract class Postulante {

    // Método default para poder compilar
    fun validarPostulacion(busqueda: Busqueda) {
        throw BusinessException("Debe utilizar un tipo de búsqueda específico")
    }

    abstract fun validarPostulacionBusquedaInterna(interna: BusquedaInterna)

    abstract fun validarPostulacionBusquedaExterna(externa: BusquedaExterna)

    abstract fun validarPostulacionBusquedaEspecial(especial: BusquedaEspecial)

}

abstract class Empleado : Postulante() {
    lateinit var cargo: Cargo
    var sector: String = ""
    var fechaIngreso = LocalDate.now()
    val personasACargo = mutableListOf<Empleado>()

    fun agregarPersonaACargo(empleado: Empleado) {
        personasACargo.add(empleado)
    }

    fun getFechaAntiguedad(dia: LocalDate) = ChronoUnit.YEARS.between(fechaIngreso, dia)

    fun getFechaAntiguedad() = getFechaAntiguedad(LocalDate.now())

    fun cantidadPersonasACargo() = personasACargo.size
}

class PersonalContratado : Empleado() {

    override fun validarPostulacionBusquedaInterna(interna: BusquedaInterna) {
        if (!sector.equals(interna.sector)) {
            throw BusinessException(
                    "El postulante pertenece al sector $sector y la búsqueda es para $interna.sector")
        }
    }

    override fun validarPostulacionBusquedaExterna(externa: BusquedaExterna) {
        if (this.getFechaAntiguedad() > 1) {
            throw BusinessException("El postulante no tiene menos de un año de antigüedad")
        }
    }

    override fun validarPostulacionBusquedaEspecial(especial: BusquedaEspecial) {
        if (this.cantidadPersonasACargo() < 20) {
            throw BusinessException("El postulante no tiene más de 20 personas a cargo")
        }
    }

}

class PersonalPlanta : Empleado() {
    fun sueldo() = cargo.sueldo

    override fun validarPostulacionBusquedaInterna(interna: BusquedaInterna) {
        // OK
    }

    override fun validarPostulacionBusquedaExterna(externa: BusquedaExterna) {
        throw BusinessException("Un empleado de planta no puede postularse a una búsqueda externa")
    }

    override fun validarPostulacionBusquedaEspecial(especial: BusquedaEspecial) {
        if (especial.remuneracion < this.sueldo()) {
            throw BusinessException("La remuneración de la búsqueda debe superar el sueldo actual para postularse a una búsqueda especial")
        }
        if (this.cantidadPersonasACargo() < 10) {
            throw BusinessException("Debe tener al menos 10 personas a cargo para postularse a una búsqueda especial")
        }
    }
}

class Externo : Postulante() {
    val puestosAnteriores = mutableListOf<String>()

    fun trabajarDe(puesto: String) {
        puestosAnteriores.add(puesto)
    }

    fun trabajoEn(puesto: String) = puestosAnteriores.contains(puesto)

    override fun validarPostulacionBusquedaInterna(interna: BusquedaInterna) {
        throw BusinessException("No puede postularse a búsquedas internas")
    }

    override fun validarPostulacionBusquedaExterna(externa: BusquedaExterna) {
        // OK
    }

    override fun validarPostulacionBusquedaEspecial(especial: BusquedaEspecial) {
        if (!puestosAnteriores.contains(especial.puesto)) {
            throw BusinessException("Para poder postularse debe haber trabajado anteriormente en el mismo puesto")
        }
    }

}

data class Cargo(val sueldo: Int, val descripcion: String) {}