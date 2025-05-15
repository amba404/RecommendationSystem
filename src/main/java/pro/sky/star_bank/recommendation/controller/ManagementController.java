package pro.sky.star_bank.recommendation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.star_bank.recommendation.service.ManagementService;

@RestController
@RequestMapping(value = "/management")
@RequiredArgsConstructor
public class ManagementController {

    private final ManagementService managementService;

    @PostMapping("/clear-caches")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void clearCache() {
        managementService.clearCaches();
    }
}
