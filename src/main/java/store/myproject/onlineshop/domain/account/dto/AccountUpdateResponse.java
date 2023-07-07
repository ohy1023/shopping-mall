package store.myproject.onlineshop.domain.account.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountUpdateResponse {

    private String bankName;

    private String accountNumber;

    private String depositor;
}