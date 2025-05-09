package pro.sky.star_bank.recommendation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pro.sky.star_bank.recommendation.model.RuleSet;
import pro.sky.star_bank.recommendation.service.RuleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
public class RuleController {

    private final RuleService ruleService;

    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    public RuleSet createRule(@RequestBody RuleSet ruleSet) {
        return ruleService.addRuleSet(ruleSet);
    }

    @GetMapping
    public List<RuleSet> getAllRuleSets() {
        return ruleService.getAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteRuleSet(@PathVariable("id") UUID id) {
        ruleService.deleteRuleSet(id);
    }
}
