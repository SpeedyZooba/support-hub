package com.projects.supporthub.system;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projects.supporthub.exception.UserNotFoundException;
import com.projects.supporthub.model.User;
import com.projects.supporthub.security.SecurityService;
import com.projects.supporthub.security.recovery.RecoveryService;
import com.projects.supporthub.security.recovery.RecoveryToken;
import com.projects.supporthub.security.recovery.TokenService;
import com.projects.supporthub.service.UserService;

@Controller
public class RecoveryController 
{
    private TokenService tokens;
    private SecurityService verifier;
    private UserService users;
    private RecoveryService helper;

    private static final BCryptPasswordEncoder encryptor = new BCryptPasswordEncoder();
    private static final Logger log = LoggerFactory.getLogger(RecoveryController.class);

    public RecoveryController(TokenService tokens, SecurityService verifier, UserService users, RecoveryService helper)
    {
        this.tokens = tokens;
        this.verifier = verifier;
        this.users = users;
        this.helper = helper;
    }

    @GetMapping("/forgotpassword")
    public String initForgotPasswordForm(Model model)
    {
        log.info("initForgotPasswordForm has begun execution.");
        String email = "";
        model.addAttribute("email", email);
        log.info("initForgotPasswordForm is about to finish execution.");
        return "forgotpassword";
    }

    @PostMapping("/forgotpassword")
    public String processForgotPasswordForm(@RequestParam(name = "email", required = true) String email, RedirectAttributes redirect)
    {
        try {
            User user = users.getUserByUsername(email);
            RecoveryToken token = tokens.createRecoveryToken(user.getUserId());
            helper.sendAccountRecoveryMail(email, "localhost:8080/resetpassword?req=" + token.getTokenString());
            redirect.addFlashAttribute("success", "An email containing the link for resetting your password has been sent to your email account.");
            return "redirect:/login";
        }
        catch (UserNotFoundException exception) {
            log.error("Unable to find an email for recovery.");
            redirect.addFlashAttribute("notFound", "A user with this email does not exist.");
            return "redirect:/forgotpassword";
        }
    }

    @GetMapping("/resetpassword")
    public String initResetPasswordForm(@RequestParam(name = "req", required = true) String tokenString, Model model, RedirectAttributes redirect)
    {
        Optional<RecoveryToken> token = tokens.getTokenFromString(tokenString);
        if (!token.isPresent() || token.get().isExpired() || token.get().isUsed())
        {
            redirect.addFlashAttribute("failure", "Invalid link for recovery.");
            return "redirect:/login";        
        }
        String newPassword = "";
        String userId = token.get().getUserId();
        model.addAttribute("req", tokenString);
        model.addAttribute("newPassword", newPassword);
        model.addAttribute("userId", userId);
        return "resetpassword";
    }

    @PostMapping("/resetpassword")
    public String processResetPasswordForm(@RequestParam(name = "req", required = true) String tokenString, @RequestParam(name = "newPassword", required = true) String password, 
                                                @RequestParam(name = "userId", required = true) String userId, RedirectAttributes redirect)
    {
        User user = users.getUserById(userId);
        RecoveryToken token = tokens.getTokenFromString(tokenString).get();
        if (encryptor.matches(password, user.getPassword()))
        {
            redirect.addFlashAttribute("matches", "Your new password cannot be the same as the old one.");
            return "redirect:/resetpassword?req=" + tokenString;
        }
        else if (!verifier.isValidPassword(password))
        {
            redirect.addFlashAttribute("invalid", "An error has occurred while processing your password.");
            return "redirect:/resetpassword?req=" + tokenString;
        }
        else
        {
            log.info("Conditions met for password change.");
            user.setPassword(encryptor.encode(password));
            token.setIsUsed(true);
            users.newUser(user);
            tokens.saveToken(token);
            verifier.forceLogout(user.getEmail());
            redirect.addFlashAttribute("changeSuccess", "Your password has been successfully updated.");
            return "redirect:/login";
        }
    }
}