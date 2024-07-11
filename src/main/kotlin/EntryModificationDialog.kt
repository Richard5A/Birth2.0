import java.awt.Font
import java.util.*
import javax.swing.*

sealed class BirthdayDialogResult {
    data object Cancelled : BirthdayDialogResult()
    data class Ok(
        val name: String,
        val day: Int,
        val month: Int,
        val year: Int,
    ) : BirthdayDialogResult()
}

fun showBirthdayDialog(
    initialData: BirthdayDialogResult.Ok = BirthdayDialogResult.Ok(
        "",
        1,
        1,
        2000
    )
): BirthdayDialogResult {
    val nameTextField = JTextField()
    nameTextField.text = initialData.name
    val daySpinner = JSpinner()
    daySpinner.font = Font("Tahoma", Font.PLAIN, 20)
    daySpinner.model = SpinnerNumberModel(initialData.day, 1, 31, 1)
    val monthCombobox: JComboBox<String?> = JComboBox(MONTHS_GERMAN)
    monthCombobox.font = Font("Tahoma", Font.PLAIN, 20)
    monthCombobox.selectedIndex = initialData.month - 1
    val yearSpinner = JSpinner()
    yearSpinner.font = Font("Tahoma", Font.PLAIN, 20)
    yearSpinner.model = SpinnerNumberModel(initialData.year, 1900, Calendar.getInstance()[1], 1)
    val message =
        arrayOf(
            "Name:",
            nameTextField,
            "Tag:",
            daySpinner,
            "Monat",
            monthCombobox,
            "Jahr (0 bei ?)",
            yearSpinner
        )
    val selectedOption = JOptionPane.showConfirmDialog(null, message, "Eintrag hinzufügen", 2)
    return if (selectedOption == 0 && !nameTextField.text.isNullOrEmpty())
        BirthdayDialogResult.Ok(
            name = nameTextField.text,
            day = daySpinner.value as Int,
            month = monthCombobox.selectedIndex + 1,
            year = yearSpinner.value as Int
        )
    else
        BirthdayDialogResult.Cancelled
}

sealed class MarryDialogResult {
    data object Cancelled : MarryDialogResult()
    data class Ok(
        val name1: String,
        val name2: String,
        val day: Int,
        val month: Int,
        val year: Int,
    ) : MarryDialogResult()
}

fun showMarryDialog(initialData: MarryDialogResult.Ok = MarryDialogResult.Ok("", "", 1, 1, 2000)): MarryDialogResult {
    val name1TextField = JTextField()
    name1TextField.text = initialData.name1
    val name2TextField = JTextField()
    name2TextField.text = initialData.name2
    val daySpinner = JSpinner()
    daySpinner.font = Font("Tahoma", Font.PLAIN, 20)
    daySpinner.model = SpinnerNumberModel(initialData.day, 1, 31, 1)
    val monthCombobox: JComboBox<String?> = JComboBox(MONTHS_GERMAN)
    monthCombobox.font = Font("Tahoma", Font.PLAIN, 20)
    monthCombobox.selectedIndex = initialData.month - 1
    val yearSpinner = JSpinner()
    yearSpinner.font = Font("Tahoma", Font.PLAIN, 20)
    yearSpinner.model = SpinnerNumberModel(initialData.year, 1900, Calendar.getInstance()[1], 1)
    val message =
        arrayOf(
            "Name von Person 1:",
            name1TextField,
            "Name von Person 2:",
            name2TextField,
            "Tag:",
            daySpinner,
            "Monat",
            monthCombobox,
            "Jahr (0 bei ?)",
            yearSpinner
        )
    val selectedOption = JOptionPane.showConfirmDialog(null, message, "Eintrag hinzufügen", 2)
    return if (selectedOption == 0 && !name1TextField.text.isNullOrEmpty())
        MarryDialogResult.Ok(
            name1 = name1TextField.text,
            name2 = name2TextField.text,
            day = daySpinner.value as Int,
            month = monthCombobox.selectedIndex + 1,
            year = yearSpinner.value as Int
        )
    else
        MarryDialogResult.Cancelled
}
