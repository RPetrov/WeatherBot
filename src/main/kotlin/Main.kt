import com.github.prominence.openweathermap.api.OpenWeatherMapClient
import com.github.prominence.openweathermap.api.enums.Language
import com.github.prominence.openweathermap.api.enums.UnitSystem
import com.github.prominence.openweathermap.api.model.Coordinate
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.TelegramException
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage


fun main(args: Array<String>) {
    val token = args.getOrNull(0) ?: run {
        println("токен!")
        return
    }
    val wetherToken = args.getOrNull(1) ?: run {
        println("токен!")
        return
    }
// Create your bot passing the token received from @BotFather
    // Create your bot passing the token received from @BotFather
    val bot = TelegramBot(token)
    val openWeatherClient = OpenWeatherMapClient(wetherToken)
// Register for updates
    bot.setUpdatesListener(
        { updates: List<Update?>? ->
            updates?.forEach { message ->
                val chatId: Long? = message?.message()?.chat()?.id()
                val location = message?.message()?.location()
                val locText = message?.message()?.text()
                if (location == null && locText != null && locText.isNotBlank()) {
                    println(locText)
                    try {
                        val currentWether = getCurrentWether(
                            openWeatherClient,
                            locText
                        )
                        println(bot.execute(SendMessage(chatId, currentWether)))
                        val forCust = getTomorrowWeather(
                            openWeatherClient,
                            locText
                        )
                        println(bot.execute(SendMessage(chatId, forCust)))

                    }catch (e: Exception){
                        e.printStackTrace()
                    }



                }
                else if (location != null) {
                    println(location)
                    val currentWether = getCurrentWether(
                        openWeatherClient,
                        location.latitude().toDouble(),
                        location.longitude().toDouble()
                    )
                    println(bot.execute(SendMessage(chatId, currentWether)))
                    val forCust = getTomorrowWeather(
                        openWeatherClient,
                        location.latitude().toDouble(),
                        location.longitude().toDouble()
                    )
                    println(bot.execute(SendMessage(chatId, forCust)))
                } else {
                    println(bot.execute(SendMessage(chatId, "Пришлите, пожалуйста, местоположение")))
                }
            }
            UpdatesListener.CONFIRMED_UPDATES_ALL
        }
    ) { e: TelegramException ->
        if (e.response() != null) {
            // got bad response from telegram
            e.response().errorCode()
            e.response().description()
        } else {
            // probably network error
            e.printStackTrace()
        }
    }
}

fun getCurrentWether(openWeatherClient: OpenWeatherMapClient, lat: Double, lon: Double): String {
    val weather = openWeatherClient
        .currentWeather()
        .single()
        .byCoordinate(Coordinate.of(lat, lon))
        .language(Language.RUSSIAN)
        .unitSystem(UnitSystem.METRIC)
        .retrieve()
        .asJava().toString()
    return weather
}

fun getTomorrowWeather(openWeatherClient: OpenWeatherMapClient, lat: Double, lon: Double): String {
    val weather = openWeatherClient
        .forecast5Day3HourStep()
        .byCoordinate(Coordinate.of(lat, lon))
        .language(Language.RUSSIAN)
        .unitSystem(UnitSystem.METRIC)
        .retrieve()
        .asJava().weatherForecasts.subList(0, 8).map { it.toString() }.joinToString("\n")
    return weather
}

fun getCurrentWether(openWeatherClient: OpenWeatherMapClient, nameCity: String): String {
    val weather = openWeatherClient
        .currentWeather()
        .single()
        .byCityName(nameCity)
        .language(Language.RUSSIAN)
        .unitSystem(UnitSystem.METRIC)
        .retrieve()
        .asJava().toString()
    return weather
}

fun getTomorrowWeather(openWeatherClient: OpenWeatherMapClient, nameCity: String): String {
    val weather = openWeatherClient
        .forecast5Day3HourStep()
        .byCityName(nameCity)
        .language(Language.RUSSIAN)
        .unitSystem(UnitSystem.METRIC)
        .retrieve()
        .asJava().weatherForecasts.subList(0, 8).map { it.toString() }.joinToString("\n")
    return weather
}