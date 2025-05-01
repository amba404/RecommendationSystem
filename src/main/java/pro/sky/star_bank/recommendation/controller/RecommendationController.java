package pro.sky.star_bank.recommendation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.star_bank.recommendation.model.Recommendation;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    @GetMapping("/{userId}")
    public Optional<Recommendation> getRecommendation(@PathVariable(name = "userId") UUID userId) {
        return Optional.of(new Recommendation(userId, new ArrayList<>()));
    }
}
