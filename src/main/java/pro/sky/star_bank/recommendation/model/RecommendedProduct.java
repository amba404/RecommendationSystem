package pro.sky.star_bank.recommendation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "recommended_products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecommendedProduct {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String text;
}
