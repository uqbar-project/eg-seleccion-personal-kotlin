import ar.edu.seleccionPersonal.*
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows

class TestBusquedasInternas : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    val busquedaInterna = BusquedaInterna().apply {
        sector = "Sistemas"
        puesto = "Programador"
    }

    describe("dada una búsqueda interna") {
        it("un empleado puede postularse") {
            val cargoProgramador = Cargo(sueldo = 6000, descripcion = "Programador")
            val empleado = PersonalPlanta().apply { cargo = cargoProgramador }
            busquedaInterna.postular(empleado)
            busquedaInterna.estaPostulado(empleado) shouldBe true
        }
        it ("un contratado de otro sector no puede postularse") {
            val contratadoDistintoSector = PersonalContratado().apply { sector = "Contabilidad" }
            assertThrows<BusinessException> { busquedaInterna.postular(contratadoDistintoSector) }
        }
        it ("un contratado del mismo sector puede postularse") {
            val contratadoMismoSector = PersonalContratado().apply { sector = "Sistemas" }
            busquedaInterna.postular(contratadoMismoSector)
            busquedaInterna.estaPostulado(contratadoMismoSector) shouldBe true
        }
        it ("un externo no puede postularse") {
            val externo = Externo()
            assertThrows<BusinessException> { busquedaInterna.postular(externo) }
        }
    }
})
