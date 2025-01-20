package com.nyethack

import org.based.com.nyethack.Narrator.narrate
import kotlin.random.Random

interface Fightable {
    val name: String
    var healthPoints: Int
    val diceCounter: Int
    val diceSides: Int

    fun takeDamage(damage: Int)

    fun attack(opponent:Fightable) {
        val damageRoll = (0 until diceCounter).sumOf {
            Random.nextInt(diceSides +1)
        }
        narrate("$name deals $damageRoll to ${opponent.name}")
        opponent.takeDamage(damageRoll)
    }
}

abstract class Monster(
    override val name: String,
    val description: String,
    override var healthPoints: Int
) : Fightable {
    override fun takeDamage(damage: Int) {
        healthPoints -= damage
    }
}

class Goblin (
    name: String = "Goblin",
    description: String = "A nasty-looking goblin",
    healthPoints: Int = 30
) : Monster(name, description, healthPoints) {
    override val diceCounter = 2
    override val diceSides = 8
}