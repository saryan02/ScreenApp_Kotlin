package com.example.windowapps

//import java.util.logging.Level
//import java.util.logging.Logger

import javafx.application.Application
import javafx.collections.FXCollections
import javafx.embed.swing.SwingFXUtils
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.input.*
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import javafx.stage.Stage
import javafx.scene.control.Menu
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.image.RenderedImage
import java.io.File
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger
import javax.imageio.ImageIO


class ScreenshotApp : Application(){
     override fun  start(stage: Stage){

         val root = VBox()
         val a = 0
         val scene = Scene(root, 500.0, 400.0)
         val menuBar = MenuBar()
         val menu  = Menu("Program")
         val closeItem = MenuItem("Close")
         val saveItem = MenuItem("Save")
         val switchImgItem = MenuItem("Switch")
         menu.items.addAll(closeItem, saveItem, switchImgItem)
         menuBar.menus.add(menu)

         val loadImage = Button("load")
         loadImage.translateX = 400.0

         val canvas = Canvas()
         canvas.width = 500.0
         canvas.height = 400.0

         val g = canvas.graphicsContext2D

         val takeScreenshot = Button("Screenshot")

         val colorPicker = ColorPicker()
         val langs = FXCollections.observableArrayList(
             "1", "2", "3", "4", "5",
             "6", "7", "8", "9", "10",
             "11", "12", "13", "14", "15",
             "16", "17", "18")
         val langsComboBox = ComboBox(langs)
         val slider = Slider(1.0, 3.0, 0.0)
         val checkbox = CheckBox()
         val kCombSave = KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_ANY)
         val kCombCreate = KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_ANY )
         checkbox.isSelected = true
         checkbox.translateX = 400.0

         slider.isShowTickMarks = true
         slider.isShowTickLabels = true
         slider.blockIncrement = 1.0
         slider.majorTickUnit = 1.0;
         slider.minorTickCount = 0
         slider.isSnapToTicks = true
         slider.setMaxSize(150.0,10.0)
         slider.translateX = 250.0


         langsComboBox.translateX = 185.0
         langsComboBox.value = "6"
         langsComboBox.visibleRowCount = 6


         colorPicker.setPrefSize(100.0, 25.0)
         colorPicker.translateX = 80.0
         colorPicker.value = Color.RED

        closeItem.onAction = EventHandler {
            stage.close()
        }

        saveItem.onAction = EventHandler {
            saveCanvas(stage, scene, canvas)
        }

        switchImgItem.onAction = EventHandler {
            initDraw(g)
        }

        loadImage.onAction = EventHandler {
            initDraw(g)
        }


         takeScreenshot.onAction = EventHandler {
            createScreen(stage, checkbox, slider)
         }


        canvas.onMouseDragged = EventHandler<MouseEvent> { e->
            val size : Double = langsComboBox.value.toDouble()
            val x = e.x - size / 2
            val y = e.y - size / 2

            if(e.button == MouseButton.SECONDARY){
                g.clearRect(x, y, size, size)
            }else{
                g.fill = colorPicker.value
                g.fillRect(x, y, size, size)
            }



        }

         scene.onKeyPressed = EventHandler<KeyEvent> { e->
             if(kCombSave.match(e)){
                saveCanvas(stage, scene, canvas)
             }

             if (kCombCreate.match(e)){
                 createScreen(stage, checkbox,slider)
             }
         }


         val buttons = Group()
         buttons.children.addAll(takeScreenshot, colorPicker, langsComboBox, slider, checkbox, loadImage)

         root.children.addAll(menuBar,buttons, canvas)


         stage.title = "Paint"
         stage.scene = scene

         stage.show()

     }


    private  fun saveCanvas(stage: Stage, scene: Scene, canvas: Canvas){

        val fileChooser = FileChooser()


        val extFilter = FileChooser.ExtensionFilter("png files (*.png)", "*.png")
        fileChooser.extensionFilters.add(extFilter)


        val file = fileChooser.showSaveDialog(stage)
        if (file != null) {
            try {
                val writableImage = WritableImage(scene.width.toInt(), scene.height.toInt())
                canvas.snapshot(null, writableImage)
                val renderedImage: RenderedImage = SwingFXUtils.fromFXImage(writableImage, null)
                ImageIO.write(renderedImage, "png", file)
            } catch (ex: IOException) {
                Logger.getLogger(ScreenshotApp::class.java.getName()).log(Level.SEVERE, null, ex)
            }
        }
    }

    private  fun createScreen(stage: Stage, checkbox: CheckBox,slider:Slider ){
        if(checkbox.isSelected)
            stage.isIconified = true
        if (slider.value == 0.0)
            slider.value = 1.0

        Thread.sleep(slider.value.toLong() * 1_000)
        saveScreen()
        stage.isIconified = false
    }


    private fun initDraw(gc: GraphicsContext) {

        val canvasWidth = gc.canvas.width
        val canvasHeight = gc.canvas.height

        val bgX = canvasWidth / 30
        val bgY = canvasHeight / 30

        val fileChooser = FileChooser()
//        fileChooser.initialDirectory = File(System.getProperty("user.home"))
        val file = fileChooser.showOpenDialog(null)
        print(file.path)


        val image = Image(file.toURI().toString())

      gc.drawImage(image, bgX, bgY)

    }

    private fun saveScreen(){
        try{

            val robot = Robot()
            val fileChooser = FileChooser()
            val extFilter = FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg")
            fileChooser.extensionFilters.add(extFilter)
            val file = fileChooser.showSaveDialog(null)
            val screenSize = Toolkit.getDefaultToolkit().screenSize
            val captureRect = Rectangle(0 ,0, screenSize.width / 2, screenSize.height / 2)

            val screemFullImage = robot.createScreenCapture(captureRect)
            ImageIO.write(screemFullImage, "jpg", File(file.path));

        }
        catch (ex: IOException){
            print(ex)
        }
    }



}



fun main() {
    Application.launch(ScreenshotApp::class.java)
}