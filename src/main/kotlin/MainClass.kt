import java.awt.Component
import java.io.File
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.swing.ImageIcon
import javax.swing.JOptionPane
import kotlin.system.exitProcess

class MainClass(private var file: File) {
    var entriesBirthdays: MutableList<Entry> = ArrayList()
    var entriesMarriage: MutableList<Entry> = ArrayList()
    var gui: Gui? = null

    fun readFromFile() {
        val fileContent = parseFromFile(file)
        entriesBirthdays = fileContent.birthdays.map {
            Entry(
                listOf(it.name),
                date = listOfIntToLocalDate(it.born)
            )
        }.toMutableList()
        entriesMarriage = fileContent.marry_days.map {
            Entry(
                it.persons,
                listOfIntToLocalDate(it.date)
            )
        }.toMutableList()
    }

    fun close() {
        this.save()
        exitProcess(0)
    }

    fun save() {
        saveToFile(
            file, BirthdaysAndMarriesDAO(
                birthdays = entriesBirthdays.map {
                    BirthdayEntry(
                        born = localDateToIntList(it.date),
                        name = it.names.first()
                    )
                },
                marry_days = entriesMarriage.map { MarryEntry(date = localDateToIntList(it.date), persons = it.names) },
                version = 1
            )
        )
    }

    fun dialogLatestBirthAndMarriageDays() {
        val entriesB = entriesBirthdays.filter { daysRemain(it.date) <= 10 }

        val entriesM = entriesMarriage.filter { daysRemain(it.date) <= 10 }

        val out: StringBuilder = StringBuilder()
        if (entriesB.isNotEmpty()) {

            for (entry in entriesB) {
                when (daysRemain(entry.date)) {
                    0 -> {
                        out.append("<b><h2>").append(entry.formattedName)
                            .append(" hat heute Geburtstag!</h2></b><br>")
                    }

                    1 -> {
                        out.append("<b><h2>").append(entry.formattedName)
                            .append(" hat morgen Geburtstag!</h2>")
                    }

                    else -> {
                        out.append("<h3>").append(entry.formattedName).append(" hat ").append(
                            if (daysRemain(entry.date) > 0) "in " + daysRemain(entry.date) + " Tag" + (if (daysRemain(
                                    entry.date
                                ) > 1
                            ) "en" else "") else "heute"
                        ).append(" Geburtstag!</h3><br>")
                    }
                }
            }

            JOptionPane.showConfirmDialog(
                null,
                "<html>$out</html>",
                "Geburtstage",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE,
                ImageIcon("icon.png")
            )
        }

        if (entriesM.isNotEmpty()) {
            for (entry in entriesM) {
                out.append(entry.formattedName).append(" hat ").append(
                    if (daysRemain(entry.date) > 0) "in " + daysRemain(entry.date) + " Tag" + (if (daysRemain(entry.date) > 1) "en" else "") else "heute "
                ).append(" Heiratstag! \n")
            }

            JOptionPane.showConfirmDialog(null, out.toString(), "Heiratstage", -1, 0, ImageIcon())
        }
    }
}

fun ageOf(dob: LocalDate?): Int {
    val today = LocalDate.now()
    return if (dob!!.year != 0) ChronoUnit.YEARS.between(dob, today).toInt() else -1
}

fun daysRemain(dob: LocalDate?): Int {
    val today = LocalDate.now()
    val age: Long = ChronoUnit.YEARS.between(dob, today)
    var nextBirthday = dob!!.plusYears(age)
    if (nextBirthday.isBefore(today)) {
        nextBirthday = dob.plusYears(age + 1L)
    }

    val daysUntilNextBirthday: Long = ChronoUnit.DAYS.between(today, nextBirthday)
    return Math.toIntExact(daysUntilNextBirthday)
}