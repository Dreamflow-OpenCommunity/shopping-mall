package shoppingmall.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import shoppingmall.domain.*;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "orders", path = "orders")
public interface OrderRepository
    extends PagingAndSortingRepository<Order, Long> {}
