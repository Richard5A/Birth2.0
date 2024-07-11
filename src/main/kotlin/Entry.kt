import java.time.LocalDate

data class Entry(
    var names: List<String>,
    var date: LocalDate
) {
    val formattedName: String
    get() = if (names.isEmpty()) {
        ""
    } else {
        names.joinToString(separator = " & ")
    }
}

fun listOfIntToLocalDate(ints: List<Int>): LocalDate {
    return when (ints.size) {
        3 -> { LocalDate.of(ints[2], ints[1], ints[0]) }
        2 -> { LocalDate.of(0, ints[1], ints[0]) }
        else -> throw IllegalArgumentException("List of ints must be of size 3")
    }
}

fun localDateToIntList(localDate: LocalDate): List<Int> {
    return listOfNotNull(
        localDate.dayOfMonth,
        localDate.monthValue,
        if (localDate.year == 0) null else localDate.year,
    )
}