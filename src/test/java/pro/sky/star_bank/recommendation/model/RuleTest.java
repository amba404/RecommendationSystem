package pro.sky.star_bank.recommendation.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import pro.sky.star_bank.recommendation.model.enums.EnumCompareType;


class RuleTest {

    @Test
    public void testRuleJson()  throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = """
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
        """;
        Rule rule = mapper.readValue(json, Rule.class);

        Assertions.assertEquals(EnumCompareType.GT, rule.getCompareType());

        String result = mapper.writeValueAsString(rule);

        JSONAssert.assertEquals(json, result, JSONCompareMode.STRICT);

    }
}