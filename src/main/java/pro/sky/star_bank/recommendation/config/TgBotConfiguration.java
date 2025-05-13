package pro.sky.star_bank.recommendation.config;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TgBotConfiguration {

    private final String token = System.getenv("TG_TOKEN");

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(token);
    }

}
