package pro.sky.star_bank.recommendation.service;

public interface TgBotMessageSender {
    boolean sendMessage(Long chatId, String text);
}
