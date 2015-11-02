package controller;

import dto.UserDTO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import service.UserService;
import utils.CashManagerException;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/")
public class MainController {

    protected static Logger logger = Logger.getLogger("org/controller");

    @Resource(name="userService")
    private UserService userService;

    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public String getHelp(Model model) {
        logger.debug("Received request to show help");
        return "help";
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String getSettings(Model model, Principal principal) {
        logger.debug("Received request to show settings");
        String name = principal.getName();
        model.addAttribute("username", name);
        return "settings";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLogin(Model model) {
        logger.debug("Received request to show login page");
        return "login";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registerUserAccount(@ModelAttribute("user") @Valid UserDTO accountDTO, BindingResult result) {

        if (result.hasErrors()) {
            return "registration";
        }

        try {
            userService.registerNewUser(accountDTO);
            return "registration-success";
        } catch (CashManagerException e) {
            result.reject("username", e.getMessage());
            return "registration";
        }
    }

}