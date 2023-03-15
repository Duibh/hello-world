import scalafx.scene.control.{ComboBox, SpinnerValueFactory}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.geometry.Insets

object SpellPointTracker extends JFXApp {

  // Maps the spell level to the total number of spell points available at that level
  val spellPointTotal: Map[Int, Int] = Map(
    1 -> 4,
    2 -> 6,
    3 -> 14,
    4 -> 17,
    5 -> 27,
    6 -> 32,
    7 -> 38,
    8 -> 44,
    9 -> 57,
    10 -> 64, 
    11 -> 73,
    12 -> 73,
    13 -> 83,
    14 -> 83,
    15 -> 94,
    16 -> 94,
    17 -> 107,
    18 -> 114,
    19 -> 123,
    20 -> 133
  )

   // Maps the spell level to the cost of casting a spell at that level in spell points
  val spellCostPerLevel: Map[Int, Int] = Map(
    1 -> 2,
    2 -> 3,
    3 -> 5,
    4 -> 6,
    5 -> 7,
    6 -> 9,
    7 -> 10,
    8 -> 11,
    9 -> 13
  )

  // Keeps track of the number of spell points spent at each spell level
  var spellPoints: Map[Int, Int] = Map.empty

   // Sets the caster level and initializes the spellPoints map with zeros for each spell level
  def setCasterLevel(level: Int, spellCostPerLevel: Map[Int, Int], spellPointTotal: Map[Int, Int]): Unit = {
    spellPoints = spellPointTotal.map { case (k, _) => k -> 0 }
    totalPointsLabel.text = s"Total spell points: ${spellPointTotal(level)}"

    // Sets the maximum value and listener for each spinner
    spellLevelSpinners.foreach(spinner => {
      val level = spinner.userData.asInstanceOf[Int]
      val spellLevel = spellCostPerLevel(level)

      val valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, spellPointTotal(level), 0)
      valueFactory.setWrapAround(true)
      valueFactory.valueProperty().addListener((_, _, newValue) => {
        val level = spinner.userData.asInstanceOf[Int]
        spellPoints += (level -> newValue)
        totalPointsLabel.text = s"Total spell points: ${spellPoints.values.sum}"
        spellLevelSpinners.foreach(otherSpinner => {
          if (otherSpinner != spinner) {
            val otherValueFactory = otherSpinner.getValueFactory.asInstanceOf[SpinnerValueFactory.IntegerSpinnerValueFactory]
            otherValueFactory.setMax((spellPointTotal(level) - spellPoints.values.sum) / spellCostPerLevel(otherSpinner.userData.asInstanceOf[Int]))
          }
        })
      })
      spinner.valueFactory = valueFactory.asInstanceOf[SpinnerValueFactory[Int]]
    })
  }

  /*def setCasterLevel(level: Int): Unit = {
    println(s"Setting caster level to $level")
    spellPoints = spellPointTotal.map { case (k, _) => k -> 0 }
    totalPointsLabel.text = s"Total spell points: ${spellPointTotal(level)}"

    // Set the maximum value for each spinner
    spellLevelSpinners.foreach(spinner => {
      val level = spinner.userData.asInstanceOf[Int]
      val spellLevel = spellCostPerLevel(level)

      val valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, spellPointTotal(casterLevelComboBox.getValue), 0)
      valueFactory.setWrapAround(true)
      valueFactory.valueProperty().addListener((_, _, newValue) => {
        println(s"Spinner value changed to $newValue")
        val level = spinner.userData.asInstanceOf[Int]
        spellPoints += (level -> newValue)
        totalPointsLabel.text = s"Total spell points: ${spellPoints.values.sum}"
        spellLevelSpinners.foreach(otherSpinner => {
          if (otherSpinner != spinner) {
            val otherValueFactory = otherSpinner.getValueFactory.asInstanceOf[SpinnerValueFactory.IntegerSpinnerValueFactory]
            otherValueFactory.setMax((spellPointTotal(casterLevelComboBox.getValue) - spellPoints.values.sum) / spellCostPerLevel(otherSpinner.userData.asInstanceOf[Int]))
          }
        })
      })
      spinner.valueFactory = valueFactory.asInstanceOf[SpinnerValueFactory[Int]]

      /*val valueFactory = spinner.valueFactory.value.asInstanceOf[SpinnerValueFactory.IntegerSpinnerValueFactory]
      valueFactory.setWrapAround(true)
      valueFactory.setValue(0)
      valueFactory.setMin(0)
      valueFactory.setMax(spellPointTotal(level) / spellLevel)
      spinner.setValueFactory(valueFactory)*/
    })
  }*/

  val spellLevelSpinners: Seq[Spinner[Int]] = spellCostPerLevel.keys.toSeq.sorted.map(level => {
    val spinner = new Spinner[Int](0, spellPointTotal(casterLevelComboBox.getValue) / spellCostPerLevel(level), 0)
    spinner.userData = level.asInstanceOf[AnyRef]
    spinner.value.onChange((_, _, newValue) => {
      val spellLevel = spinner.userData.asInstanceOf[Int]
      spellPoints += (spellLevel -> (newValue * spellCostPerLevel(spellLevel)))
      totalPointsLabel.text = s"Total spell points: ${spellPoints.values.sum}"
    })
    spinner
  })

  val casterLevelComboBox = new ComboBox[Int](1 to 20)
  casterLevelComboBox.selectionModel().selectFirst()
  casterLevelComboBox.value.onChange((_, _, newValue) => {
    setCasterLevel(newValue, spellCostPerLevel, spellPointTotal)
  })
  /*casterLevelComboBox.value.onChange((_, _, newValue) => {
    setCasterLevel(newValue)
  })*/


  /*def setCasterLevel(level: Int): Unit = {
    spellPoints = spellPointTotal.map { case (k, _) => k -> 0 }
    totalPointsLabel.text = s"Total spell points: ${spellPointTotal(level)}"
    
    // Set the maximum value for each spinner
    spellLevelSpinners.foreach(spinner => {
      val valueFactory = spinner.valueFactory.value
      
      valueFactory.setWrapAround(true)
      valueFactory.setValue(0)
      //valueFactory.maxValueProperty().setValue(Integer.valueOf(spellPointTotal(level)))

      spinner.setValueFactory(valueFactory)
      //spinner.getValueFactory.setValue(valueFactory) // Update the spinner value if necessary
    })
  }

  val casterLevelComboBox = new ComboBox[Int](1 to 20)
  casterLevelComboBox.selectionModel().selectFirst()
  casterLevelComboBox.value.onChange((_, _, newValue) => {
    setCasterLevel(newValue)
  })

  val spellLevelSpinners: Seq[Spinner[Int]] = spellPointTotal.keys.toSeq.sorted.map(level => {
    val spinner = new Spinner[Int](0, spellPointTotal(casterLevelComboBox.getValue), 0)
    spinner.userData = level.asInstanceOf[AnyRef]
    spinner.value.onChange((_, _, newValue) => {
      spellPoints += (level -> newValue)
      totalPointsLabel.text = s"Total spell points: ${spellPoints.values.sum}"
    })
    spinner
  })*/

  val totalPointsLabel = new Label(s"Total spell points: ${spellPointTotal.values.sum}")

  val spellLevelLabels: Seq[Label] = spellCostPerLevel.keys.toSeq.sorted.map(level =>
    new Label(s"Level $level (${spellCostPerLevel(level)} points)")
  )

  val gridPane = new GridPane {
    hgap = 10
    vgap = 10
    padding = Insets(10)
    add(new Label("Caster level:"), 0, 0)
    add(casterLevelComboBox, 1, 0)
    spellLevelLabels.zipWithIndex.foreach { case (label, index) =>
      add(label, 0, index + 1)
      add(spellLevelSpinners(index), 1, index + 1)
    }
    add(totalPointsLabel, 0, spellLevelLabels.size + 1, 2, 1)
  }

  stage = new PrimaryStage {
    title = "Spell Point Tracker"
    scene = new Scene {
      root = new BorderPane {
        center = gridPane
      }
    }
  }

}
