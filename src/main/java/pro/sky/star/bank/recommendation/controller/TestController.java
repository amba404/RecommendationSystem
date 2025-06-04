package pro.sky.star.bank.recommendation.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.star.bank.recommendation.service.implementations.TestServiceImpl;
import pro.sky.star.bank.recommendation.service.interfaces.TestService;

import java.util.UUID;

/**
 * Контроллер для тестирования правильности подключения к БД транзакций клиентов
 */
@RestController
@RequestMapping("/test")
@Hidden
public class TestController {

    private final TestService service;

    public TestController(TestServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index() {
        return "Recommendation Service";
    }

    @GetMapping("/amount/{userId}")
    public Integer getAmount(@PathVariable UUID userId) {
        return service.getAmount(userId);
    }
}
