package pro.sky.recommendation.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.recommendation.system.service.TestService;
import pro.sky.recommendation.system.service.TestServiceImpl;

import java.util.UUID;

@RestController
public class TestController {

    private final TestService service;

    public TestController(TestServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index() {
        return "Recommendation Service";
    }

    @GetMapping("/recommendation/{userId}")
    public Integer recommendation(@PathVariable UUID userId) {
        return service.test(userId);
    }
}
