package org.based.com.nyethack

import com.nyethack.*
import org.based.com.nyethack.Narrator.narrate
import kotlin.system.exitProcess

lateinit var player: Player

fun main() {
    narrate("Welcome to NyetHack")
    val playerName = promptHeroName()
    player = Player(playerName)
    Game.play()
}

private fun promptHeroName() : String {
    narrate("A hero enters the rown of Kronstadt. What is their name?") { message ->
        "\u001b[33;1m$message\u001b[0m"
    }
    println("Madrigal")
    return "Madrigal"
}

object Game {
    // список списков по координатам. 0.0 Townsquare, 0.1 Tavern.. 1.0 A long corridor, 2.0 dungeon
    private val worldMap = listOf(
        listOf(Townsquare(), Tavern(), Room("Back room")),
        listOf(
            MonsterRoom("A long corridor"), MonsterRoom("A generic room")),
        listOf(MonsterRoom("The Dungeon"))
    )
    private var currentRoom : Room = worldMap[0][0]
    private var currentPosition = Coordinate(0,0)

    init {
        narrate("Welcome to the game, adventurer!")
        val mortality = if (player.isImmortal) "an immortal" else "a mortal"
        narrate ("${player.name}, $mortality, has ${player.healthPoints} health points")
    }

    private class GameInput(arg: String?) {
        private val input = arg ?: ""
        val command = input.split(" ")[0]
        val argument = input.split(" ").getOrElse(1) { "" }
        fun processCommand() = when (command.lowercase()) {
            "move" -> {
                val direction = Direction.values()
                    .firstOrNull() { it.name.equals(argument, ignoreCase = true) }
                if (direction != null) {
                    move(direction)
                } else {
                    narrate("I don't know what direction that is")
                }
            }
            "fight" -> fight()
            "map" -> renderMap()
            else -> narrate("I'm not sure what you're trying to do")
        }
    }

    fun play() {
        while(true) {
            narrate("${player.name} of ${player.hometown}, title: ${player.title}, " +
                    "is in ${currentRoom.description()}")
            currentRoom.enterRoom()
            print("> ENTER YOUR COMMAND: ")
            GameInput(readLine()).processCommand()
        }
    }

    fun move(direction: Direction) {
        val newPosition = direction.updateCoordinate(currentPosition)
        val newRoom = worldMap.getOrNull(newPosition.y)?.getOrNull(newPosition.x)

        if (newRoom != null) {
            narrate("The hero moves ${direction.name}")
            currentPosition = newPosition
            currentRoom = newRoom
        } else {
            narrate("You cannot move ${direction.name}")
        }
    }

    fun fight() {
        val monsterRoom = currentRoom as? MonsterRoom
        val currentMonster = monsterRoom?.monster
        if (currentMonster == null) {
            narrate("There's nothing to fight here")
            return
        }
        while (player.healthPoints > 0 && currentMonster.healthPoints > 0) {
            player.attack(currentMonster)
            if (currentMonster.healthPoints > 0) {
                currentMonster.attack(player)
            }
            Thread.sleep(1000)
        }
        if (player.healthPoints <= 0) {
            narrate("You are defeated! Thanks for dying")
            exitProcess(0)
        } else {
            narrate("${currentMonster.name} has been defeated")
            monsterRoom.monster = null
        }
    }

    private fun renderMap() {
        narrate("Rendering the map...")
        val mapRepresentation = worldMap.mapIndexed { y, row ->
            List(row.size) { x ->
                if (currentPosition == Coordinate(x, y)) "X" else "0"
            }.joinToString(" ")
        }.joinToString("\n")
        println(mapRepresentation)
    }
}