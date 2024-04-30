//package bank.service.serviceImpl;
//
//import bank.dto.UserForm;
//import bank.mapper.UserMapper;
//import bank.model.BankAccount;
//import bank.model.User;
//import bank.service.UserService;
//import jakarta.persistence.EntityExistsException;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Objects;
//
//@Service
//@AllArgsConstructor
//public class UserServiceImpl implements UserService {
//
//    private final UserRepository userRepo;
//    private final UserMapper userMapper;
//
//    @Override
//    public User findById(Long userId) {
//        return userRepo.findById(userId).orElseThrow(
//                () -> new EntityNotFoundException(
//                        "User with  id [%d] not found".formatted(userId)
//                )
//        );
//    }
//
//    @Override
//    @Transactional
//    public User signup(UserForm userForm) {
//
//        checkIfUserExistsByEmailOrPhoneNumber(userForm);
//
//        User map = userMapper.mapToUser(userForm);
//        User savedUser = userRepo.save(map);
//
//        BankAccount bankAccount = BankAccount.builder()
//                .user(savedUser)
//                .build();
//        savedUser.setBankAccount(bankAccount);
//
//        return savedUser;
//    }
//
//    @Override
//    public void checkIfUserExistsByEmailOrPhoneNumber(UserForm userForm) {
//        // check if a userForm exists with the email and phone number provided during registration (these fields must be unique)
//        String email = userForm.getEmail();
//        String phoneNumber = userForm.getPhoneNumber();
//        if (userRepo.existsByEmailOrPhoneNumber(email, phoneNumber)) {
//            throw new EntityExistsException(
//                    "User with email: [%s] or phone number: [%s] already exists!".formatted(email, phoneNumber)
//            );
//        }
//    }
//
//    @Override
//    @Transactional
//    public User update(UserForm userForm, Long userId) {
//        alreadyExists(userForm, userId); // method throw exception if such user already exists or if he's not found
//        User user = userMapper.mapToUser(userForm, userId);
//        return userRepo.save(user);
//    }
//
//    // method to check whether userForm with such email or/and phone number already exists, because these fields are unique
//    @Override
//    @Transactional
//    public void alreadyExists(UserForm userForm, Long userId) {
//
//        User userDB = findById(userId);
//
//        String email = userForm.getEmail();
//        String phoneNumber = userForm.getPhoneNumber();
//
//        // check whether new email and phone number (if they are new) are equal to old ones
//        boolean sameEmail = Objects.equals(userDB.getEmail(), email);
//        boolean samePhoneNumber = Objects.equals(userDB.getPhoneNumber(), phoneNumber);
//        // if userForm changed both email and phone number -> check whether userForm with such info already exists
//        if (!sameEmail && !samePhoneNumber) {
//
//            if (userRepo.existsByEmailAndPhoneNumber(email, phoneNumber)) {
//                throw new EntityExistsException(
//                        "User with email [%s] and phone number [%s] already exists!".formatted(email, phoneNumber)
//                );
//            }
//
//        } else if (!sameEmail) { // if userForm change only email -> check whether userForm with such email already exists
//
//            if (userRepo.existsByEmail(email)) {
//                throw new EntityExistsException(
//                        "User with email [%s] already exists!".formatted(email)
//                );
//            }
//
//        } else if (!samePhoneNumber) { //if userForm change only phone number -> check whether userForm with such phone number already exists
//
//            if (userRepo.existsByPhoneNumber(phoneNumber)) {
//                throw new EntityExistsException(
//                        "User with phone number [%s] already exists!".formatted(phoneNumber)
//                );
//            }
//
//        }
//    }
//
//    @Override
//    public void delete(long userId) {
//        if (!userRepo.existsById(userId)) {
//            throw new EntityNotFoundException(
//                    "User with  userId [%d] not found".formatted(userId)
//            );
//        }
//
//        userRepo.deleteById(userId);
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepo.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException(
//                        "User with username [%s] not found!".formatted(username)
//                ));
//    }
//}