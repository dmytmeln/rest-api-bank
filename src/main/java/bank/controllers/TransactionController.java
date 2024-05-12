package bank.controllers;

import bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank/{bankAccountId}/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<?> getBankAccountTransactions(@PathVariable Long bankAccountId) {
        return ResponseEntity.ok().body(transactionService.getBankAccountTransactions(bankAccountId));
    }

    @DeleteMapping
    public ResponseEntity<?> clearBankAccountTransactions(@PathVariable Long bankAccountId) {

        transactionService.clearBankAccountTransactions(bankAccountId);
        return ResponseEntity.noContent().build();
    }

}