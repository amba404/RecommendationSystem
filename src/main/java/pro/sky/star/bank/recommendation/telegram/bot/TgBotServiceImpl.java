package pro.sky.star.bank.recommendation.telegram.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.star.bank.recommendation.model.RecommendedProduct;
import pro.sky.star.bank.recommendation.model.TransactionsUser;
import pro.sky.star.bank.recommendation.repository.TransactionsRepository;
import pro.sky.star.bank.recommendation.service.interfaces.RecommendationService;
import pro.sky.star.bank.recommendation.telegram.bot.interfaces.TgBotService;

import java.util.List;

/**
 * Сервис обработки запросов к Telegram Bot
 */
@Service
@RequiredArgsConstructor
public class TgBotServiceImpl implements TgBotService {

    private final TransactionsRepository transactionsRepository;
    private final RecommendationService recommendationService;

    @Override
    public String hello() {
        return "Привет! Справка по использованию бота: /recommend username";
    }

    @Override
    public String recommendation(String userName) {
        TransactionsUser user = transactionsRepository.getUserByUserName(userName).orElse(null);
        if (user != null) {
            List<RecommendedProduct> recommendations = recommendationService.getRecommendations(user.getId());
            return String.format("Здравствуйте, %s %s!\n", user.getFirstName(), user.getLastName())
                    + "Новые продукты для вас:\n"
                    + recommendations.toString();
        } else {
            return "Пользователь не найден";
        }
    }
}
