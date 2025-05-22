package pro.sky.star_bank.recommendation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import pro.sky.star_bank.recommendation.model.enums.EnumCompareType;
import pro.sky.star_bank.recommendation.model.enums.EnumProductType;
import pro.sky.star_bank.recommendation.model.enums.EnumTransactionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс описания составных частей набора динамических правил для рекомендаций.
 * Правила в наборе проверяются на применимость и объединяются логическим И
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Schema(description = "Сущность правила. Входит в набор правил для рекомендаций.")
public class Rule {

    public static int CNT_USER_OF = 1;
    public static int CNT_ACTIVE_USER_OF = 5;
    @JsonProperty("query")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Тип запроса правила")
    private EnumQueryType queryType;
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
    @Schema(description = "Флаг отрицания правила. Результат инвертируется")
    private boolean negate;

    private void trySetArguments(String @NotNull [] arguments) {
        for (String argument : arguments) {
            try {
                if (productType == null) {
                    this.productType = EnumProductType.valueOf(argument);
                    continue;
                }
            } catch (IllegalArgumentException e) {
//                throw new RuntimeException(e);
            }
            try {
                if (transactionType == null) {
                    this.transactionType = EnumTransactionType.valueOf(argument);
                    continue;
                }
            } catch (IllegalArgumentException e) {
//                throw new RuntimeException(e);
            }
            try {
                if (compareType == null) {
                    this.compareType = EnumCompareType.fromString(argument);
                    continue;
                }
            } catch (Exception e) {
//                throw new RuntimeException(e);
            }
            try {
                if (compareValue == null) {
                    this.compareValue = Integer.valueOf(argument);
                }
            } catch (NumberFormatException e) {
//                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Метод для сериализации в JSON
     *
     * @return List<String>
     */
    @Schema(description ="Список аргументов правила. В зависимости от типа правила, количество аргументов может отличаться. От 1 до 4 аргументов (ТипПродукта|ТипТранзакции|СпособСравнения|Значение), заданных строкой",
            allowableValues = {"DEBIT|CREDIT", "DEPOSIT|WITHDRAW", ">|<|>=|<=|=", "Integer(>=0)"})
    public List<String> getArguments() {
        List<String> arguments = new ArrayList<>();

        if (queryType == null) {
            if (productType != null) {
                arguments.add(productType.toString());
            }
            if (transactionType != null) {
                arguments.add(transactionType.toString());
            }
            if (compareType != null) {
                arguments.add(compareType.toString());
            }
            if (compareValue != null) {
                arguments.add(String.valueOf(compareValue));
            }
            return arguments;
        }

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

    /**
     * Метод для десериализации из JSON
     */
    public void setArguments(String[] arguments) {

        if (queryType == null) {
            trySetArguments(arguments);
            return;
        }

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

    public void assertValid() {
        if (queryType == null) {
            throw new IllegalArgumentException("Query type must be specified");
        }
        if (productType == null) {
            throw new IllegalArgumentException("Product type must be specified");
        }
    }

    /**
     * Типы правил
     */
    public enum EnumQueryType {
        // Пользователь заданного типа банковского продукта
        USER_OF,
        // Активный пользователь заданного типа банковского продукта
        ACTIVE_USER_OF,
        // Сравнение суммы оборота по заданному типу банковских продкутов
        TRANSACTION_SUM_COMPARE,
        // Сравнение суммы пополнения и снятия средств по транзакциям заданного типа банковских продкутов
        TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW
    }

}
