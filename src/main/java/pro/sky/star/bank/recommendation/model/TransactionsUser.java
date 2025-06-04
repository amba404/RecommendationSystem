package pro.sky.star.bank.recommendation.model;

import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

/**
 * Класс отображения данных пользователя банковских продуктов
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TransactionsUser {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private String firstName;

    private String lastName;

    private String userName;

}
