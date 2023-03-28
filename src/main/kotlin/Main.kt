import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage


fun main(args: Array<String>) {
    // Create your bot passing the token received from @BotFather
    // Create your bot passing the token received from @BotFather
    val bot = TelegramBot(args[0])

// Register for updates

// Register for updates
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

// Send messages

// Send messages
//    val chatId: Long = update.message().chat().id()
//    val response = bot.execute(SendMessage(chatId, "Hello!"))
}
