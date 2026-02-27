
## Selección de personal

[![build](https://github.com/uqbar-project/eg-seleccion-personal-kotlin/actions/workflows/build.yml/badge.svg)](https://github.com/uqbar-project/eg-seleccion-personal-kotlin/actions/workflows/build.yml) [![coverage](https://codecov.io/gh/uqbar-project/eg-seleccion-personal-kotlin/branch/master/graph/badge.svg)](https://codecov.io/gh/uqbar-project/eg-seleccion-personal-kotlin/branch/master/graph/badge.svg)

<img src="images/personal.png" alt="Selección de Personal" height="130px" width="130px"/>

### Branch master

- [Apunte](https://docs.google.com/document/d/1XWq9azqchoJZ7h8-hLcpA1Zj5T1UtvFtDKbpzxoQ-dw/edit?usp=sharing)
- [Presentación](https://docs.google.com/presentation/d/11KJwHr3mLe1NtKFh7i5P5oHm-kg2lw4aPcgBC_MGVC4/edit?usp=sharing)
- Para ver una alternativa con multiple dispatch en lenguaje Xtend, pueden ver [este repositorio](https://github.com/uqbar-project/eg-seleccion-personal-xtend/tree/dispatch)

### Alternativa, usando sealed class + pattern matching

Veamos esta otra variante. En la jerarquía de Busqueda las subclases ya no necesitan encargarse de hacer el dispatch, lo delegamos al Postulante:

```kt
sealed class Busqueda {
    val postulantes = mutableListOf<Postulante>()
    var remuneracion: Int = 0
    var puesto: String = ""
    var sector: String = ""

    fun agregarPostulante(postulante: Postulante) {
        postulantes.add(postulante)
    }

    fun estaPostulado(postulante: Postulante) = postulantes.contains(postulante)

    fun postular(postulante: Postulante) {
        postulante.validar(this)
        agregarPostulante(postulante)
    }

}

class BusquedaEspecial : Busqueda()

class BusquedaInterna : Busqueda()

class BusquedaExterna : Busqueda()
```

Como agregado, hacemos que Busqueda no pueda ser subclasificada, mediante el modificador _sealed_ que es equivalente al _final_ de Java.

La clase padre Postulante será ahora la encargada de hacer un dispatch, mediante el mecanismo de pattern matching, aunque sí: hay que preguntar por la clase en Runtime:

```kt
abstract class Postulante {
  // El método de despacho único
  fun validar(busqueda: Busqueda) {
    when (busqueda) {
      is BusquedaInterna -> validarBusquedaInterna(busqueda)
      is BusquedaExterna -> validarBusquedaExterna(busqueda)
      is BusquedaEspecial -> validarBusquedaEspecial(busqueda)
    }
  }

  ...
```

Cuando necesitemos agregar un nuevo tipo de búsqueda, tenemos que

- generar la nueva subclase
- e incorporar el nuevo caso en el when de Postulante

Quienes vienen y compran el paradigma de objetos, esta solución posiblemente les parezca poco elegante. Incluso objetivamente va en contra de la extensibilidad (las sealed class tienen esa desventaja). Los fanáticos del paradigma funcional, acostumbrados a este tipo de herramientas, consideran que es más fácil entender la lógica de validación, porque está **en un solo lugar**.

No hay una única respuesta, por eso lo importante es conocer las opciones y llegar a nuestra propia conclusión.
