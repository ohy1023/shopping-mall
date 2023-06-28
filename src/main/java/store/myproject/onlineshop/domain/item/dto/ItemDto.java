package store.myproject.onlineshop.domain.item.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import store.myproject.onlineshop.domain.brand.Brand;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemDto {

    private String itemName;

    private int price;

    private int stock;

    private String itemPhotoUrl;

    private Brand brand;

    @QueryProjection
    public ItemDto(String itemName, int price, int stock, String itemPhotoUrl, Brand brand) {
        this.itemName = itemName;
        this.price = price;
        this.stock = stock;
        this.itemPhotoUrl = itemPhotoUrl;
        this.brand = brand;
    }
}
