package bank.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table("bank_accounts")
public class BankAccount {

    @Id
    private Long id;

    @Builder.Default
    private Double balance = 0D;

    @EqualsAndHashCode.Exclude
    private List<Transaction> transactions;

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public String toString() {
        return "BankAccount(id=" + this.id + ", balance=" + this.balance + ", transactions=" + this.transactions + ")";
    }
}