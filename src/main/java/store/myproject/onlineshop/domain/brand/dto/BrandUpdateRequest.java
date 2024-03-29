package store.myproject.onlineshop.domain.brand.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import store.myproject.onlineshop.domain.brand.Brand;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandUpdateRequest {

    @NotEmpty(message = "브랜드 명을 입력하세요.")
    private String name;

    public Brand toEntity() {
        return Brand.builder()
                .name(this.name)
                .build();
    }

}
