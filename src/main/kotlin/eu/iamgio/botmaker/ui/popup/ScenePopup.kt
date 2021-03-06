package eu.iamgio.botmaker.ui.popup

import animatefx.animation.ZoomIn
import animatefx.animation.ZoomOut
import eu.iamgio.botmaker.bundle.getString
import eu.iamgio.botmaker.root
import eu.iamgio.botmaker.ui.withClass
import javafx.application.Platform
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox

/**
 * @author Giorgio Garofalo
 */
open class ScenePopup(title: String) : VBox() {

    protected var nodeToFocus: Node? = null
    protected val shownProperty = SimpleBooleanProperty()

    init {
        styleClass += "scene-popup"
        isFocusTraversable = true

        children += Label(title).withClass("popup-title")

        var isRootClicked = false

        root.addEventFilter(MouseEvent.MOUSE_CLICKED) {
            isRootClicked = true
            Platform.runLater {
                if(isRootClicked) hide()
                isRootClicked = false
            }
        }
        addEventFilter(MouseEvent.MOUSE_CLICKED) {
            if(isRootClicked) isRootClicked = false
        }
    }

    fun show() {
        shownProperty.set(true)
        ZoomIn(this).setSpeed(4.0).play()
        root.children += this
        repeat(2) { nodeToFocus?.requestFocus() }
    }

    fun hide() {
        shownProperty.set(false)
        ZoomOut(this).setSpeed(4.0).let {
            it.setOnFinished {
                root.children -= this
            }
            it.play()
        }
    }

    open fun onConfirm() {}

    fun addConfirmButton(textKey: String, hasPriority: Boolean = false) {
        children += VBox(
                Label(getString(textKey)).withClass("confirm").apply {
                    if(hasPriority) {
                        setOnMousePressed { onConfirm() }
                    } else {
                        setOnMouseClicked { onConfirm() }
                    }
                }
        ).apply { alignment = Pos.CENTER }
    }

    fun addError(node: Node) {
        if("error" !in node.styleClass) node.styleClass += "error"
    }

    fun removeError(node: Node) {
        if("error" in node.styleClass) node.styleClass -= "error"
    }
}