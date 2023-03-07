
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.request.SendMessage

import com.pengrad.telegrambot.response.SendResponse


fun main(args: Array<String>) {
    // Create your bot passing the token received from @BotFather
    // Create your bot passing the token received from @BotFather
    val bot = TelegramBot("6140170233:AAE_j09LC-INAf4cnQ2v7EbSNQv44Q0N27A")

// Register for updates

// Register for updates
    bot.setUpdatesListener { updates ->
        updates.forEach{update -> update.message()?.location()?.let { println(it) } ?: run {
            val chatId: Long = update.message().chat().id()
            val response: SendResponse = bot.execute(SendMessage(chatId, "Пришлите, пожалуйста, ваше местоположение"))
        }}
        println(updates)
        UpdatesListener.CONFIRMED_UPDATES_ALL }

// Send messages

// Send messages


}
