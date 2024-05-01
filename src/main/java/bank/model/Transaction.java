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

    private String msg;

    private String type;

    private Double moneyAmount;

    @Builder.Default
    private LocalDateTime transactionDate = LocalDateTime.now();

}
