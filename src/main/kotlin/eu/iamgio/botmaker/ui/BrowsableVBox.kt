package eu.iamgio.botmaker.ui

import eu.iamgio.botmaker.root
import javafx.application.Platform
import javafx.css.PseudoClass
import javafx.scene.Node
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox

/**
 * VBox which can be browsed through arrow keys
 * @author Giorgio Garofalo
 */
open class BrowsableVBox(listenForRootClicks: Boolean) : VBox() {

    private var index = -1

    private fun increase() {
        if(index >= children.size - 1) {
            index = 0
        } else {
            index++
        }
    }

    private fun decrease() {
        if(index <= 0) {
            index = children.size - 1
        } else {
            index--
        }
    }

    private fun Node.updateSelectedPseudoClass(active: Boolean) {
        this.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), active)
    }

    private fun unselect(resetIndex: Boolean = false) {
        children.forEach { child -> child.updateSelectedPseudoClass(false) }
        if(resetIndex) index = -1
    }

    init {
        isFocusTraversable = true
        setOnKeyPressed {
            if(!isFocused) return@setOnKeyPressed
            when(it.code) {
                KeyCode.UP -> decrease()
                KeyCode.DOWN -> increase()
                else -> return@setOnKeyPressed
            }
            unselect(resetIndex = false)
            children[index].updateSelectedPseudoClass(true)
        }
        setOnKeyReleased {
            if(isFocused && it.code == KeyCode.ENTER) {
                val node = children[index]
                if(node is Actionable) node.onAction()
            }
        }

        if(listenForRootClicks) {
            Platform.runLater {
                root.addEventFilter(MouseEvent.MOUSE_CLICKED) { event ->
                    val target = children.firstOrNull { it == event.target || it == (event.target as? Node)?.parent }
                    unselect()
                    index = children.indexOf(target)
                    target?.updateSelectedPseudoClass(true)
                }
            }
        }

        focusedProperty().addListener { _ -> if(!isFocused) unselect() }
    }
}

interface Actionable {

    fun onAction()
}