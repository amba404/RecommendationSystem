package pro.sky.star_bank.recommendation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.star_bank.recommendation.model.RecommendedProduct;
import pro.sky.star_bank.recommendation.model.TransactionsUser;
import pro.sky.star_bank.recommendation.repository.TransactionsRepository;

import java.util.List;

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
