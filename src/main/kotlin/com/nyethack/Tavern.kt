package org.based.com.nyethack

import com.nyethack.Room
import org.based.com.nyethack.Narrator.narrate
import java.io.File
import kotlin.random.Random
import kotlin.random.nextInt

private const val TAVERN_MASTER = "Taernyl"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"

private val firstNames = setOf("Alex", "Mordoc", "Sophie", "Tariq")
private val lastNames = setOf("Ironfoot", "Fernsworth", "Baggins", "Downstrider")

private val menuData = File("data/tavern-menu-data.txt")
    .readText()
    .split("\n") //Разбивает эту строку по разделителю "\n" (символ перевода строки), превращая её в список строк.
    /*.map { it.split(",") }*///вместо того, чтобы сплитить menuData в каждой переменной, сплитнем здесь
    //Каждая строка из menuData разбивается на подстроки с помощью разделителя ",".
    .map { line ->
        val(type, name, price) = line.split(",")
        Triple(type.trim(), name.trim(), price.trim().toDouble())
    }
private val menuItems = menuData.map { (_, name, _) -> name }
private val menuItemsTypes = menuData.associate { (type, name, _) -> name to type }
private val menuItemPrices = menuData.associate { (_, name, price) -> name to price }

class Tavern : Room(TAVERN_NAME) {
    override val status = "Busy"
    private val patrons: MutableSet<String> = firstNames.shuffled()
        .zip(lastNames.shuffled()) { firstName, lastName -> "$firstName $lastName" }.toMutableSet()
    private val patronGold = mutableMapOf(
        TAVERN_MASTER to 86.00,
        player.name to 4.50,
        *patrons.map { it to 6.00 }.toTypedArray())
    private val itemOfDay = patrons.flatMap { getFavoriteMenuItems(it) }.random()

    override fun enterRoom() {
        narrate("${player.name} enters $TAVERN_NAME")
        narrate("There are several items for sale:")
        narrate("The item of day is $itemOfDay")
        narrate("${player.name} sees several patrons in the tavern:")
        narrate(patrons.joinToString())

        //val itemOfDay = patrons.flatMap { getFavoriteMenuItems(it).random() }
        //то что далее не оч что переменными так реализовано
        val favoriteItems = patrons.associateWith { patron ->
            getFavoriteMenuItems(patron)
        }
        println("Favorite items: ${favoriteItems.entries.joinToString { "${it.key} - ${it.value}" }}")
        // второй элемент — имя блюда
        val randomMenuItem = menuData.random()
        println(patronGold)
        //заказ
        placeOrder(patrons.random(), randomMenuItem.second, patronGold)
    }

    //разместить и принести заказ
    private fun placeOrder(
        patronName: String,
        menuItemName: String,
        patronGold: MutableMap<String, Double>,
        /*patronGold: MutableMap<String, Double>*/) {
        val itemPrice = menuItemPrices.getValue(menuItemName)
        narrate("$patronName speaks with $TAVERN_MASTER to place an order")
        if (itemPrice <= this.patronGold.getOrDefault(patronName, 0.0)) {
            val action = when (menuItemsTypes[menuItemName]) {
                "shandy", "elixir" -> "pours"
                "meal" -> "serves"
                 else -> "hands"
            }
            narrate("$TAVERN_MASTER $action $patronName a $menuItemName")
            narrate("$patronName pays $TAVERN_MASTER $itemPrice gold")
            this.patronGold[patronName] = this.patronGold.getValue(patronName) - itemPrice
            this.patronGold[TAVERN_MASTER] = this.patronGold.getValue(TAVERN_MASTER) + itemPrice
        } else {
            narrate("$TAVERN_MASTER says, \"You need more coin for a $menuItemName\"")
        }
    }

//найти любимое блюдо. Фильтруем список из строк
private fun getFavoriteMenuItems(patron: String): List<String> {
    return when (patron) {
        "Alex Ironfoot" -> menuItems.filter { menuItem ->
            menuItemsTypes[menuItem]?.contains("dessert") == true
        } else -> menuItems.shuffled().take(Random.nextInt(1..2)) }
    }
}

