import ar.edu.seleccionPersonal.BusquedaInterna
import ar.edu.seleccionPersonal.Cargo
import ar.edu.seleccionPersonal.PersonalPlanta
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class TestEmpleadoDePlanta  : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    val personalDePlanta = PersonalPlanta().apply {
        fechaIngreso = LocalDate.of(2008, 2, 14)
    }
    describe("dado un empleado de planta") {
        it("se puede calcular su antigüedad tomando una fecha fija como base") {
            personalDePlanta.getFechaAntiguedad(LocalDate.of(2013, 8, 15)) shouldBe 5
        }
    }
})