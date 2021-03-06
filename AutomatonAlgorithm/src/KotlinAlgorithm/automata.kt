package KotlinAlgorithm;
/**
 * Created by Jobarah on 7/25/2016.
 */
fun main(arr : Array<String>){
    var a =  Automaton()
    val state1 = States("q0",true,false)
    val states2 = States("q1",false,false)
    val states3 = States("q2",false,false)
    val states4 = States("q3",false,false)
    val states5 = States("q4",false,true)
    var transition1 = Transition('0',"q0","q1")
    var transition2 = Transition('0',"q1","q2")
    var transition3 = Transition('0',"q2","q3")
    var transition4 = Transition('0',"q3","q0")
    var transition5 = Transition('1',"q3","q4")
    state1._transition.add(transition1)
    states2._transition.add(transition2)
    states3._transition.add(transition3)
    states4._transition.add(transition4)
    states4._transition.add(transition5)
    a.states.add(state1)
    a.states.add(states2)
    a.states.add(states3)
    a.states.add(states4)
    a.states.add(states5)
    a.alphabet.add('0')
    a.alphabet.add('1')
    if(a.evaluate("0000")){
        println("paso")
    }
    else{
        println("no paso")
    }
}
