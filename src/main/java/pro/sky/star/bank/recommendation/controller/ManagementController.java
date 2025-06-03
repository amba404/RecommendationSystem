package pro.sky.star.bank.recommendation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pro.sky.star.bank.recommendation.dto.Info;
import pro.sky.star.bank.recommendation.service.interfaces.ManagementService;

/**
 * Контроллер сервиса служебного функционала.
 * <ul>
 * <li>Предоставляет информацию о сервисе</li>
 * <li>Очищает кэши запросов к БД транзакций</li>
 * </ul>
 */
@Tag(name = "Management Controller", description = "Контроллер служебного функционала")
@RestController
@RequestMapping(value = "/management")
@RequiredArgsConstructor
public class ManagementController {

    private final ManagementService managementService;
    private final BuildProperties buildProperties;

    @PostMapping("/clear-caches")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Очистка кэшей", description = "Очищает все кэши запросов к БД транзакций")
    public void clearCache() {
        managementService.clearCaches();
    }

    @GetMapping("/info")
    @Operation(description = "Предоставляет информацию о сервисе", summary = "Информация о сервисе")
    public Info getInfo() {
        return new Info(buildProperties.getName(), buildProperties.getVersion());
    }

}
