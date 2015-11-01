package controller;

import controller.property_editor.CurrencyPropertyEditor;
import domain.Account;
import domain.Currency;
import domain.User;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import service.CurrencyService;
import service.AccountService;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    protected static Logger logger = Logger.getLogger("org/controller");

    @Resource(name="accountService")
    private AccountService accountService;

    @Resource(name="currencyService")
    private CurrencyService currencyService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getAccountsWithAmount(Model model) {
        logger.debug("Received request to show accounts page with amount");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Object[]> accountsWithAmount = accountService.getAllWithAmountByUser(user);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("accountsWithAmount",accountsWithAmount);
        return "account-list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String getAdd(Model model) {
        logger.debug("Received request to show add accounts page");

        Account account = new Account();
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Currency> currencies = currencyService.getAllByUser(user);

        model.addAttribute("accountAttribute", account);
        model.addAttribute("currencyAttribute", currencies);
        model.addAttribute("type", "add");

        return "account";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String postAdd(@ModelAttribute("accountAttribute") Account account) {
        logger.debug("Received request to add new account");

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        accountService.add(account, user);

        return "redirect:list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String getDelete(@RequestParam("id") Long accountId) {
        logger.debug("Received request to delete account");
        accountService.delete(accountId);
        return "redirect:list";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String getEdit(@RequestParam("id") Long accountId, Model model) {
        logger.debug("Received request to show edit account page");

        Account existingAccount = accountService.get(accountId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Currency> currencies = currencyService.getAllByUser(user);

        model.addAttribute("accountAttribute", existingAccount);
        model.addAttribute("currencyAttribute", currencies);
        model.addAttribute("type", "edit");

        return "account";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String postEdit(@RequestParam("id") Long accountId, @ModelAttribute("accountAttribute") Account account) {
        logger.debug("Received request to add new account");

        account.setId(accountId);
        accountService.edit(account);

        return "redirect:list";
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(Currency.class, "currency", new CurrencyPropertyEditor(currencyService));
    }

}