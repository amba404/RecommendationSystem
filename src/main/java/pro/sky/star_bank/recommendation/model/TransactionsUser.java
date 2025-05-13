package pro.sky.star_bank.recommendation.model;

import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

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
