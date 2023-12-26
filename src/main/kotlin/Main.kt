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

    // Create your bot passing the token received from @BotFather
    val bot = TelegramBot(token)


// Register for updates
    bot.setUpdatesListener(
        { updates: List<Update?>? ->
            println(updates)
            updates?.forEach {
                val chatId = it?.message()?.chat()?.id()?: return@forEach
                val response = bot.execute(SendMessage(chatId, it.message().text()))
            }

            UpdatesListener.CONFIRMED_UPDATES_ALL },
        { e: TelegramException ->
            if (e.response() != null) {
                // got bad response from telegram
                e.response().errorCode()
                e.response().description()
            } else {
                // probably network error
                e.printStackTrace()
            }
        })


//// Send messages
//        val chatId: Long = update.message().chat().id()
//        val response = bot.execute(SendMessage(chatId, "Hello!"))


}

