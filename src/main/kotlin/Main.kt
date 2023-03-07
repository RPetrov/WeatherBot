import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Location
import com.pengrad.telegrambot.request.SendLocation
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.SendResponse
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import java.io.FileReader
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.pow
import kotlin.math.sqrt


fun main(args: Array<String>) {
    // Create your bot passing the token received from @BotFather
    // Create your bot passing the token received from @BotFather

    val busStopData = BusStopData()

    val bot = TelegramBot("6140170233:AAE_j09LC-INAf4cnQ2v7EbSNQv44Q0N27A")

// Register for updates

// Register for updates
    bot.setUpdatesListener { updates ->
        updates.forEach { update ->
            val chatId: Long = update.message().chat().id()
            update.message()?.location()?.let {location ->
                val resultBusStop = busStopData.busStops.minBy { it.location.distance(location) }
                bot.execute(SendMessage(chatId, resultBusStop.toString()))
                bot.execute(SendLocation(chatId, resultBusStop.location.latitude,resultBusStop.location.longitude))


                println(resultBusStop) } ?: run {

                val response: SendResponse =
                    bot.execute(SendMessage(chatId, "Пришлите, пожалуйста, ваше местоположение"))
            }
        }
        println(updates)
        UpdatesListener.CONFIRMED_UPDATES_ALL
    }

// Send messages

// Send messages


}

class BusStopData {
    val busStops: List<BusStop>

    init {

        //Files.readAllLines(Paths.get("data/perechen-ostanovochnyh-punktov-s-ukazaniem-vida-transporta-i-s-koordinatami-ih-mestopolozheniya.csv"))
        val `in`: Reader =
            FileReader("data/perechen-ostanovochnyh-punktov-s-ukazaniem-vida-transporta-i-s-koordinatami-ih-mestopolozheniya.csv")

        val csvFormat: CSVFormat = CSVFormat.DEFAULT.builder()
            .setHeader("№,Вид транспортного средства,Тип объекта,Наименование остановки,Официальное наименование,Расположение,Маршруты,Координаты")
            .setSkipHeaderRecord(true)
            .build()

        val records: Iterable<CSVRecord> = csvFormat.parse(`in`)
        busStops = records.map { record ->
            val name: String = record.get(3)
            val adress: String = record.get(5)
            val routes: String = record.get(6)
            val location: BusStopLocation = record.get(7).let {
                val locationString = it.split(",")
                val location =
                    BusStopLocation(locationString[0].toFloat(), locationString[1].toFloat())
                return@let location
            }
            BusStop(adress, name, routes, location)

        }
    }
}

data class BusStop(
    val adress: String,
    val name: String,
    val routes: String,
    val location: BusStopLocation
){
    override fun toString(): String {
        return "Остановка $name находится по адресу $adress.\nДоступные маршруты $routes.\nhttps://www.google.ru/maps/@${location.latitude},${location.longitude}"
    }
}

data class BusStopLocation(val latitude: Float, val longitude: Float) {

    /**
     * https://stackoverflow.com/questions/365826/calculate-distance-between-2-gps-coordinates
     */
    fun distance(
        location: Location
    ): Double {
        val R = 6371 // Radius of the earth
        val latDistance = Math.toRadians(location.latitude().toDouble() - latitude)
        val lonDistance = Math.toRadians(location.longitude().toDouble() - longitude)
        val a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + (Math.cos(Math.toRadians(latitude.toDouble())) * Math.cos(Math.toRadians(location.latitude().toDouble()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)))
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        var distance = R * c * 1000 // convert to meters
        val height = 0
        distance = distance.pow(2.0) + Math.pow(height.toDouble(), 2.0)
        return sqrt(distance)
    }
}