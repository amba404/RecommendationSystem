package pro.sky.star.bank.recommendation.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.star.bank.recommendation.model.RecommendedProduct;

import java.util.UUID;

@Transactional
public interface RecommendedProductRepository extends JpaRepository<RecommendedProduct, UUID> {
}
