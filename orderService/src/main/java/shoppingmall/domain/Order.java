package shoppingmall.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import shoppingmall.OrderServiceApplication;
import shoppingmall.domain.OrderPlaced;

@Entity
@Table(name = "Order_table")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long customerId;
    private Long productId;
    private Long quantity;
    private String orderStatus;
    private String deliveryStatus;

    @PostPersist
    public void onPostPersist() {
        OrderPlaced orderPlaced = new OrderPlaced(this);
        orderPlaced.setCustomerId(this.getCustomerId());
        orderPlaced.publishAfterCommit();
    }

    @PreUpdate
    public void onPreUpdate() {}

    public static OrderRepository repository() {
        OrderRepository orderRepository = OrderServiceApplication.applicationContext.getBean(
            OrderRepository.class
        );
        return orderRepository;
    }


    public void orderCancel() {
        this.setOrderStatus("주문 취소");
        OrderCanceled orderCanceled = new OrderCanceled(this);
        orderCanceled.publishAfterCommit();
    }


    public void orderConfirm() {
        this.setOrderStatus("주문 확정");
        OrderConfirmed orderConfirmed = new OrderConfirmed(this);
        orderConfirmed.publishAfterCommit();
    }

    public static void updateDeliveryInfo(DeliveryStarted deliveryStarted) {

        repository().findById(Long.valueOf(deliveryStarted.getOrderId())).ifPresent(order->{
            order.setDeliveryStatus("배달 시작");
            repository().save(order);
         });

    }

    public static void updateDeliveryInfo(DeliveryCompleted deliveryCompleted) {

        repository().findById(Long.valueOf(deliveryCompleted.getOrderId())).ifPresent(order->{
            order.setDeliveryStatus("배달 완료");
            repository().save(order);
        });

    }

}

