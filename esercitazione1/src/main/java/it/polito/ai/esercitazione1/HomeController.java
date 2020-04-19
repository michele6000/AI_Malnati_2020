package it.polito.ai.esercitazione1;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;

@Controller
@Log(topic = "HomeController")
public class HomeController {

    String homePage = "home.html";
    String regPage = "register.html";
    String privatePage = "private.html";
    String redirectHome = "redirect:/";
    String redirectRegPage = "register";
    String redirectPrivatePage = "redirect:/private";


    @Autowired
    RegistrationManager manager;

    @GetMapping("/")
    public String home(@ModelAttribute("login") LoginCommand loginCom) {
        return homePage;
    }

    @GetMapping("/register")
    public String registrationPage(@ModelAttribute("command") RegistrationCommand regCom) {
        log.info(regCom.toString());
        return regPage;
    }

    @GetMapping("/private")
    public String privatePage(HttpSession session) {
        String user = (String) session.getAttribute("username");
        if (user != null && user.length() > 0)
            return privatePage;
        else
            return redirectHome;
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("command") RegistrationCommand regCom, BindingResult result) {
        RegistrationDetails regUser = RegistrationDetails.builder()
                .name(regCom.getName())
                .surname(regCom.getSurname())
                .email(regCom.getEmail())
                .password(regCom.getPassword())
                .registrationDate(new Date())
                .build();

        checkText(regCom, result);
        log.info("Submitted module");
        log.info(regCom.toString());

        if (!regCom.getPassword().contentEquals(regCom.getPassword2()))
            result.addError(new FieldError("command", "password2", "Le due password non sono uguali!"));

        if (!regCom.isPrivacy())
            result.addError(new FieldError("command", "privacy", "Devi acconsentire al trattamento dei dati"));

        if (manager.putIfAbsent(regCom.getEmail(), regUser) != null)
            result.addError(new FieldError("command", "email", "Utente esistente!"));

        log.info(result.toString());

        if (result.getErrorCount() == 0)
            return redirectHome;

        manager.remove(regCom.getEmail());
        return redirectRegPage;

    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("login") LoginCommand loginCom, BindingResult result, HttpSession session) {
        String username = loginCom.getUsername();
        String password = loginCom.getPassword();
        String passwordT;

        if (username.length() == 0)
            return homePage;

        if (!manager.containsKey(username)) {
            result.addError(new FieldError("login", "username", "Utente non esistente!"));
            return homePage;
        }

        if (password.length() == 0)
            return homePage;

        passwordT = manager.get(username).getPassword();

        if (!password.equals(passwordT)) {
            result.addError(new FieldError("login", "password", "Password errata!"));
            return homePage;
        }

        session.setAttribute("username", username);
        return redirectPrivatePage;
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("username");
        return redirectHome;
    }

    private void checkText(RegistrationCommand regCom, BindingResult result) {
        if (!regCom.getName().matches("^[a-zA-Z]*$"))
            result.addError(new FieldError("command", "name", "Caratteri validi : A-Z"));
        if (!regCom.getSurname().matches("^[a-zA-Z]*$"))
            result.addError(new FieldError("command", "surname", "Caratteri validi : A-Z"));
        if (regCom.getPassword().contains(" "))
            result.addError(new FieldError("command", "password", "Gli spazi non sono consentiti!"));
    }

}
