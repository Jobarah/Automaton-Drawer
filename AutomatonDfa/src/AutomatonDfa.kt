import com.mxgraph.model.mxCell
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.util.mxConstants
import com.mxgraph.util.mxRectangle
import com.mxgraph.view.mxGraph

import javafx.application.Application
import javafx.embed.swing.SwingNode
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import javafx.stage.Stage

fun main(args: Array<String>) {
    Application.launch(MainApplication::class.java)
}

class MainApplication : Application() {
    override fun start(stage: Stage) {

        val graph = mxGraph()
        var automaton:Automaton = Automaton()
        automaton.alphabet.add('0')
        val parent = graph.defaultParent

        graph.update {}

        graph.minimumGraphSize = mxRectangle(0.0,0.0,800.0,320.0)

        val graphComponent = mxGraphComponent(graph)

        graph.isAllowLoops = true

        graph.isAllowDanglingEdges = false

        graph.isEdgeLabelsMovable = false

        graph.isCellsResizable = false

        mxConstants.DEFAULT_HOTSPOT = 1.0

        val miCombo = ComboBox<String>()

        val nodes: MutableList<mxCell> = mutableListOf()

        val insertLabel = Label("New Vertex name: ")
        val editVertex = Label("Edit vertex")
        val trans0 = Label("Transition 0")
        val initialState = Label("Is initial state")
        val comboTransition0 = ComboBox<String>()
        val trans1 = Label("Transition 1")
        val comboTransition1 = ComboBox<String>()
        val isAcceptanceState = Label("Is acceptance state")
        val acceptanceCombo = ComboBox<String>()
        acceptanceCombo.items.add("True")
        acceptanceCombo.items.add("False")
        val initialCombo = ComboBox<String>()
        initialCombo.items.add("True")
        initialCombo.items.add("False")
        val deleteLabel = Label("Delete vertex")
        val deleteCombo = ComboBox<String>()
        val separator = Separator()
        val separator2 = Separator()
        val separator3 = Separator()
        val aTextField = TextField()
        aTextField.setMaxSize(120.0,20.0)
        var estadoInicial = ""

        val aButton = Button("Insert")
        val bButton = Button("Apply Changes")
        val cButton = Button("Delete")
        val dButton = Button("Evaluate Automaton")

        aButton.onMouseClicked = EventHandler<MouseEvent> {

            val aceptado = acceptanceCombo.getSelectionModel().getSelectedItem().toString()
            val isInitialState = initialCombo.getSelectionModel().getSelectedItem().toString()
            var sizeX=0.0
            var sizeY=0.0
            var acceptance:Boolean =false
            var initial:Boolean = false
            if(acceptanceCombo.selectionModel.selectedIndex>=0 && aTextField.text!=""){
                var style=""
                if(aceptado == "True"){
                    acceptance = true
                    style = "shape=doubleEllipse;fillColor=white"
                }else{
                    style = "shape=ellipse;fillColor=white"
                }
                if(isInitialState == "True"){
                    initial = true
                    sizeX = 100.00
                    sizeY = 100.00
                }else{
                    sizeX = 50.00
                    sizeY = 50.00
                }
                automaton.states.add(States(aTextField.text,initial,acceptance))
                val vertex = graph.insertVertex(parent, null, aTextField.text, 150.0, 150.0, sizeX, sizeY,style)
                miCombo.items.add(aTextField.text)
                comboTransition0.items.add(aTextField.text)
                comboTransition1.items.add(aTextField.text)
                deleteCombo.items.add(aTextField.text)
                nodes.add(vertex as mxCell)
            }
            graph.refresh()

        }

        bButton.onMouseClicked = EventHandler<MouseEvent> {
            val verEdit = miCombo.getSelectionModel().getSelectedItem().toString()

            if(miCombo.selectionModel.selectedIndex >=0){
                var ct0Opcion = ""
                if(comboTransition0.selectionModel.selectedIndex>=0){
                    ct0Opcion = comboTransition0.getSelectionModel().getSelectedItem().toString()
                }

                var ct1Opcion = ""
                if(comboTransition1.selectionModel.selectedIndex >=0){
                    ct1Opcion = comboTransition1.getSelectionModel().getSelectedItem().toString()
                }

                var cantEdges = nodes[miCombo.items.indexOf(verEdit)].edgeCount
                if(cantEdges==1){
                    nodes[miCombo.items.indexOf(verEdit)].getEdgeAt(0).removeFromParent()
                }

                if(cantEdges==2){
                    nodes[miCombo.items.indexOf(verEdit)].getEdgeAt(0).removeFromParent()
                    nodes[miCombo.items.indexOf(verEdit)].getEdgeAt(1).removeFromParent()
                }

                if(comboTransition0.selectionModel.selectedIndex >=0){
                    graph.insertEdge(parent, null, "0", nodes[miCombo.items.indexOf(verEdit)], nodes[comboTransition0.items.indexOf(ct0Opcion)])
                    var state:States? = automaton.getState(nodes[miCombo.items.indexOf(verEdit)].value.toString())
                    state!!._transition.add(Transition('0',nodes[miCombo.items.indexOf(verEdit)].value.toString(),nodes[comboTransition0.items.indexOf(ct0Opcion)].value.toString()))
                }

                if(comboTransition1.selectionModel.selectedIndex >=0){
                    graph.insertEdge(parent, null, "1", nodes[miCombo.items.indexOf(verEdit)], nodes[comboTransition1.items.indexOf(ct1Opcion)])
                    var state:States? = automaton.getState(nodes[miCombo.items.indexOf(verEdit)].value.toString())
                    state!!._transition.add(Transition('1',nodes[miCombo.items.indexOf(verEdit)].value.toString(),nodes[comboTransition0.items.indexOf(ct1Opcion)].value.toString()))
                }
            }
            graph.refresh()
        }

        cButton.onMouseClicked = EventHandler<MouseEvent> {
            if(deleteCombo.selectionModel.selectedIndex > -1){
                val verDelete = deleteCombo.getSelectionModel().getSelectedItem().toString()
                nodes[miCombo.items.indexOf(verDelete)].removeFromParent()
                graph.removeSelectionCell(nodes[miCombo.items.indexOf(verDelete)])
                graph.refresh()
                nodes.removeAt(deleteCombo.items.indexOf(verDelete))
                comboTransition0.items.removeAt(deleteCombo.items.indexOf(verDelete))
                comboTransition1.items.removeAt(deleteCombo.items.indexOf(verDelete))
                miCombo.items.removeAt(deleteCombo.items.indexOf(verDelete))
                deleteCombo.items.removeAt(deleteCombo.items.indexOf(verDelete))
                graph.refresh()
            }
        }
        dButton.onMouseClicked = EventHandler<MouseEvent> {
            println(automaton.evaluate("0000"))
        }

        val sceneRoot = GridPane()
        sceneRoot.setPrefSize(400.0, 400.0)
        sceneRoot.setVgap(5.0)
        sceneRoot.setHgap(1.0)
        sceneRoot.add(SwingNode().apply {
            content = graphComponent
        }, 0, 0)
        sceneRoot.add(insertLabel, 0, 1)
        sceneRoot.add(aTextField, 0, 2)
        sceneRoot.add(isAcceptanceState, 0, 3)
        sceneRoot.add(acceptanceCombo, 0, 4)
        sceneRoot.add(initialState,0,5)
        sceneRoot.add(initialCombo, 0, 6)
        sceneRoot.add(aButton, 0, 7)
        sceneRoot.add(separator, 0, 8)
        sceneRoot.add(editVertex, 0, 9)
        sceneRoot.add(miCombo, 0, 10)
        sceneRoot.add(trans0, 0, 11)
        sceneRoot.add(comboTransition0, 0, 12)
        sceneRoot.add(trans1, 0, 13)
        sceneRoot.add(comboTransition1, 0, 14)
        sceneRoot.add(bButton, 0, 15)
        sceneRoot.add(separator2, 0, 16)
        sceneRoot.add(deleteLabel, 0, 17)
        sceneRoot.add(deleteCombo, 0, 18)
        sceneRoot.add(cButton, 0, 19)
        sceneRoot.add(separator3, 0, 20)
        sceneRoot.add(dButton,0,21)

        stage.scene = Scene(sceneRoot, 825.0, 900.0)

        stage.show()
    }

    private fun  mxGraph.update(block: () -> Any) {
        model.beginUpdate()
        try {
            block()
        }
        finally {
            model.endUpdate()
        }
    }
}