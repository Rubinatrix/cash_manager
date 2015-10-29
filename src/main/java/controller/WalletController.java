package controller;

import domain.Currency;
import domain.User;
import domain.Wallet;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import service.CurrencyService;
import service.WalletService;

import javax.annotation.Resource;
import java.beans.PropertyEditorSupport;
import java.util.List;

@Controller
@RequestMapping("/wallet")
public class WalletController {

    protected static Logger logger = Logger.getLogger("org/controller");

    @Resource(name="walletService")
    private WalletService walletService;

    @Resource(name="currencyService")
    private CurrencyService currencyService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getWalletsWithAmount(Model model) {
        logger.debug("Received request to show wallets page with amount");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Object[]> walletsWithAmount = walletService.getAllWithAmountByUser(user);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("walletsWithAmount", walletsWithAmount);
        return "wallet-list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String getAdd(Model model) {
        logger.debug("Received request to show add wallets page");

        Wallet wallet = new Wallet();
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Currency> currencies = currencyService.getAllByUser(user);

        model.addAttribute("walletAttribute", wallet);
        model.addAttribute("currencyAttribute", currencies);
        model.addAttribute("type", "add");

        return "wallet";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String postAdd(@ModelAttribute("walletAttribute") Wallet wallet) {
        logger.debug("Received request to add new wallet");

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        walletService.add(wallet, user);

        return "redirect:list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String getDelete(@RequestParam("id") Long walletId) {
        logger.debug("Received request to delete wallet");
        walletService.delete(walletId);
        return "redirect:list";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String getEdit(@RequestParam("id") Long walletId, Model model) {
        logger.debug("Received request to show edit wallet page");

        Wallet existingWallet = walletService.get(walletId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Currency> currencies = currencyService.getAllByUser(user);

        model.addAttribute("walletAttribute", existingWallet);
        model.addAttribute("currencyAttribute", currencies);
        model.addAttribute("type", "edit");

        return "wallet";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String postEdit(@RequestParam("id") Long walletId, @ModelAttribute("walletAttribute") Wallet wallet) {
        logger.debug("Received request to add new wallet");

        wallet.setId(walletId);
        walletService.edit(wallet);

        return "redirect:list";
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(Currency.class, "currency", new CurrencyPropertyEditor(currencyService));
    }

    private class CurrencyPropertyEditor extends PropertyEditorSupport {

        public CurrencyPropertyEditor(CurrencyService dbCurrencyManager) {
            this.dbCurrencyManager = dbCurrencyManager;
        }

        private CurrencyService dbCurrencyManager;

        public String getAsText() {
            Currency obj = (Currency) getValue();
            if (null==obj) {
                return "";
            } else {
                return obj.getId().toString();
            }
        }

        public void setAsText(final String value) {
            try {
                Long id = Long.parseLong(value);
                Currency currency = dbCurrencyManager.get(id);
                if (null!=currency) {
                    super.setValue(currency);
                } else {
                    throw new IllegalArgumentException("Binding error. Cannot find currency with id  ["+value+"]");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Binding error. Invalid id: " + value);
            }
        }
    }

}