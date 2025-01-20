package org.based

import org.based.Narrator.narrate
import java.io.File
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

private const val TAVERN_MASTER = "Taernyl"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"
// Generator of random values
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
private val menuItemPrices = menuData.associate { (_, name, price) -> name to price.toDouble() }

fun visitTavern() {
    narrate("$heroName enters $TAVERN_NAME")
    narrate("There are several items for sale:")
    // генерация уникальных имен
    val patrons: MutableSet<String> = firstNames.shuffled()
        .zip(lastNames.shuffled()) { firstName, lastName -> "$firstName $lastName" }.toMutableSet()

    val patronGold = mutableMapOf(
        TAVERN_MASTER to 86.00,
        heroName to 4.50,
        *patrons.map { it to 6.00 }.toTypedArray()
    )
    println(patronGold["qwwer"])
    /*    val eliMessage = if(patrons.contains("Eli")) {
        "$TAVERN_MASTER says: Eli's in the back playing cards"
    } else {
        "$TAVERN_MASTER says: Eli isn't here"
    }
    println(eliMessage)
    val othersMessage = if (patrons.containsAll(listOf("Sophie", "Mordoc"))) {
        "$TAVERN_MASTER says: Sophie and Mordoc are seated by the stew kettle"
    } else {
        "$TAVERN_MASTER says: Sophie and Morcod aren't with each other right now"
    }
    println(othersMessage)*/
    /*patrons.forEachIndexed { index, patron ->
        println("Good evening, $patron - you're #${index + 1} in line")
        placeOrder(patron, "Dragon's Breath")
    }*/
    narrate("$heroName sees several patrons in the tavern:")
    narrate(patrons.joinToString())

    val favoriteItems = patrons.associateWith { patron ->
        getFavoriteMenuItems(patron)
    }
    println("Favorite items: ${favoriteItems.entries.joinToString { "${it.key} - ${it.value}" }}")
     // второй элемент — имя блюда
    val randomMenuItem = menuData.random()
    println(patronGold)
    placeOrder(patrons.random(), randomMenuItem.second, patronGold)
    displayPatronBalances(patronGold)

    // выгоняем
    val departingPatrons: List<String> = patrons
        .filter { patron -> patronGold.getOrDefault(patron, 0.0) < 4.0}
        patrons -= departingPatrons
        patronGold -= departingPatrons
        departingPatrons.forEach { patron -> narrate("$heroName sees $patron departing the tavern") }
        narrate("There are still some ${ patrons.joinToString() }}")
}


//найти любимое блюдо. Фильтруем список из строк
private fun getFavoriteMenuItems(patron: String): List<String> {
    return when (patron) {
        "Alex Ironfoot" -> menuItems.filter { menuItem ->
            menuItemsTypes[menuItem]?.contains("dessert") == true
        } else -> menuItems.shuffled().take(Random.nextInt(1..2))
    }
}

//разместить и принести заказ
private fun placeOrder(patronName: String,
                       menuItemName: String,
                       patronGold: MutableMap<String, Double>) {
    val itemPrice = menuItemPrices.getValue(menuItemName)
    narrate("$patronName speaks with $TAVERN_MASTER to place an order")
    if (itemPrice <= patronGold.getOrDefault(patronName, 0.0)) {
        val action = when (menuItemsTypes[menuItemName]) {
            "shandy", "elixir" -> "pours"
            "meal" -> "serves"
             else -> "hands"
        }
        narrate("$TAVERN_MASTER $action $patronName a $menuItemName")
        narrate("$patronName pays $TAVERN_MASTER $itemPrice gold")
        patronGold[patronName] = patronGold.getValue(patronName) - itemPrice
        patronGold[TAVERN_MASTER] = patronGold.getValue(TAVERN_MASTER) + itemPrice
    } else {
        narrate("$TAVERN_MASTER says, \"You need more coin for a $menuItemName\"")
    }
}

//отобразить баланс на кошельке патронов
private fun displayPatronBalances(patronGold: Map<String, Double>) {
    narrate("$heroName intuitevely knows how much money each patron has")
    patronGold.forEach { (patron, balance) ->
        narrate("$patron hййas ${"%.2f".format(balance)} gold")
    }
}
/*// Function to generate menu items
private fun generateMenuData(): List<Triple<String, String, Double>> {
    val decimalFormat = DecimalFormat("#.##", DecimalFormatSymbols(Locale.US))
    return List(10) {
        val type = itemTypes.random()
        val name = itemNames.random()
        val rawPrice = Random.nextDouble(priceRange.start, priceRange.endInclusive)
        val formattedPrice = decimalFormat.format(rawPrice) // Ensures '.' as decimal separator
        Triple(type, name, formattedPrice.toDouble())
    }
}*/

/*private fun displayMenu() {
    groupedMenu.forEach { (type, items) ->
        println("~[$type]~")
        items.forEach { (_, name, price) ->
            val dotsCount = maxNameLength - name.length + 10
            val dots = ".".repeat(dotsCount)
            println("$name$dots${"%.2f".format(price)}")
        }
    }
}*/

