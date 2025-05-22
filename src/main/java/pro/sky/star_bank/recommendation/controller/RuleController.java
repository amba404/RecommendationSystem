package pro.sky.star_bank.recommendation.controller;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pro.sky.star_bank.recommendation.model.RuleSet;
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
public class RuleController {

    private final RuleService ruleService;
    private final RuleStatService ruleStatService;

    @PostMapping
    public RuleSet createRule(@RequestBody RuleSet ruleSet) {
        return ruleService.addRuleSet(ruleSet);
    }

    @GetMapping
    public List<RuleSet> getAllRuleSets() {
        return ruleService.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteRuleSet(@PathVariable("id") UUID id) {
        ruleService.deleteRuleSet(id);
    }

    @GetMapping("/stats")
    public JSONObject getStats() {
        JSONObject stats = new JSONObject();
        stats.put("stats", ruleStatService.findAll());
        return stats;
    }
}
