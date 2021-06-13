import ar.edu.seleccionPersonal.*
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

class TestBusquedasExternas : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    val busquedaExterna = BusquedaExterna().apply {
        sector = "Sistemas"
        puesto = "Programador"
        remuneracion = 10000
    }

    describe("dada una búsqueda interna") {
        it("un empleado de planta no puede postularse") {
            val cargoProgramador = Cargo(sueldo = 6000, descripcion = "Programador")
            val empleado = PersonalPlanta().apply { cargo = cargoProgramador }
            assertThrows<BusinessException> { busquedaExterna.postular(empleado) }
        }
        it ("un contratado con mucha antigüedad no puede postularse") {
            val contratadoAntiguo = PersonalContratado().apply {
                sector = "Contabilidad"
                fechaIngreso = LocalDate.of(2014, 5, 9)
            }
            assertThrows<BusinessException> { busquedaExterna.postular(contratadoAntiguo) }
        }
        it ("un contratado reciente puede postularse") {
            val contratadoReciente = PersonalContratado().apply {
                sector = "Sistemas"
            }
            busquedaExterna.postular(contratadoReciente)
            busquedaExterna.estaPostulado(contratadoReciente) shouldBe true
        }
        it ("un externo puede postularse") {
            val externo = Externo()
            busquedaExterna.postular(externo)
            busquedaExterna.estaPostulado(externo) shouldBe true
        }
    }
})
