package org.based.com.nyethack

import com.nyethack.Fightable
import org.based.com.nyethack.Narrator.narrate

class Weapon(val name: String)

class Player(
    initialName: String,
    val hometown: String = "Neversummer",
    override var healthPoints: Int,
    val isImmortal: Boolean,
) : Fightable {
    override var name = initialName
        //меняя get на свой, я меняю его поведение при попытке прочитать значение. возвращается строка ис помощью гет мы ее меняем
        get() = field.replaceFirstChar { it.uppercase() }
        private set(value) {
            field = value.trim()
        }

    override val diceCounter = 3
    override val diceSides = 4
    override fun takeDamage(damage: Int) {
        if (!isImmortal) {
            healthPoints -= damage
        }
    }
    val title: String
        get() = when {
        name.all { it.isDigit() } -> "The Identifieble Number"
        name.none { it.isLetter() } -> "The Protected"
        name.count { it.lowercase() in "aeiouy" } > 4 -> "The Master Of Vowels"
        else -> "The Renowned Hero"
    }
    val prophecy by lazy {
        narrate("$name embarks on an arduous quest to locate a fortune teller")
        Thread.sleep(3000)
        narrate("The fortune teller bestows a prophecy upon $name")
        "An intrepid hero from $hometown shall some day " + listOf(
            "form an unlikely bond between two warring factions",
            "bring the gift of creation back to the world",
            "best the world-eater"
        ).random()
    }

    init {
        require(healthPoints > 0) { "healthPoints must be greater than zero" }
    }

    constructor(name: String) : this (
        initialName = name,
        healthPoints = 150,
        isImmortal = false
    ) {
        if (name.equals("Jason", ignoreCase = true)) {
            healthPoints = 500
        }
    }

    var weapon: Weapon? = Weapon("Mjollnir")

    fun printWeaponName() {
        weapon?.let {
            println(it.name)
        }
    }

    fun changeName(newName: String) {
        narrate("$name legally changes their name to $newName")
        name = newName
    }

    fun prophesize() {
        narrate("$name thinks about their future")
        narrate("A fortune teller told Madrigal, \"$prophecy\"")
    }

    fun castFireball(numFireballs: Int = 2) {
        narrate("A glass of Fireball springs into existence (x$numFireballs)")
    }
}
