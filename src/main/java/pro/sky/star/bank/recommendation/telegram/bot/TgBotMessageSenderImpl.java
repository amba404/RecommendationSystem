package pro.sky.star.bank.recommendation.telegram.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.star.bank.recommendation.telegram.bot.interfaces.TgBotMessageSender;

/**
 * Сервис отправки сообщения в Telegram Bot
 */
@Service
public class TgBotMessageSenderImpl implements TgBotMessageSender {
    private final Logger logger = LoggerFactory.getLogger(TgBotMessageSenderImpl.class);

    private final TelegramBot bot;

    public TgBotMessageSenderImpl(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean sendMessage(@NotNull Long chatId, @NotNull String text) {
        SendMessage request = new SendMessage(chatId, text)
                .parseMode(ParseMode.HTML)
                .disableNotification(true);

        SendResponse sendResponse = bot.execute(request);
        boolean isOk = sendResponse.isOk();
        if (!isOk) {
            logger.error("Error sending message: {}. Response: {}", request, sendResponse);
        }
        return isOk;
    }
}
