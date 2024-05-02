package bank.dto.transaction;

public record TransactionResponseDto(

    Long id,
    String msg,
    String type,
    Double moneyAmount,
    String createdAt


) {
}
