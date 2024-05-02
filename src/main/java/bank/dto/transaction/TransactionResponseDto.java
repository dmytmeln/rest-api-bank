package bank.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDto {
    private Long id;
    private String msg;
    private String type;
    private Double moneyAmount;
    private String transactionDate;
}
