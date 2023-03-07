import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Location
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.SendResponse
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import java.io.FileReader
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Paths


fun main(args: Array<String>) {
    // Create your bot passing the token received from @BotFather
    // Create your bot passing the token received from @BotFather
    val bot = TelegramBot("6140170233:AAE_j09LC-INAf4cnQ2v7EbSNQv44Q0N27A")

// Register for updates

// Register for updates
    bot.setUpdatesListener { updates ->
        updates.forEach { update ->
            update.message()?.location()?.let { println(it) } ?: run {
                val chatId: Long = update.message().chat().id()
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
            val name: String = record.get("Наименование остановки")
            val adress: String = record.get("Расположение")
            val routes: String = record.get("Маршруты")
            val location: BusStopLocation = record.get("Координаты").let {
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
)

data class BusStopLocation(val latitude: Float, val longitude: Float)