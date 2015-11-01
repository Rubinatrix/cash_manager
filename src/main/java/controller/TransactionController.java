package controller;

import controller.property_editor.AccountPropertyEditor;
import controller.property_editor.CategoryPropertyEditor;
import controller.property_editor.DatePropertyEditor;
import domain.*;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import service.AccountService;
import service.CategoryService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import service.TransactionService;
import utils.CashManagerException;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@Scope("session")
@RequestMapping("/transaction")
public class TransactionController {

    private String transactionListUrl = "list/all";
    private Long currentAccountId = Long.valueOf(0);

    protected static Logger logger = Logger.getLogger("org/controller");

    @Resource(name = "transactionService")
    private TransactionService transactionService;

    @Resource(name = "accountService")
    private AccountService accountService;

    @Resource(name = "categoryService")
    private CategoryService categoryService;

    @RequestMapping(value = "/list/all", method = RequestMethod.GET)
    public String getTransactions(Model model) {
        logger.debug("Received request to show transactions list");

        transactionListUrl = "list/all";
        currentAccountId = Long.valueOf(0);

        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Transaction> transactions = transactionService.getAllByUser(user);

        model.addAttribute("transactions", transactions);
        model.addAttribute("username", user.getUsername());

        return "transaction-list-all";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getAccountTransactions(@RequestParam("id") Long accountId, Model model) {
        logger.debug("Received request to show transactions list for one account");

        transactionListUrl = "list?id=" + accountId;
        currentAccountId = accountId;

        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountService.get(accountId);
        List<Transaction> transactions = transactionService.getAllByAccount(account);
        Integer amount = accountService.getAmountForAccount(account);

        model.addAttribute("transactions", transactions);
        model.addAttribute("account", account);
        model.addAttribute("amount", amount);
        model.addAttribute("username", user.getUsername());

        return "transaction-list-account";
    }

    @RequestMapping(value = "/add/regular", method = RequestMethod.GET)
    public String getRegularAdd(Model model) {
        logger.debug("Received request to show add transaction page");

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Transaction transaction = new Transaction();
        if (currentAccountId > 0) {
            transaction.setAccount(accountService.get(currentAccountId));
        }
        List<Account> accounts = accountService.getAllByUser(user);
        List<Category> categories = categoryService.getAllByUser(user);

        model.addAttribute("transactionAttribute", transaction);
        model.addAttribute("transactionTypeAttribute", new TransactionType[]{TransactionType.WITHDRAW, TransactionType.DEPOSIT});
        model.addAttribute("accountAttribute", accounts);
        model.addAttribute("categoryAttribute", categories);
        model.addAttribute("type", "add");

        return "transaction-regular";
    }

    @RequestMapping(value = "/add/transfer", method = RequestMethod.GET)
    public String getTransferAdd(Model model) {
        logger.debug("Received request to show add transaction page");

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Transaction transaction = new Transaction();
        if (currentAccountId > 0) {
            transaction.setAccount(accountService.get(currentAccountId));
        }
        List<Account> accounts = accountService.getAllByUser(user);

        model.addAttribute("transactionAttribute", transaction);
        model.addAttribute("accountAttribute", accounts);
        model.addAttribute("type", "add");

        return "transaction-transfer";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String postAdd(@ModelAttribute("transactionAttribute") Transaction transaction, BindingResult result, Model model) {
        logger.debug("Received request to add new transaction");

        try {
            transactionService.add(transaction);
            return "redirect:" + transactionListUrl;
        } catch (CashManagerException e){
            result.reject("transaction", e.getMessage());
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Account> accounts = accountService.getAllByUser(user);
            model.addAttribute("accountAttribute", accounts);
            model.addAttribute("type", "add");
            return "transaction-transfer";
        }

    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String getDelete(@RequestParam("id") Long transactionId) {
        logger.debug("Received request to delete transaction");
        transactionService.delete(transactionId);
        return "redirect:" + transactionListUrl;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String getRegularEdit(@RequestParam("id") Long transactionId, Model model) {
        logger.debug("Received request to show edit transaction page");

        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Transaction existingTransaction = transactionService.get(transactionId);
        List<Account> accounts = accountService.getAllByUser(user);
        List<Category> categories = categoryService.getAllByUser(user);

        model.addAttribute("transactionAttribute", existingTransaction);
        model.addAttribute("transactionTypeAttribute", new TransactionType[]{TransactionType.WITHDRAW, TransactionType.DEPOSIT});
        model.addAttribute("accountAttribute", accounts);
        model.addAttribute("categoryAttribute", categories);
        model.addAttribute("type", "edit");

        if (existingTransaction.getType() == TransactionType.TRANSFER) {
            return "transaction-transfer";
        } else {
            return "transaction-regular";
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String postEdit(@RequestParam("id") Long transactionId, @ModelAttribute("transactionAttribute") Transaction transaction, BindingResult result, Model model) {
        logger.debug("Received request to add new transaction");

        transaction.setId(transactionId);

        try {
            transactionService.edit(transaction);
            return "redirect:" + transactionListUrl;
        } catch (CashManagerException e){
            result.reject("transaction", e.getMessage());
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Account> accounts = accountService.getAllByUser(user);
            model.addAttribute("accountAttribute", accounts);
            model.addAttribute("type", "edit");
            return "transaction-transfer";
        }
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(Account.class, "accountTo", new AccountPropertyEditor(accountService));
        binder.registerCustomEditor(Account.class, "account", new AccountPropertyEditor(accountService));
        binder.registerCustomEditor(Category.class, "category", new CategoryPropertyEditor(categoryService));
        binder.registerCustomEditor(java.util.Date.class, "date", new DatePropertyEditor(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm")));
    }

}