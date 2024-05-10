package bank.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table("bank_accounts")
public class BankAccount {

    @Id
    private Long id;

    private Double balance;

    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        transactions.add(transaction);
    }

    @Override
    public String toString() {
        return "BankAccount(id=" + this.id + ", balance=" + this.balance + ", transactions=" + this.transactions + ")";
    }
}