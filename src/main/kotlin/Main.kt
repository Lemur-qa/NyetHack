package org.based

import org.based.Narrator.changeNarratorMood
import org.based.Narrator.narrate

var heroName: String = "qwwer"

fun main() {
    /*narrate("A hero enters the town. What is their name?",
    ::makeYellow
    )
    val heroName = readLine()
    require(heroName != null && heroName.isNotEmpty()) {
        "The hero must have a name"
    }*/

    // changeNarratorMood()
    narrate("$heroName, ${createTitle(heroName)} heads to the town square")

    visitTavern()
}

private fun createTitle(name: String): String {
    return when {
        name.all { it.isDigit() } -> "The Identifieble Number"
        name.none { it.isLetter() } -> "The Protected"
        name.count { it.lowercase() in "aeiouy" } > 4 -> "The Master Of Vowels"
        else -> "The Renowned Hero"
    }
}

private fun makeYellow(message: String) = "\u001b[33;1m$message\u001b[0m"