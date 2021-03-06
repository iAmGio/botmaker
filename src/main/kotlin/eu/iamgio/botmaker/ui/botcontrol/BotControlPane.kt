package eu.iamgio.botmaker.ui.botcontrol

import animatefx.animation.FadeInUp
import eu.iamgio.botmaker.Settings
import eu.iamgio.botmaker.lib.BotConfiguration
import eu.iamgio.botmaker.root
import eu.iamgio.botmaker.save
import eu.iamgio.botmaker.ui.SVG_CONSOLE
import eu.iamgio.botmaker.ui.botcontrol.event.MessageEventNode
import eu.iamgio.botmaker.ui.botcontrol.event.NewEventButton
import eu.iamgio.botmaker.ui.createSvg
import eu.iamgio.botmaker.ui.splitcontrols.ConsoleSplitControl
import eu.iamgio.botmaker.ui.withClass
import eu.iamgio.botmaker.ui.wrap
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox

/**
 * Pane which covers the whole second part of the main SplitPane. Contains all the controls needed to make/edit a bot.
 * @author Giorgio Garofalo
 */
class BotControlPane(
        val name: String,
        val bot: BotConfiguration,
        private val settings: Settings) : VBox() {

    val eventsVBox = VBox()

    init {
        styleClass += "bot-control-pane"
        stylesheets += "/css/botcontrol.css"

        children += HBox().apply {
            alignment = Pos.CENTER_LEFT
            children += Label(name).withClass("title")
            children += TokenBox(this@BotControlPane)
        }

        children += NewEventButton(this)

        children += Pane(createSvg(SVG_CONSOLE).wrap().withClass("console-svg").apply {
            var consoleControl = ConsoleSplitControl(name)
            setOnMouseClicked {
                if(root.consoleControl == null || root.consoleControl!!.botName != name) {
                    consoleControl = root.addConsole(name, consoleControl)
                } else {
                    root.removeConsole()
                }
            }
        }).apply { setMinSize(50.0, 50.0) }

        children += ScrollPane(eventsVBox).withClass("edge-to-edge")

        bot.messageEvents.forEach {
            eventsVBox.children += MessageEventNode(it, this)
        }
    }

    fun show() {
        FadeInUp(this).setSpeed(2.0).play()
        root.rightControl.showBotControl(this)
    }

    fun save() = bot.save(name)

    fun autosave() {
        if(settings.autoSave) save()
    }

    fun openConsole() {
        autosave()
        root.addConsole(name)
    }
}