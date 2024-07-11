import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileReader
import java.io.FileWriter

@Serializable
data class BirthdayEntry(
    val born: List<Int>,
    val name: String,
)

@Serializable
data class MarryEntry(
    val date: List<Int>,
    val persons: List<String>
)

@Serializable
data class BirthdaysAndMarriesDAO(
    val marry_days: List<MarryEntry>,
    val birthdays: List<BirthdayEntry>,
    val version: Int
)

fun parseFromFile(file: File): BirthdaysAndMarriesDAO {
    val returnValue: BirthdaysAndMarriesDAO
    try {
        val fileReader = FileReader(file)
        val content = fileReader.readText()
        fileReader.close()
        return Json.decodeFromString(content)
    } catch (exeption: Exception) {
        System.err.println("Problem parsing JSON file: $file. Creating empty DAO")
        exeption.printStackTrace()
        return BirthdaysAndMarriesDAO(
            marry_days = emptyList(),
            birthdays = emptyList(),
            version = 1
        )
    }
}

fun saveToFile(file: File, dao: BirthdaysAndMarriesDAO) {
    val fileWriter = FileWriter(file, false)
    val content = Json.encodeToString(dao)
    fileWriter.write(content)
    fileWriter.flush()
    fileWriter.close()
}