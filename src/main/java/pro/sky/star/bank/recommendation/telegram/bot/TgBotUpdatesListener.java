package pro.sky.star.bank.recommendation.telegram.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import pro.sky.star.bank.recommendation.telegram.bot.interfaces.TgBotMessageSender;
import pro.sky.star.bank.recommendation.telegram.bot.interfaces.TgBotService;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Контроллер для Telegram Bot, выдает рекомендованные банковские продукты по запросу
 * для указанного с помощью NikName пользователя
 */
@Service
@RequiredArgsConstructor
public class TgBotUpdatesListener implements UpdatesListener {

    public static final String RECOMMEND = "/recommend";
    private final TelegramBot bot;
    private final TgBotMessageSender messageSender;
    private final TgBotService tgBotService;

    @PostConstruct
    public void init() {
        bot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(this::processUpdate);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    void processUpdate(@NotNull Update update) {

        Message message = update.message() == null ? update.editedMessage() : update.message();

        Long chatId = message.chat().id();
        String text = message.text();

        if ("/start".equals(text)) {
            messageSender.sendMessage(chatId,
                    tgBotService.hello());
        } else if (text.startsWith(RECOMMEND)) {
            messageSender.sendMessage(chatId,
                    tgBotService.recommendation(text
                            .substring(RECOMMEND.length())
                            .trim()));
        }
    }
}
