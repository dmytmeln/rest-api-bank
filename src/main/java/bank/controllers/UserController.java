//package bank.controllers;
//
//import bank.dto.UserForm;
//import bank.mapper.UserMapper;
//import bank.model.User;
//import bank.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//@Controller("UserController")
//@RequestMapping("/user")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//    private final String USER_PAGE = "user-info";
//    private final String USER_UPDATE_PAGE = "user-update";
//    private final UserMapper userMapper;
//    private final PasswordEncoder passwordEncoder;
//
//    @GetMapping
//    public String showUserInfo(@AuthenticationPrincipal User user, Model model) {
//        user = userService.findById(user.getId());
//        model.addAttribute("user", user);
//        return USER_PAGE;
//    }
//
//    @GetMapping("/update")
//    public String showUpdateUser(@AuthenticationPrincipal User user, Model model) {
//        user = userService.findById(user.getId());
//        model.addAttribute("userForm", userMapper.mapToUserForm(user));
//        model.addAttribute("userId", user.getId());
//        return USER_UPDATE_PAGE;
//    }
//
//    @PostMapping("/update")
//    public String updateUser(
//            Model model, @AuthenticationPrincipal User user, @RequestParam("oldPassword") String password,
//            @ModelAttribute("userForm") @Validated UserForm userForm, BindingResult bindingResult
//    ) {
//        if (bindingResult.hasErrors()) {
//            return USER_UPDATE_PAGE;
//        }
//
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            model.addAttribute("incorrectPassword",
//                    "Incorrect password! It doesn't match with the old one!");
//            return USER_UPDATE_PAGE;
//        }
//
//        userService.update(userForm, user.getId());
//        return "redirect:/user";
//    }
//
//}