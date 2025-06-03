package pro.sky.star.bank.recommendation.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumProductType {
    DEBIT("DEBIT"),
    CREDIT("CREDIT"),
    INVEST("INVEST"),
    SAVING("SAVING");

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
