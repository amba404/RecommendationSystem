package pro.sky.star_bank.recommendation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.sky.star_bank.recommendation.model.TransactionsUser;
import pro.sky.star_bank.recommendation.repository.TransactionsRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest()
@ExtendWith(MockitoExtension.class)
public class TgBotServiceImplTest {

    @Autowired
    private TgBotServiceImpl tgBotService;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Test
    public void testHello() {
        String result = tgBotService.hello();
        assertEquals("Привет! Справка по использованию бота: /recommend username", result);
    }

    @Test
    public void testRecommendation() {
        String username = "quintin.deckow";

        TransactionsUser user = transactionsRepository.getUserByUserName(username).orElseThrow();
        String result = tgBotService.recommendation(username);

        assertTrue(result.contains("Здравствуйте"));
        assertTrue(result.contains("Новые продукты"));

    }

    @Test
    public void testRecommendationUserNotFound() {
        String result = tgBotService.recommendation("userNameNotExists");

        assertEquals("Пользователь не найден", result);
    }
}

