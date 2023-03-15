import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ComboBox, Label}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.event.ActionEvent
import scalafx.scene.layout.GridPane

object SpellPointCalculator extends JFXApp {
  val spellPointsMap = Map(
    1 -> 4, 2 -> 6, 3 -> 14, 4 -> 17, 5 -> 27,
    6 -> 32, 7 -> 38, 8 -> 44, 9 -> 57, 10 -> 64,
    11 -> 73, 12 -> 73, 13 -> 83, 14 -> 83, 15 -> 94,
    16 -> 94, 17 -> 107, 18 -> 114, 19 -> 123, 20 -> 133
  )

  val spellCostMap = Map(
    "0" -> 0, "1" -> 2, "2" -> 3, "3" -> 5, "4" -> 6, "5" -> 7,
    "6" -> 9, "7" -> 10, "8" -> 11, "9" -> 13
  )

  // Set up caster level combo box with appropriate values and labels
  val casterLevelComboBox = new ComboBox(1 to 20 map { level =>
    s"Level $level (Spell Points ${spellPointsMap(level)})"
  })
  val initialLevel = 1

  casterLevelComboBox.value = casterLevelComboBox.items().find(item => item.startsWith(s"Level $initialLevel")).get

  // Set up total points label
  var totalPoints = spellPointsMap(initialLevel)
  val totalPointsLabel = new Label(s"Total Spell Points: $totalPoints")

  // Add listener to caster level combo box to update total points
  casterLevelComboBox.onAction = () => {
    val newLevel = casterLevelComboBox.value().split(" ")(1).toInt
    val newPoints = spellPointsMap(newLevel)
    totalPoints = newPoints
    totalPointsLabel.text = s"Total Spell Points: $totalPoints"
  }
  
  val spellLevelComboBox = new ComboBox(List("1", "2", "3", "4", "5", "6", "7", "8", "9") map { level =>
    s"Spell Level $level (Cost ${spellCostMap(level)})"
  })

  // Set up add and subtract buttons with appropriate listeners
  val addButton = new Button("Add")
  val subtractButton = new Button("Subtract")

  /*spellLevelComboBox.onAction = () => {
    val cost = spellCostMap(spellLevelComboBox.value())
    totalPointsLabel.text = s"Total Spell Points: ${totalPoints - cost}"
  }*/
  
  addButton.onAction = () => {
    val costString = "Cost Y".r
    val cost = (costString findFirstIn spellLevelComboBox.value()).get.replaceAll("[^0-9]", "").toInt //try this again
    //val cost = spellCostMap(spellLevelComboBox.value().split(" ")(2).dropRight(1))
    //val cost = spellCostMap(spellLevelComboBox.value().drop("Spell Level ".length))
    totalPoints += cost
    totalPointsLabel.text = s"Total Spell Points: $totalPoints"
  }

  subtractButton.onAction = () => {
    val costString = "Cost Y".r
    val cost = (costString findFirstIn spellLevelComboBox.value()).get.replaceAll("[^0-9]", "").toInt //try this again
    //val cost = spellCostMap(spellLevelComboBox.value().split(" ")(2).dropRight(1))
    //val cost = spellCostMap(spellLevelComboBox.value().drop("Spell Level ".length))
    totalPoints -= cost
    totalPointsLabel.text = s"Total Spell Points: $totalPoints"
  }
  
  // Set up spell points grid pane with appropriate components
  val spellPointsGridPane = new GridPane {
    hgap = 10
    vgap = 10
    padding = Insets(10)
    add(new Label("Caster Level:"), 0, 0)
    add(casterLevelComboBox, 1, 0)
    add(totalPointsLabel, 2, 0)
    add(new Label("Spell Level:"), 0, 1)
    add(spellLevelComboBox, 1, 1)
    add(addButton, 2, 1)
    add(subtractButton, 3, 1)
  }

  val vbox = new VBox {
    children = spellPointsGridPane
    padding = Insets(10)
  }
  stage = new PrimaryStage {
    title = "Spell Point Calculator"
    scene = new Scene {
      content = vbox
    }
  }
}