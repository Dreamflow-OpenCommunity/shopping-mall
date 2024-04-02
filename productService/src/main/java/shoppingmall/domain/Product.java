package shoppingmall.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import shoppingmall.ProductServiceApplication;
import shoppingmall.domain.StockDecreased;
import shoppingmall.domain.StockIncreased;

@Entity
@Table(name = "Product_table")
@Getter
@Setter
//<<< DDD / Aggregate Root
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String productName;

    private Integer stock;

    private String productInfo;

    @PostPersist
    public void onPostPersist() {
    }

    public static ProductRepository repository() {
        ProductRepository productRepository = ProductServiceApplication.applicationContext.getBean(
            ProductRepository.class
        );
        return productRepository;
    }

    //<<< Clean Arch / Port Method
    public static void orderPlace(OrderPlaced orderPlaced) {
        //implement business logic here:


        repository().findById(Long.valueOf(orderPlaced.getProductId())).ifPresent(product->{
            
            product.setStock(product.getStock() - orderPlaced.getQuantity());
            repository().save(product);

            StockDecreased stockDecreased = new StockDecreased(product);
            stockDecreased.publishAfterCommit();

         });


    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void orderCancel(OrderCanceled orderCanceled) {
        //implement business logic here:

        repository().findById(Long.valueOf(orderCanceled.getProductId())).ifPresent(product->{
            
            product.setStock(product.getStock() + orderCanceled.getQuantity());
            repository().save(product);

            StockIncreased stockIncreased = new StockIncreased(product);
            stockIncreased.publishAfterCommit();

         });

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
