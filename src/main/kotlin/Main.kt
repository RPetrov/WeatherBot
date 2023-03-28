import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import org.apache.commons.csv.CSVFormat
import java.io.FileReader


fun main(args: Array<String>) {
    val bot = TelegramBot(args[0])

    bot.setUpdatesListener { updates: List<Update?>? ->
        updates?.forEach { update ->
            val location = update?.message()?.location()
            if (location == null) {
                val chatId: Long? = update?.message()?.chat()?.id()
                val response = bot.execute(SendMessage(chatId, "Send location, pls"))
            } else {
                println(location)
            }
        }
//        println(updates)
        UpdatesListener.CONFIRMED_UPDATES_ALL
    }

    val data = Data("data\\perechen-ostanovochnyh-punktov-s-ukazaniem-vida-transporta-i-s-koordinatami-ih-mestopolozheniya.csv")
}

class Data(fileName: String) {

    val bustStopList = mutableListOf<BusStop>()
    init {
        val reader = FileReader(fileName)

        val csvFormat = CSVFormat.DEFAULT.builder()
            .setHeader("№,Вид транспортного средства,Тип объекта,Наименование остановки,Официальное наименование,Расположение,Маршруты,Координаты")
            .setSkipHeaderRecord(true)
            .build()

        val records = csvFormat.parse(reader)

        for (record in records) {
            val name = record.get(3)
            val address = record.get(5)
            val lattitude = record.get(7).split(",")[0].toFloat()
            val longitude = record.get(7).split(",")[1].toFloat()
            bustStopList.add(BusStop(name, address, lattitude, longitude))
        }
    }
}

data class BusStop(val name: String, val address: String, val lattitude: Float, val longitude: Float) {

}