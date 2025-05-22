package pro.sky.star_bank.recommendation.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pro.sky.star_bank.recommendation.service.ManagementService;

/**
 * Контроллер сервиса служебного функционала.
 * <ul>
 * <li>Предоставляет информацию о сервисе</li>
 * <li>Очищает кэши запросов к БД транзакций</li>
 * </ul>
 */
@RestController
@RequestMapping(value = "/management")
@RequiredArgsConstructor
public class ManagementController {

    private final ManagementService managementService;
    private final BuildProperties buildProperties;

    @PostMapping("/clear-caches")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void clearCache() {
        managementService.clearCaches();
    }

    @GetMapping("/info")
    public Info getInfo() {
        return new Info(buildProperties.getName(), buildProperties.getVersion());
    }

    @Getter
    @AllArgsConstructor
    public static class Info {
        private String name;
        private String version;
    }
}
