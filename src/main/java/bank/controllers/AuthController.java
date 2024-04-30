//package bank.controllers;
//
//import bank.dto.UserForm;
//import bank.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/register")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final UserService userService;
//    private final String SIGNUP_PAGE = "auth-signup";
//
//    @GetMapping
//    public String showSignup(Model model) {
//        model.addAttribute("userForm", new UserForm());
//        return SIGNUP_PAGE;
//    }
//
//    @PostMapping
//    public String signupUser(@ModelAttribute @Validated UserForm userForm, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return SIGNUP_PAGE;
//        }
//
//        userService.signup(userForm);
//        return "redirect:/login";
//    }
//
//
//}
