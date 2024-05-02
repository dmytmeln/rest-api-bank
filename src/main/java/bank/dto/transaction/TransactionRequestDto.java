package bank.dto.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequestDto {

    @NotNull(message = "Transaction msg can't be null")
    private String msg;

    @NotNull(message = "Transaction type can't be null")
    private String type;

    @NotNull(message = "Amount of money can't be null")
    @Positive(message = "Amount of money have to be bigger than zero")
    private Double moneyAmount;

}