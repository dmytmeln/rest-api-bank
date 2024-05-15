package bank.controllers;

import bank.dto.bank.BankResponseDto;
import bank.dto.transaction.TransactionRequestDto;
import bank.model.User;
import bank.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/banks")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @PostMapping
    public ResponseEntity<?> create(@AuthenticationPrincipal User user) {

        BankResponseDto bankResponseDto = bankService.creteBankAccountForUser(user.getId());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{bankAccountId}")
                .buildAndExpand(bankResponseDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(bankResponseDto);
    }

    @GetMapping("/{bankAccountId}")
    @PreAuthorize("hasRole('ADMIN') OR @bankServiceImpl.findById(#user.id, #bankAccountId) != null")
    public ResponseEntity<?> findById(@AuthenticationPrincipal User user, @PathVariable Long bankAccountId) {
        return ResponseEntity.ok().body(bankService.findBankResponseById(user.getId(), bankAccountId));
    }

    @PostMapping("/{bankAccountId}/deposit")
    @PreAuthorize("hasRole('ADMIN') OR @bankServiceImpl.findById(#user.id, #bankAccountId) != null")
    public ResponseEntity<?> makeDeposit(
            @AuthenticationPrincipal User user,
            @PathVariable Long bankAccountId,
            @RequestBody @Validated TransactionRequestDto transactionRequestDto
    ) {
        return ResponseEntity.ok().body(bankService.makeDeposit(bankAccountId, user.getId(), transactionRequestDto));
    }

    @PostMapping("/{bankAccountId}/withdrawal")
    @PreAuthorize("hasRole('ADMIN') OR @bankServiceImpl.findById(#user.id, #bankAccountId) != null")
    public ResponseEntity<?> makeWithdrawal(
            @AuthenticationPrincipal User user,
            @PathVariable Long bankAccountId,
            @RequestBody @Validated TransactionRequestDto transactionRequestDto
    ) {
        return ResponseEntity.ok().body(bankService.makeWithdrawal(bankAccountId, user.getId(), transactionRequestDto));
    }

    @DeleteMapping("/{bankAccountId}")
    @PreAuthorize("hasRole('ADMIN') OR @bankServiceImpl.findById(#user.id, #bankAccountId) != null")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user, @PathVariable Long bankAccountId) {
        bankService.deleteBankAccount(bankAccountId, user.getId());
        return ResponseEntity.noContent().build();
    }

}
