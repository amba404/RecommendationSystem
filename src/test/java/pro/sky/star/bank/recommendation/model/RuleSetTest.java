package pro.sky.star.bank.recommendation.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import pro.sky.star.bank.recommendation.model.enums.EnumCompareType;

import java.util.UUID;


class RuleSetTest {

    @Test
    public void testRuleSetJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = """
                {
                    "product_name": "Простой кредит",
                    "product_id": "ab138afb-f3ba-4a93-b74f-0fcee86d447f",
                    "product_text": "<текст рекомендации>",
                    "rule": [
                        {
                            "query": "USER_OF",
                            "arguments": [
                                "CREDIT"
                            ],
                            "negate": true
                        },
                        {
                            "query": "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW",
                            "arguments": [
                                "DEBIT",
                                ">"
                            ],
                            "negate": false
                        },
                        {
                            "query": "TRANSACTION_SUM_COMPARE",
                            "arguments": [
                                "DEBIT",
                                "DEPOSIT",
                                ">",
                                "100000"
                            ],
                            "negate": false
                        }
                    ]
                }
                """;
        RuleSet ruleSet = mapper.readValue(json, RuleSet.class);

        Assertions.assertEquals(UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f"), ruleSet.getProduct().getId());
        Assertions.assertEquals(EnumCompareType.GT, ruleSet.getRules().get(2).getCompareType());

        String result = mapper.writeValueAsString(ruleSet);

        JSONAssert.assertEquals(json, result, JSONCompareMode.LENIENT);
    }
}