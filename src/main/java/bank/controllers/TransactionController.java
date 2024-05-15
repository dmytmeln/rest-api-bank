package bank.controllers;

import bank.model.User;
import bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank/{bankAccountId}/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') OR @bankServiceImpl.findById(#bankAccountId, #user.id)")
    public ResponseEntity<?> getBankAccountTransactions(@AuthenticationPrincipal User user, @PathVariable Long bankAccountId) {
        return ResponseEntity.ok().body(transactionService.getBankAccountTransactions(bankAccountId));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN') OR @bankServiceImpl.findById(#bankAccountId, #user.id)")
    public ResponseEntity<?> clearBankAccountTransactions(@AuthenticationPrincipal User user, @PathVariable Long bankAccountId) {

        transactionService.clearBankAccountTransactions(bankAccountId);
        return ResponseEntity.noContent().build();
    }

}