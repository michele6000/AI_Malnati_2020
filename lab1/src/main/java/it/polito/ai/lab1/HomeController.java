package it.polito.ai.lab1;

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

    final static String home_page = "home.html";
    final static String reg_page = "register.html";
    final static String private_page = "private.html";

    final static String redirectHome = "redirect:/";
    final static String redirectRegPage = "redirect:/register";
    final static String redirectPrivatePage = "redirect:/private";

    final static String Home = "/";


    @Autowired
    private RegistrationManager manager;

    @GetMapping("/")
    public String home(@ModelAttribute("command") LoginCommand log_cmd) {
        log.info(log_cmd.toString());
        return home_page;
    }

    @GetMapping("/register")
    public String registrationPage(@ModelAttribute("command") RegistrationCommand reg_cmd) {
        log.info(reg_cmd.toString());
        return reg_page;
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("command") RegistrationCommand reg_cmd, BindingResult bind_res) {

        log.info("Sending sign-up form");
        log.info(reg_cmd.toString());
        RegistrationDetails reg_details = RegistrationDetails.builder()
                .mail(reg_cmd.getMail())
                .name(reg_cmd.getName())
                .surname(reg_cmd.getSurname())
                .password(reg_cmd.getPassword())
                .registrationDate(new Date())
                .build();

        if (!reg_cmd.getPassword().contentEquals(reg_cmd.getPassword2()))
            bind_res.addError(new FieldError("command", "password", "Le due password non sono uguali!"));

        if (!reg_cmd.isPrivacy_accepted())
            bind_res.addError(new FieldError("command", "privacy_accepted", "Campo obbligatorio: accettare le norme sulla privacy!"));

        if (manager.putIfAbsent(reg_details.getMail(), reg_details) != null)
            bind_res.addError(new FieldError("command", "mail", "Utente già registrato!"));

        if (bind_res.getErrorCount() == 0)
            return redirectHome;

        manager.remove(reg_cmd.getMail());
        return reg_page;
    }

    @PostMapping("/login")
    public String login(HttpSession session, @Valid @ModelAttribute("command") LoginCommand log_cmd, BindingResult bind_res) {

        if (log_cmd.getMail().length() == 0)
            bind_res.addError(new FieldError("command", "mail", "Campo obbligatorio: inserire un indirizzo e-mail!"));
            //Controllo utente esistente
        else if (!manager.containsKey(log_cmd.getMail()))
            bind_res.addError(new FieldError("command", "mail", "Utente non registrato!"));
        if (log_cmd.getPassword().length() == 0)
            bind_res.addError(new FieldError("command", "password", "Campo obbligatorio: inserire password!"));
        if (bind_res.getErrorCount() != 0)
            return home_page;

        //Controllo password inserita corretta
        if (manager.get(log_cmd.getMail()).getPassword() != null)
            if (!log_cmd.getPassword().contentEquals(manager.get(log_cmd.getMail()).getPassword()))
                bind_res.addError(new FieldError("command", "password", "Password errata!"));


        if (bind_res.getErrorCount() != 0)
            return home_page;

        session.setAttribute("username", log_cmd.getMail());
        return redirectPrivatePage;
    }

    @GetMapping("/private")
    public String privatePage(HttpSession session) {
        if (session.getAttribute("username") == null)
            return redirectHome;

        return private_page;
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("username");
        return redirectHome;
    }

}
