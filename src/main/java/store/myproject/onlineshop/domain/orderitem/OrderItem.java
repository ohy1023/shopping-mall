package store.myproject.onlineshop.domain.orderitem;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import store.myproject.onlineshop.domain.BaseEntity;
import store.myproject.onlineshop.domain.item.Item;
import store.myproject.onlineshop.domain.order.Order;

import java.math.BigDecimal;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@Table(name = "order_item")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Where(clause = "deleted_date IS NULL")
@SQLDelete(sql = "UPDATE order_item SET deleted_date = CURRENT_TIMESTAMP WHERE order_item_id = ?")
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item; //주문 상품

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order; //주문

    private BigDecimal orderPrice; //주문 가격

    private Long count; //주문 수량

    public static OrderItem createOrderItem(Item item, BigDecimal orderPrice, Long count) {

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(orderPrice)
                .count(count)
                .build();

        item.removeStock(count);

        return orderItem;

    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void cancel() {
        getItem().addStock(count);
    }

    public BigDecimal getTotalPrice() {
        return getOrderPrice().multiply(new BigDecimal(getCount()));
    }


}
