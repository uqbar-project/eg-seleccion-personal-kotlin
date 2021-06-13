package ar.edu.seleccionPersonal

abstract class Busqueda {
    val postulantes = mutableListOf<Postulante>()
    var remuneracion: Int = 0
    var puesto: String = ""
    var sector: String = ""

    fun agregarPostulante(postulante: Postulante) {
        postulantes.add(postulante)
    }

    fun estaPostulado(postulante: Postulante) = postulantes.contains(postulante)

    fun postular(postulante: Postulante) {
        validarPostulacion(postulante)
        agregarPostulante(postulante)
    }

    abstract fun validarPostulacion(postulante: Postulante)
}

class BusquedaEspecial : Busqueda() {
    override fun validarPostulacion(postulante: Postulante) {
        postulante.validarPostulacionBusquedaEspecial(this)
    }
}

class BusquedaInterna : Busqueda() {
    override fun validarPostulacion(postulante: Postulante) {
        postulante.validarPostulacionBusquedaInterna(this)
    }
}

class BusquedaExterna : Busqueda() {
    override fun validarPostulacion(postulante: Postulante) {
        postulante.validarPostulacionBusquedaExterna(this)
    }
}
