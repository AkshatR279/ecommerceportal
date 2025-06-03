package in.akshatr.ecommerceportal.repository;

import in.akshatr.ecommerceportal.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemEntityRepository extends JpaRepository<OrderItemEntity, Long> {

}
