package pro.sky.star.bank.recommendation.telegram.bot.interfaces;

public interface TgBotMessageSender {
    boolean sendMessage(Long chatId, String text);
}
