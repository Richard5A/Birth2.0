import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Font
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.swing.*
import javax.swing.table.AbstractTableModel
import javax.swing.table.TableModel

class Gui(var main: MainClass) : JFrame() {
    private var table: JTable
    private var addButton: JButton
    private var removeButton: JButton
    private var editButton: JButton
    private var scrollPane: JScrollPane
    private var toolbar: JToolBar
    private var buttonsPanel: JPanel
    private var openedList: ListTypes = ListTypes.BIRTHDAYS

    private fun configureTable() {
        table.apply {
            font = Font("", 0, 20)
            rowHeight = 25
            tableHeader.font = Font("", 0, 20)
            foreground = Color.BLACK
            setSelectionMode(0)
            autoResizeMode = 4
            columnModel.getColumn(0).preferredWidth = 8
            columnModel.getColumn(1).preferredWidth = 3
            columnModel.getColumn(2).preferredWidth = 2
            columnModel.getColumn(3).preferredWidth = 2
        }
    }

    private fun configureAddButton() {
        addButton.addActionListener {
            println("Eintrag hinzufügen")
            val list: MutableList<Entry>
            if (openedList == ListTypes.BIRTHDAYS) {
                val data = showBirthdayDialog()
                if (data is BirthdayDialogResult.Ok) {
                    list = main.entriesBirthdays
                    list.add(Entry(listOf(data.name), LocalDate.of(data.year, data.month, data.day)))
                }
            } else {
                val data = showMarryDialog()
                if (data is MarryDialogResult.Ok) {
                    list = main.entriesMarriage
                    list.add(Entry(listOf(data.name1, data.name2), LocalDate.of(data.year, data.month, data.day)))
                }
            }
            table.updateUI()
        }

    }

    private fun configureEditButton() {
        editButton.addActionListener {
            val i = table.selectedRow
            if (i >= 0) {
                val list: MutableList<Entry>
                val entry: Entry
                if (openedList == ListTypes.BIRTHDAYS) {
                    list = main.entriesBirthdays
                    entry = list[i]
                    val data = showBirthdayDialog(
                        BirthdayDialogResult.Ok(
                            name = entry.names.single(),
                            day = entry.date.dayOfMonth,
                            month = entry.date.month.value,
                            year = entry.date.year
                        )
                    )
                    if (data is BirthdayDialogResult.Ok) {
                        list[i] = Entry(
                            names = listOf(data.name),
                            LocalDate.of(data.year, data.month, data.day),
                        )
                    }
                } else {
                    list = main.entriesMarriage
                    entry = list[i]
                    val data = showMarryDialog(
                        MarryDialogResult.Ok(
                            name1 = entry.names.component1(),
                            name2 = entry.names.component2(),
                            day = entry.date.dayOfMonth,
                            month = entry.date.month.value,
                            year = entry.date.year
                        )
                    )
                    if (data is MarryDialogResult.Ok) {
                        list[i] = Entry(
                            names = listOf(data.name1, data.name2),
                            LocalDate.of(data.year, data.month, data.day),
                        )
                    }
                }

                table.updateUI()
            }
        }
    }

    private fun configureRemoveButton() {
        removeButton.addActionListener {
            if (table.selectedRow >= 0 && JOptionPane.showConfirmDialog(
                    null, "Willst du wirklich löschen?"
                ) == 0
            ) {
                if (openedList == ListTypes.BIRTHDAYS) {
                    main.entriesBirthdays.removeAt(table.selectedRow)
                } else {
                    main.entriesMarriage.removeAt(table.selectedRow)
                }

                table.updateUI()
            }
        }
    }

    init {
        title = "Geburtstagskalender"
        defaultCloseOperation = DISPOSE_ON_CLOSE
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                main.close()
            }
        })
        table = JTable(createTableModel(main.entriesBirthdays))
        configureTable()
        layout = BorderLayout()
        iconImage = ImageIcon(".\\icon.png", "Hallo").image
        setSize(700, 800)
        scrollPane = JScrollPane(this.table)
        addButton = JButton("Eintrag hinzufügen")
        configureAddButton()
        this.editButton = JButton("Eintrag ändern")
        configureEditButton()
        this.removeButton = JButton("Eintrag löschen")
        configureRemoveButton()
        this.buttonsPanel = JPanel()
        buttonsPanel.add(this.addButton)
        buttonsPanel.add(this.editButton)
        buttonsPanel.add(this.removeButton)
        this.toolbar = JToolBar()
        configureToolbar()
        add(this.toolbar, "North")
        add(this.buttonsPanel, "South")
        add(this.scrollPane, "Center")
    }

    private fun configureToolbar() {
        val saveButton = JButton("Speichern")
        val marryToolBarButton = JButton("zu Hochzeitstage")

        saveButton.addActionListener {
            main.save()
        }
        marryToolBarButton.addActionListener {
            if (openedList == ListTypes.BIRTHDAYS) {
                openedList = ListTypes.MARRY_DAYS
                table.model = createTableModel(main.entriesMarriage)
                marryToolBarButton.text = "zu Geburtstage"
            } else {
                openedList = ListTypes.BIRTHDAYS
                table.model = createTableModel(main.entriesBirthdays)
                marryToolBarButton.text = "zu Hochzeitstage"
            }
        }
        toolbar.add(marryToolBarButton)
        toolbar.add(saveButton)
    }

    companion object {
        fun createDesign() {
            try {
                val installedLookAndFeels = UIManager.getInstalledLookAndFeels()
                println(UIManager.getSystemLookAndFeelClassName())
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
                for (element in installedLookAndFeels) {
                    println("Look and Feel: ${element.name}")
                    if ("Nimbus" == element.name) {
                        UIManager.setLookAndFeel(element.className)
                        println("Setting Nimbus as default")
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun createTableModel(of: List<Entry>): TableModel {
        return object : AbstractTableModel() {
            override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
                val entry = of[rowIndex]
                val ret: String = when (columnIndex) {
                    0 -> entry.formattedName
                    1 -> entry.date.format(DateTimeFormatter.ofPattern("dd.MM")) + (if (entry.date.year != 0) "." + entry.date.year else "")

                    2 -> daysRemain(entry.date).toString()
                    3 -> if (ageOf(entry.date) == -1) "?" else ageOf(entry.date).toString()
                    else -> ""
                }
                return ret
            }

            override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
                return false
            }

            override fun getRowCount(): Int {
                return of.size
            }

            override fun getColumnCount(): Int {
                return 4
            }

            override fun getColumnName(column: Int): String {
                return when (column) {
                    0 -> "Name"
                    1 -> "Datum"
                    2 -> "in ... Tagen"
                    3 -> if (openedList == ListTypes.BIRTHDAYS) "Alter" else "seit Jahren"
                    else -> error("A wrong column index called: $column")

                }
            }
        }
    }

}
