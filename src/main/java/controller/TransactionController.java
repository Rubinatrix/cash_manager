package controller;

import domain.*;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import service.CategoryService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import service.TransactionService;
import service.WalletService;

import javax.annotation.Resource;
import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@Scope("session")
@RequestMapping("/transaction")
public class TransactionController {

    private String transactionListUrl = "list/all";
    private Long currentWalletId = Long.valueOf(0);

    protected static Logger logger = Logger.getLogger("org/controller");

    @Resource(name = "transactionService")
    private TransactionService transactionService;

    @Resource(name = "walletService")
    private WalletService walletService;

    @Resource(name = "categoryService")
    private CategoryService categoryService;

    @RequestMapping(value = "/list/all", method = RequestMethod.GET)
    public String getTransactions(Model model) {
        logger.debug("Received request to show transactions list");

        transactionListUrl = "list/all";
        currentWalletId = Long.valueOf(0);

        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Transaction> transactions = transactionService.getAllByUser(user);

        model.addAttribute("transactions", transactions);
        model.addAttribute("username", user.getUsername());

        return "transaction-list-all";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getWalletTransactions(@RequestParam("id") Long walletId, Model model) {
        logger.debug("Received request to show transactions list for one wallet");

        transactionListUrl = "list?id=" + walletId;
        currentWalletId = walletId;

        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Wallet wallet = walletService.get(walletId);
        List<Transaction> transactions = transactionService.getAllByWallet(wallet);
        Integer amount = walletService.getAmountForWallet(wallet);

        model.addAttribute("transactions", transactions);
        model.addAttribute("wallet", wallet);
        model.addAttribute("amount", amount);
        model.addAttribute("username", user.getUsername());

        return "transaction-list-wallet";
    }

    @RequestMapping(value = "/add/regular", method = RequestMethod.GET)
    public String getRegularAdd(Model model) {
        logger.debug("Received request to show add transaction page");

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Transaction transaction = new Transaction();
        if (currentWalletId > 0) {
            transaction.setWallet(walletService.get(currentWalletId));
        }
        List<Wallet> wallets = walletService.getAllByUser(user);
        List<Category> categories = categoryService.getAllByUser(user);

        model.addAttribute("transactionAttribute", transaction);
        model.addAttribute("transactionTypeAttribute", new TransactionType[]{TransactionType.WITHDRAW, TransactionType.DEPOSIT});
        model.addAttribute("walletAttribute", wallets);
        model.addAttribute("categoryAttribute", categories);
        model.addAttribute("type", "add");

        return "transaction-regular";
    }

    @RequestMapping(value = "/add/transfer", method = RequestMethod.GET)
    public String getTransferAdd(Model model) {
        logger.debug("Received request to show add transaction page");

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Transaction transaction = new Transaction();
        if (currentWalletId > 0) {
            transaction.setWallet(walletService.get(currentWalletId));
        }
        List<Wallet> wallets = walletService.getAllByUser(user);

        model.addAttribute("transactionAttribute", transaction);
        model.addAttribute("transactionTypeAttribute", TransactionType.values());
        model.addAttribute("walletAttribute", wallets);
        model.addAttribute("type", "add");

        return "transaction-transfer";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String postAdd(@ModelAttribute("transactionAttribute") Transaction transaction) {
        logger.debug("Received request to add new transaction");

        if ((transaction.getType() == TransactionType.TRANSFER) && (transaction.getWallet().getCurrency().getId().intValue() != transaction.getWalletTo().getCurrency().getId().intValue())) {
            return "redirect:/app/errorpage?type=TRANSFER_CROSS_CURRENCY";
        } else {
            transactionService.add(transaction);
            return "redirect:" + transactionListUrl;
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
        List<Wallet> wallets = walletService.getAllByUser(user);
        List<Category> categories = categoryService.getAllByUser(user);

        model.addAttribute("transactionAttribute", existingTransaction);
        model.addAttribute("transactionTypeAttribute", new TransactionType[]{TransactionType.WITHDRAW, TransactionType.DEPOSIT});
        model.addAttribute("walletAttribute", wallets);
        model.addAttribute("categoryAttribute", categories);
        model.addAttribute("type", "edit");

        if (existingTransaction.getType() == TransactionType.TRANSFER) {
            return "transaction-transfer";
        } else {
            return "transaction-regular";
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String postEdit(@RequestParam("id") Long transactionId, @ModelAttribute("transactionAttribute") Transaction transaction) {
        logger.debug("Received request to add new transaction");

        transaction.setId(transactionId);

        if ((transaction.getType() == TransactionType.TRANSFER) && (transaction.getWallet().getCurrency().getId().intValue() != transaction.getWalletTo().getCurrency().getId().intValue())) {
            return "redirect:/app/errorpage?type=TRANSFER_CROSS_CURRENCY";
        } else {
            transactionService.edit(transaction);
            return "redirect:" + transactionListUrl;
        }
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(Wallet.class, "walletTo", new WalletPropertyEditor(walletService));
        binder.registerCustomEditor(Wallet.class, "wallet", new WalletPropertyEditor(walletService));
        binder.registerCustomEditor(Category.class, "category", new CategoryPropertyEditor(categoryService));
        binder.registerCustomEditor(java.util.Date.class, "date", new DateEditor(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm")));
    }

    public class DateEditor extends PropertyEditorSupport {
        private final DateFormat dateFormat;

        public DateEditor(DateFormat dateFormat) {
            this.dateFormat = dateFormat;
        }

        public String getAsText() {
            Date value = (Date) getValue();
            return value != null ? this.dateFormat.format(value) : "";
        }

        public void setAsText(String text) throws IllegalArgumentException {
            try {
                Date date = this.dateFormat.parse(text);
                super.setValue(date);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Could not parse date: " + e.getMessage(), e);
            }
        }

    }

    private class WalletPropertyEditor extends PropertyEditorSupport {

        public WalletPropertyEditor(WalletService dbWalletManager) {
            this.dbWalletManager = dbWalletManager;
        }

        private WalletService dbWalletManager;

        public String getAsText() {
            Wallet obj = (Wallet) getValue();
            if (null == obj) {
                return "";
            } else {
                return obj.getId().toString();
            }
        }

        public void setAsText(final String value) {
            try {
                Long id = Long.parseLong(value);
                Wallet wallet = dbWalletManager.get(id);
                super.setValue(wallet);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Binding error. Invalid id: " + value);
            }
        }
    }

    private class CategoryPropertyEditor extends PropertyEditorSupport {

        public CategoryPropertyEditor(CategoryService dbCategoryManager) {
            this.dbCategoryManager = dbCategoryManager;
        }

        private CategoryService dbCategoryManager;

        public String getAsText() {
            Category obj = (Category) getValue();
            if (null == obj) {
                return "";
            } else {
                return obj.getId().toString();
            }
        }

        public void setAsText(final String value) {
            try {
                Long id = Long.parseLong(value);
                Category category = dbCategoryManager.get(id);
                super.setValue(category);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Binding error. Invalid id: " + value);
            }
        }
    }

}