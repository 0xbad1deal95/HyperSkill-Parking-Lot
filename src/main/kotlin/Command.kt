sealed class Command {
    companion object {

        private const val CREATE = "create"
        private const val PARK = "park"
        private const val LEAVE = "leave"
        private const val STATUS = "status"
        private const val REG_BY_COLOR = "reg_by_color"
        private const val SPOT_BY_COLOR = "spot_by_color"
        private const val SPOT_BY_REG = "spot_by_reg"
        private const val EXIT = "exit"

        fun parse(input: String, delimiter: String = " "): Command {
            val tokens = input.split(delimiter)
            return when (tokens[0].lowercase()) {
                CREATE -> Create(capacity = tokens[1].toInt())
                PARK -> Park(car = Car(regId = tokens[1], color = tokens[2]))
                LEAVE -> Leave(slot = tokens[1].toInt())
                STATUS -> Status
                REG_BY_COLOR -> RegByColor(color = tokens[1])
                SPOT_BY_COLOR -> SpotByColor(color = tokens[1])
                SPOT_BY_REG -> SpotByReg(regId = tokens[1])
                EXIT -> Exit
                else -> throw IllegalStateException()
            }
        }
    }
}

class Create(val capacity: Int) : Command()
class Park(val car: Car) : Command()
class Leave(val slot: Int) : Command()
object Status : Command()
class RegByColor(val color: String) : Command()
class SpotByColor(val color: String) : Command()
class SpotByReg(val regId: String) : Command()
object Exit : Command()
