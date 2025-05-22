package pro.sky.star_bank.recommendation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pro.sky.star_bank.recommendation.model.RuleSet;
import pro.sky.star_bank.recommendation.model.RuleStat;
import pro.sky.star_bank.recommendation.service.RuleService;
import pro.sky.star_bank.recommendation.service.RuleStatService;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер сервиса для работы с динамическими правилами рекомендаций
 * <ul>
 *     <li>Создание правила</li>
 *     <li>Удаление правила</li>
 *     <li>Получение статистики срабатывания по правилам</li>
 * </ul>
 */
@RestController
@RequestMapping("/rule")
@RequiredArgsConstructor
@Tag(name = "Rule Controller", description = "Контроллер сервиса для работы с динамическими правилами рекомендаций")
public class RuleController {

    private final RuleService ruleService;
    private final RuleStatService ruleStatService;

    @PostMapping
    @Operation(summary = "Создает набор правил", description = "Создает новый динамический набор правил рекомендаций. Создает/обновляет информацию по рекомендуемому продукту")
    public RuleSet createRule(@RequestBody RuleSet ruleSet) {
        return ruleService.addRuleSet(ruleSet);
    }

    @GetMapping
    @Operation(summary = "Список всех правил", description = "Получает список всех динамических наборов правил рекомендаций")
    public List<RuleSet> getAllRuleSets() {
        return ruleService.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Удаляет набор правил", description = "Удаляет динамический набор правил рекомендаций по id")
    public void deleteRuleSet(@PathVariable("id") UUID id) {
        ruleService.deleteRuleSet(id);
    }

    @GetMapping("/stats")
    @Operation(summary = "Возвращает статистику по правилам", description = "Возвращает статистику по срабатыванию правил")
    public RuleStatReturn getStats() {
        return new RuleStatReturn(ruleStatService.findAll());
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class RuleStatReturn {
        private List<RuleStat> stats;
    }
}
