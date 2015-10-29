package controller;

import domain.Currency;
import domain.User;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import service.CurrencyService;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/currency")
public class CurrencyController {

    protected static Logger logger = Logger.getLogger("org/controller");

    @Resource(name="currencyService")
    private CurrencyService currencyService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getCurrencies(Model model) {
        logger.debug("Received request to show currency list");
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Currency> currencies = currencyService.getAllByUser(user);
        model.addAttribute("currencies", currencies);
        model.addAttribute("username", user.getUsername());
        return "currency-list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String getAdd(Model model) {
        logger.debug("Received request to show add currency page");
        Currency currency = new Currency();
        model.addAttribute("currencyAttribute", currency);
        model.addAttribute("type", "add");
        return "currency";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String postAdd(@ModelAttribute("currencyAttribute") Currency currency) {
        logger.debug("Received request to add new currency");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        currencyService.add(currency, user);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String getDelete(@RequestParam("id") Long currencyId) {
        logger.debug("Received request to delete currency");

        if (currencyService.delete(currencyId)) {
            return "redirect:list";
        } else {
            return "redirect:/app/errorpage?type=CURRENCY_DELETE";
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String getEdit(@RequestParam("id") Long currencyId, Model model) {
        logger.debug("Received request to show edit currency page");
        Currency existingCurrency = currencyService.get(currencyId);
        model.addAttribute("currencyAttribute", existingCurrency);
        model.addAttribute("type", "edit");
        return "currency";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String postEdit(@RequestParam("id") Long currencyId, @ModelAttribute("currencyAttribute") Currency currency) {
        logger.debug("Received request to add new currency");
        currency.setId(currencyId);
        currencyService.edit(currency);
        return "redirect:list";
    }

}