package bank.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private BankAccount bankAccount;

    private String msg;

    private String type;

    @Builder.Default
    private Double moneyAmount = 0D;

    @Builder.Default
    private LocalDateTime transactionDate = LocalDateTime.now();

}
