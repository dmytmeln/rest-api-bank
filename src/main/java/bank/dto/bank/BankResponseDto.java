package bank.dto.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankResponseDto {
    private Long id;
    private Double balance;
    private List<Long> transactionsId;
}
