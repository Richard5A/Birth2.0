import java.awt.Component
import java.io.File
import java.io.IOException
import java.util.stream.Stream
import javax.swing.JOptionPane

fun main(args: Array<String>) {
    Gui.createDesign()
    val path = "geburtstage.json"
    val file = File(path)
    if (!file.exists()) {
        try {
            file.createNewFile()
        } catch (ioEx: IOException) {
            JOptionPane.showMessageDialog(
                null as Component?,
                "Error: " + ioEx.message,
                "Fehler beim Erstellen der Datei",
                0
            )
            ioEx.printStackTrace()
        }
    }

    val inst = MainClass(file)
    inst.readFromFile()
    inst.dialogLatestBirthAndMarriageDays()
    println(args.contentToString())
    if (Stream.of(*args).noneMatch { x: String -> x == "nogui" }) {
        println("GUI")
        val gui = Gui(inst)
        inst.gui = gui
        gui.isVisible = true
    }
}