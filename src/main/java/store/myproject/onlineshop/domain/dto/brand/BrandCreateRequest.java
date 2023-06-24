package store.myproject.onlineshop.domain.dto.brand;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import store.myproject.onlineshop.domain.entity.Brand;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandCreateRequest {

    @NotBlank(message = "브랜드명은 공백일수 없습니다.")
    private String name;

    private String originImagePath;

    public Brand toEntity() {
        return Brand.builder()
                .name(this.name)
                .originImagePath(this.originImagePath)
                .build();
    }

    public void setOriginImagePath(String originImageUrl, String resizedImageUrl) {
        this.originImagePath = originImageUrl;
    }
}