
package KotlinAlgorithm;

import java.io.Serializable

/**
 * Created by Jobarah on 7/25/2016.
 */

open abstract class Automaton: Serializable {
    var states = mutableListOf<State>()
    var alphabet = mutableListOf<String>()
    abstract var type:String

    open fun addState(name:String,isInitialState:Boolean,isAcceptanceState:Boolean):Unit{
        states.add(State(name,isInitialState,isAcceptanceState))
    }

    open fun getInitialState(): State?{
        for (i in states.indices) {
            if (states[i]._initialState) {
                return states[i]
            }
        }
        return null
    }

    open fun getState(stateName:String): State?{
        for (state in states) {
            if(state._name.equals(stateName)){
                return state
            }
        }
        return null
    }

    open abstract fun evaluate(strEvString:String):Boolean

    open fun getDestinyState(stateName: String, symbol: String):State? {
        val originState = getState(stateName) as State
        for(transition in originState._transitions){
            if(transition._symbol == symbol){
                return getState(transition._destiny)
            }
        }
        return null
    }
    open fun getDestinyStates(state:State, symbol: String):MutableList<State> {
        var destinies:MutableList<State> = mutableListOf()

        for (transition in state._transitions) {
            if (transition._symbol.equals(symbol)) {
                var stateToAdd = getState(transition._destiny)
                if (stateToAdd != null) {
                    destinies.add(stateToAdd)
                }
            }
        }
        return destinies
    }

    open fun clearAutomaton(){
        states.clear()
        alphabet.clear()
    }

    open fun getAutomatonAlphabet(): MutableList<String>{
        return alphabet
    }
    open fun getAutomatonStates(): MutableList<State> {
        return this.states
    }

    fun stringToCharList(string:String):MutableList<String> {
        var stringList = string.toCharArray()
        var charList:MutableList<String> = mutableListOf()

        for (char in stringList) {
            charList.add(char.toString())
        }

        return charList
    }

    open fun addTransition(initialState: String, destinyState: String, symbol: String): Boolean {
        var stateA = getState(initialState)
        var stateB = getState(destinyState)

        if (stateA != null && stateB != null) {
            stateA.addTransition(symbol, stateB._name)
            return true
        }
        return false
    }

    open fun changeName(oldName:String, newName:String){
        if(getState(oldName) != null){
            for(vertex in states){
                for(transicion in vertex._transitions){
                    if(transicion._destiny == oldName){
                        transicion._destiny = newName
                    }
                    if(transicion._origin == oldName){
                        transicion._origin = newName
                    }
                }
            }
            var verticeCambiar = getState(oldName)
            verticeCambiar!!._name = newName
        }
    }

}