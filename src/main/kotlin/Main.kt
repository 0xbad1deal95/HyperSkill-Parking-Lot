import java.util.*

private fun processCommands(scanner: Scanner, parkingLot: MutableList<Slot>) {
    tailrec fun implementation(command: Command, _parkingLot: MutableList<Slot>) {
        when (command) {
            is Create -> println("Created a parking lot with ${command.capacity} spots.")

            is Park -> {
                val emptySlot = _parkingLot.indexOfFirst { it == Free }
                if (emptySlot > -1) {
                    _parkingLot[emptySlot] = Occupied(command.car)
                    println("${command.car.color} car parked in spot ${emptySlot + 1}.")
                } else println("Sorry, the parking lot is full.")
            }

            is Leave -> {
                when {
                    _parkingLot[command.slot - 1] != Free -> {
                        _parkingLot[command.slot - 1] = Free
                        println("Spot ${command.slot} is free.")
                    }

                    else -> println("There is no car in spot ${command.slot}.")
                }
            }

            is Status -> _parkingLot
                .asSequence()
                .mapIndexed { index, slot -> if (slot != Free) index to slot else -1 to Free }
                .filter { (slot, _) -> slot != -1 }
                .map { (index, slot) -> "${index + 1} ${(slot as Occupied).car.regId} ${slot.car.color}" }
                .joinToString(separator = "\n")
                .ifEmpty { "Parking lot is empty." }
                .also(::println)

            is RegByColor -> _parkingLot
                .asSequence()
                .filterIsInstance<Occupied>()
                .filter { it.car.color.lowercase() == command.color.lowercase() }
                .joinToString { it.car.regId }
                .ifEmpty { "No cars with color ${command.color} were found." }
                .also(::println)

            is SpotByColor -> _parkingLot
                .asSequence()
                .mapIndexed { index, slot ->
                    if (slot is Occupied && slot.car.color.lowercase() == command.color.lowercase()) index + 1 else 0
                }.filter { it != 0 }
                .joinToString()
                .ifEmpty { "No cars with color ${command.color} were found." }
                .also(::println)

            is SpotByReg -> _parkingLot
                .asSequence()
                .mapIndexed { indx, slot -> if (slot is Occupied && slot.car.regId == command.regId) indx + 1 else 0 }
                .filter { it != 0 }
                .joinToString()
                .ifEmpty { "No cars with registration number ${command.regId} were found." }
                .also(::println)

            is Exit -> return
        }

        if (command !is Create && _parkingLot.size == 0) println("Sorry, a parking lot has not been created.")
        val lot = if (command is Create) MutableList<Slot>(command.capacity) { Free } else _parkingLot
        if (scanner.hasNext()) implementation(Command.parse(scanner.nextLine()), lot)
    }

    if (scanner.hasNext()) implementation(Command.parse(scanner.nextLine()), parkingLot)
}

fun main() {
    val parkingLot = MutableList<Slot>(0) { Free }
    Scanner(System.`in`).use { scanner -> processCommands(scanner = scanner, parkingLot = parkingLot) }
}