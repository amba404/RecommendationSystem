package pro.sky.star_bank.recommendation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import lombok.*;
import pro.sky.star_bank.recommendation.model.enums.EnumCompareType;
import pro.sky.star_bank.recommendation.model.enums.EnumProductType;
import pro.sky.star_bank.recommendation.model.enums.EnumTransactionType;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Rule {

    public enum EnumQueryType {
        USER_OF,
        ACTIVE_USER_OF,
        TRANSACTION_SUM_COMPARE,
        TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW
    }

    public void setArguments(String[] arguments) {

        switch (queryType) {
            case USER_OF:
            case ACTIVE_USER_OF:
                this.productType = EnumProductType.valueOf(arguments[0]);
                break;
            case TRANSACTION_SUM_COMPARE:
                this.productType = EnumProductType.valueOf(arguments[0]);
                this.transactionType = EnumTransactionType.valueOf(arguments[1]);
                this.compareType = EnumCompareType.fromString(arguments[2]);
                this.compareValue = Integer.valueOf(arguments[3]);
                break;
            case TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW:
                this.productType = EnumProductType.valueOf(arguments[0]);
                this.compareType = EnumCompareType.fromString(arguments[1]);
                break;
        }
    }

    public List<String> getArguments() {
        List<String> arguments = new ArrayList<>();
        switch (queryType) {
            case USER_OF:
            case ACTIVE_USER_OF:
                arguments.add(productType.toString());
                break;
            case TRANSACTION_SUM_COMPARE:
                arguments.add(productType.toString());
                arguments.add(transactionType.toString());
                arguments.add(compareType.toString());
                arguments.add(String.valueOf(compareValue));
                break;
            case TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW:
                arguments.add(productType.toString());
                arguments.add(compareType.toString());
                break;
        }

        return arguments;
    }

    @NonNull
    @JsonProperty("query")
    @Enumerated(EnumType.STRING)
    private EnumQueryType queryType;

    @NonNull
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private EnumProductType productType;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private EnumTransactionType transactionType;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private EnumCompareType compareType;

    @JsonIgnore
    @Min(0)
    private Integer compareValue;

    private boolean negate;


}
