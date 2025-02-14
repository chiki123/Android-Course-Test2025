import kotlin.math.pow
import kotlin.math.sqrt

data class SystemParameters(
    val time: Long,
    val age: Int,
    val gender: Char,
    val osVersion: Int,
    val xCoord: Double,
    val yCoord: Double
)

sealed class Push(val text: String) {
    abstract fun shouldFilter(systemParams: SystemParameters): Boolean
}

class LocationPush(
    text: String,
    private val xCoord: Double,
    private val yCoord: Double,
    private val radius: Int,
    private val expiryDate: Long
) : Push(text) {
    override fun shouldFilter(systemParams: SystemParameters): Boolean {
        val distance = sqrt((xCoord - systemParams.xCoord).pow(2) + (yCoord - systemParams.yCoord).pow(2))
        return distance > radius || expiryDate < systemParams.time
    }
}

class AgeSpecificPush(text: String, private val age: Int, private val expiryDate: Long) : Push(text) {
    override fun shouldFilter(systemParams: SystemParameters) = age > systemParams.age || expiryDate < systemParams.time
}

class TechPush(text: String, private val osVersion: Int) : Push(text) {
    override fun shouldFilter(systemParams: SystemParameters) = systemParams.osVersion > osVersion
}

class LocationAgePush(
    text: String,
    private val xCoord: Double,
    private val yCoord: Double,
    private val radius: Int,
    private val age: Int
) : Push(text) {
    override fun shouldFilter(systemParams: SystemParameters): Boolean {
        val distance = sqrt((xCoord - systemParams.xCoord).pow(2) + (yCoord - systemParams.yCoord).pow(2))
        return distance > radius || age > systemParams.age
    }
}

class GenderAgePush(text: String, private val gender: Char, private val age: Int) : Push(text) {
    override fun shouldFilter(systemParams: SystemParameters) = gender != systemParams.gender || age > systemParams.age
}

class GenderPush(text: String, private val gender: Char) : Push(text) {
    override fun shouldFilter(systemParams: SystemParameters) = gender != systemParams.gender
}

fun parseSystemParameters(inputLines: List<String>): SystemParameters {
    var time = 0L
    var age = 0
    var gender = 'M'
    var osVersion = 0
    var xCoord = 0.0
    var yCoord = 0.0

    inputLines.forEach { line ->
        val (key, value) = line.split(" ")
        when (key) {
            "time" -> time = value.toLong()
            "age" -> age = value.toInt()
            "gender" -> gender = value[0]
            "os_version" -> osVersion = value.toInt()
            "x_coord" -> xCoord = value.toDouble()
            "y_coord" -> yCoord = value.toDouble()
        }
    }
    return SystemParameters(time, age, gender, osVersion, xCoord, yCoord)
}

fun parsePush(inputLines: List<String>): Push {
    val type = inputLines.first().split(" ")[1]

    var text = ""
    var xCoord = 0.0
    var yCoord = 0.0
    var radius = 0
    var age = 0
    var gender = 'M'
    var expiryDate = 0L
    var osVersion = 0

    inputLines.drop(1).forEach { line ->
        val (key, value) = line.split(" ")
        when (key) {
            "text" -> text = value
            "x_coord" -> xCoord = value.toDouble()
            "y_coord" -> yCoord = value.toDouble()
            "radius" -> radius = value.toInt()
            "age" -> age = value.toInt()
            "gender" -> gender = value[0]
            "expiry_date" -> expiryDate = value.toLong()
            "os_version" -> osVersion = value.toInt()
        }
    }

    return when (type) {
        "LocationPush" -> LocationPush(text, xCoord, yCoord, radius, expiryDate)
        "AgeSpecificPush" -> AgeSpecificPush(text, age, expiryDate)
        "TechPush" -> TechPush(text, osVersion)
        "LocationAgePush" -> LocationAgePush(text, xCoord, yCoord, radius, age)
        "GenderAgePush" -> GenderAgePush(text, gender, age)
        "GenderPush" -> GenderPush(text, gender)
        else -> throw IllegalArgumentException("Unknown push type: $type")
    }
}

fun main() {
    val systemParams = parseSystemParameters(List(6) { readLine()!! })

    val N = readLine()!!.toInt()
    val pushes = List(N) {
        val M = readLine()!!.toInt()
        parsePush(List(M) { readLine()!! })
    }

    val output = pushes.filterNot { it.shouldFilter(systemParams) }.map { it.text }

    println(if (output.isEmpty()) -1 else output.joinToString("\n"))
}
