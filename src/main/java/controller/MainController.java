package controller;

import domain.ApplicationErrorType;
import domain.User;
import dto.UserDTO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import service.UserService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/")
public class MainController {

    protected static Logger logger = Logger.getLogger("org/controller");

    @Resource(name="userService")
    private UserService userService;

    @RequestMapping(value = "/homepage", method = RequestMethod.GET)
    public String getHomepage(Model model, Principal principal) {
        logger.debug("Received request to show homepage");
        String name = principal.getName();
        model.addAttribute("username", name);
        return "homepage";
    }

    @RequestMapping(value = "/errorpage", method = RequestMethod.GET)
    public String getErrorpage(@RequestParam("type") ApplicationErrorType applicationErrorType, Model model) {
        logger.debug("Received request to show errorpage");
        model.addAttribute("errorDescription", applicationErrorType.getName());
        return "errorpage";
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
        User registered = new User();
        if (!result.hasErrors()) {
            registered = createUserAccount(accountDTO);
        }
        if (registered == null) {
            result.reject("username", ApplicationErrorType.USER_EXISTS.getName());
        }
        if (result.hasErrors()) {
            return "registration";
        }
        else {
            return "registration-success";
        }
    }
    private User createUserAccount(UserDTO accountDTO) {
        User registered = null;
        try {
            registered = userService.registerNewUserAccount(accountDTO);
        } catch (Exception e) {
            return null;
        }
        return registered;
    }

}