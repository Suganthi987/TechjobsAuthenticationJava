package org.launchcode.techjobsauth.controllers;


import jakarta.servlet.Registration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.launchcode.techjobsauth.models.DTO.LoginFormDTO;
import org.launchcode.techjobsauth.models.DTO.RegistrationFormDTO;
import org.launchcode.techjobsauth.models.User;
import org.launchcode.techjobsauth.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class AuthenticationController {
    @Autowired
    private UserRepository userRepository;
    public static final String userSessionKey = "user";
    public User getUserFromSession(HttpSession session){
        Integer userId = (Integer)session.getAttribute(userSessionKey);
        if(userId == null){
            return null;
        }
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            return null;
        }
        return user.get();

    }
    private static void setUserInSession(HttpSession session, User user){
        session.setAttribute(userSessionKey,user.getId());
    }

    @GetMapping("/register")
    public String displayRegisterForm(Model model){
        model.addAttribute(new RegistrationFormDTO());
        model.addAttribute("title","Register");
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute @Valid RegistrationFormDTO registrationFormDTO, Errors errors, HttpServletRequest request,Model model){
        if(errors.hasErrors()){
            model.addAttribute("title","register");
            return "register";
        }
        User existingUser = userRepository.findByUsername(registrationFormDTO.getUsername());
        if(existingUser != null) {
            errors.rejectValue("username", "username.alreadyexists", "A user already exists by that name");
            model.addAttribute("title", "register");
            return "register";
        }
        String password = registrationFormDTO.getPassword();
        String verifyPassword = registrationFormDTO.getVerifyPassword();
        if(!password.equals(verifyPassword)){
            errors.rejectValue("Password","password.mismatch","Mismatched password");
            model.addAttribute("title","register");
            return "register";
        }
        User newUser = new User(registrationFormDTO.getUsername(),registrationFormDTO.getPassword());
        userRepository.save(newUser);
        setUserInSession(request.getSession(),newUser);
        return "redirect:";
    }

    @GetMapping("/login")
    public String displayLoginForm(Model model){
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title","login");
        return "login";
    }

    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,Errors errors, HttpServletRequest request,Model model){
        if(errors.hasErrors()){
            model.addAttribute("title","login");
            return "login";
        }

        User theUser = userRepository.findByUsername(loginFormDTO.getUsername());
        String password = loginFormDTO.getPassword();
        if(theUser == null || !theUser.isMatchingPassword(password)) {
            errors.rejectValue("password", "password.invalid", "Invalid username and password");
        }
        setUserInSession(request.getSession(), theUser);
        return "redirect:";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:login";
    }

}


