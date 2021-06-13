import ar.edu.seleccionPersonal.*
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

class TestBusquedasEspeciales : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    val busquedaEspecial = BusquedaEspecial().apply {
        sector = "Sistemas"
        puesto = "Programador"
        remuneracion = 7000
    }

    fun cargoProgramador() = Cargo(descripcion = "Programador", sueldo = 6000)
    fun cargoTester() = Cargo(descripcion = "Tester", sueldo = 126000)
    fun cargoAnalista() = Cargo(descripcion = "Analista", sueldo = 10000)

    describe("dada una búsqueda especial") {
        it("un empleado de planta con menos de 10 personas a cargo no puede postularse") {
            val empleadoSinPersonalACargo = PersonalPlanta().apply { cargo = cargoProgramador() }
            assertThrows<BusinessException> { busquedaEspecial.postular(empleadoSinPersonalACargo) }
        }
        it ("un empleado de planta que tiene mejor sueldo actual no puede postularse") {
            val empleadoConMuyBuenSueldo = PersonalPlanta().apply {cargo = cargoTester() }
            assertThrows<BusinessException> { busquedaEspecial.postular(empleadoConMuyBuenSueldo) }
        }
        it ("un empleado de planta con un sueldo menor y más de 10 personas a cargo puede postularse") {
            val empleadoOk = PersonalPlanta().apply {cargo = cargoProgramador() }
            (1..25).forEach { empleadoOk.agregarPersonaACargo(PersonalPlanta()) }
            busquedaEspecial.postular(empleadoOk)
            busquedaEspecial.estaPostulado(empleadoOk) shouldBe true
        }
        it ("un contratado con pocas personas a cargo no puede postularse") {
            val contratadoConPocasPersonasACargo = PersonalContratado().apply {
                sector = "Contabilidad"
                fechaIngreso = LocalDate.of(2014, 5, 9)
            }
            assertThrows<BusinessException> { busquedaEspecial.postular(contratadoConPocasPersonasACargo) }
        }
        it ("un contratado con muchas personas a cargo puede postularse") {
            val contratadoConMuchasPersonasACargo = PersonalContratado().apply {
                sector = "Contabilidad"
                fechaIngreso = LocalDate.now().minusYears(1)
            }
            (1..25).forEach { contratadoConMuchasPersonasACargo.agregarPersonaACargo(PersonalPlanta()) }
            busquedaEspecial.postular(contratadoConMuchasPersonasACargo)
            busquedaEspecial.estaPostulado(contratadoConMuchasPersonasACargo) shouldBe true
        }
        it ("un externo sin experiencia en el puesto no puede postularse") {
            val externo = Externo()
            assertThrows<BusinessException> { busquedaEspecial.postular(externo) }
        }
        it ("un externo con experiencia en el puesto puede postularse") {
            val externoConExperienciaEnElPuesto = Externo().apply {
                trabajarDe(cargoProgramador().descripcion)
                trabajarDe(cargoAnalista().descripcion)
            }
            busquedaEspecial.postular(externoConExperienciaEnElPuesto)
            busquedaEspecial.estaPostulado(externoConExperienciaEnElPuesto) shouldBe true
        }
    }
})
