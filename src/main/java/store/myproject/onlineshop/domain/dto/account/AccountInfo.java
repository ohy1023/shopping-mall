package store.myproject.onlineshop.domain.dto.account;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountInfo {

    private String bankName;

    private String accountNumber;

    private String depositor;
}
