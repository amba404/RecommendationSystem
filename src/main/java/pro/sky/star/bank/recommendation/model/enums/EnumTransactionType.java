package pro.sky.star.bank.recommendation.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumTransactionType {
    DEPOSIT("DEPOSIT"),
    WITHDRAW("WITHDRAW");

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
