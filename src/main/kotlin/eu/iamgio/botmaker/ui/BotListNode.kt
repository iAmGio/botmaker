package eu.iamgio.botmaker.ui

import eu.iamgio.botmaker.ui.botcontrol.BotControlPane
import javafx.scene.control.Label
import javafx.scene.input.MouseButton

/**
 * @author Giorgio Garofalo
 */
class BotListNode : BrowsableVBox(true) {

    init {
        styleClass += "bot-list"
    }

    inner class ListedBotNode(private val name: String) : Label(name), Actionable {

        init {
            styleClass += "bot-name"
            prefWidthProperty().bind(this@BotListNode.prefWidthProperty())

            setOnMouseClicked {
                if(it.button == MouseButton.PRIMARY) {
                    if(it.clickCount == 2) {
                        open()
                    }
                } else if(it.button == MouseButton.SECONDARY) {
                    BotContextMenu(this).show(it.screenX, it.screenY)
                }
            }
        }

        fun open() {
            println("Bot $text")
            BotControlPane(name).show()
        }

        fun delete() {
            println("Delete $text")
            // TODO
        }

        override fun onAction() = open()
    }
}