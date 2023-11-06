import com.github.prominence.openweathermap.api.OpenWeatherMapClient
import com.pengrad.telegrambot.TelegramBot


fun main(args: Array<String>) {
    val token = args.getOrNull(0) ?: run {
        println("токен!")
        return
    }
}