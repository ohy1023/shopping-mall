package store.myproject.onlineshop.domain.cartitem;

import jakarta.persistence.*;
import lombok.*;
import store.myproject.onlineshop.domain.BaseEntity;
import store.myproject.onlineshop.domain.cart.Cart;
import store.myproject.onlineshop.domain.cartitem.dto.CartItemResponse;
import store.myproject.onlineshop.domain.item.Item;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    private Long cartItemCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(name = "order_bool")
    private boolean isChecked;

    public void plusItemCnt(Long cnt) {
        this.cartItemCnt += cnt;
    }

    public static CartItem createCartItem(Item findItem, Long itemCnt, Cart cart) {
        return CartItem.builder()
                .item(findItem)
                .isChecked(true)
                .cartItemCnt(itemCnt)
                .cart(cart)
                .build();
    }

    public CartItemResponse toCartItemResponse() {
        return CartItemResponse
                .builder()
                .itemId(this.item.getId())
                .itemName(this.item.getItemName())
                .imagePath(this.item.getItemPhotoUrl())
                .price(this.item.getPrice())
                .stock(this.item.getStock())
                .itemCnt(this.cartItemCnt)
                .build();
    }

    public void setCheck() {
        this.isChecked = !this.isChecked;
    }
}