//package bank.controllers;
//
//import bank.dto.TransactionForm;
//import bank.model.User;
//import bank.service.BankService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//@Controller("BankController")
//@RequestMapping("/bank")
//@RequiredArgsConstructor
//public class BankController {
//
//    private final BankService bankService;
//
//    private final String BANK_PAGE = "bank";
//
//    @GetMapping
//    public String showBank(@AuthenticationPrincipal User user, Model model) {
//        model.addAttribute("account", bankService.findBankAccountByUserId(user.getId()));
//        model.addAttribute("transactionFormW", new TransactionForm());
//        model.addAttribute("transactionFormD", new TransactionForm());
//        return BANK_PAGE;
//    }
//
//    @PostMapping("deposit/{accountId}")
//    public String makeDeposit(
//            @PathVariable Long accountId, Model model,
//            @ModelAttribute("transactionFormD") @Validated TransactionForm transactionForm, BindingResult bindingResult
//    ) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("account", bankService.findById(accountId));
//            model.addAttribute("transactionFormW", new TransactionForm());
//            return BANK_PAGE;
//        }
//
//        bankService.makeDeposit(accountId, transactionForm);
//        return "redirect:/bank";
//    }
//
//    @PostMapping("withdrawal/{accountId}")
//    public String makeWithdrawal(
//            @PathVariable Long accountId, Model model,
//            @ModelAttribute("transactionFormW") @Validated TransactionForm transactionForm, BindingResult bindingResult
//    ) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("account", bankService.findById(accountId));
//            model.addAttribute("transactionFormD", new TransactionForm());
//            return BANK_PAGE;
//        }
//
//        bankService.makeWithdrawal(accountId, transactionForm);
//        return "redirect:/bank";
//    }
//
//}
