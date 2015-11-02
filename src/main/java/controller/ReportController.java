package controller;

import domain.*;
import dto.ReportSettingsDTO;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import service.CurrencyService;
import service.ReportService;

import javax.annotation.Resource;
import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController {

    protected static Logger logger = Logger.getLogger("org/controller");

    @Resource(name = "reportService")
    private ReportService reportService;

    @Resource(name="currencyService")
    private CurrencyService currencyService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getReport(Model model) {
        logger.debug("Received request to show report");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("username", user.getUsername());
        return "report-list";
    }

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public String getCategoryReportDefault(Model model) {
        logger.debug("Received request to show category report");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        List<Currency> currencies = currencyService.getAllByUser(user);
        model.addAttribute("username", name);
        model.addAttribute("reportSettings", new ReportSettingsDTO());
        model.addAttribute("categoryAmount", new ArrayList<Object>());
        model.addAttribute("totalAmount", 0.00);
        model.addAttribute("currencies", currencies);
        model.addAttribute("transactionTypes", new TransactionType[]{TransactionType.WITHDRAW, TransactionType.DEPOSIT});
        model.addAttribute("reportType", "category");
        return "report";
    }

    @RequestMapping(value = "/category", method = RequestMethod.POST)
    public String getCategoryReport(@ModelAttribute("reportSettings") ReportSettingsDTO reportSettings, Model model) {
        logger.debug("Received request to show category report");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        Currency currency = reportSettings.getCurrency();
        TransactionType transactionType = reportSettings.getTransactionType();
        List<Currency> currencies = currencyService.getAllByUser(user);
        Date startDate = getStartOfDay(reportSettings.getStartDate());
        Date endDate = getEndOfDay(reportSettings.getEndDate());
        model.addAttribute("username", name);
        model.addAttribute("reportSettings", reportSettings);
        model.addAttribute("categoryAmount", reportService.getCategoryAmount(user, currency, transactionType, startDate, endDate));
        model.addAttribute("totalAmount", reportService.getTotalCategoryAmount(user, currency, transactionType, startDate, endDate));
        model.addAttribute("currencies", currencies);
        model.addAttribute("transactionTypes", new TransactionType[]{TransactionType.WITHDRAW, TransactionType.DEPOSIT});
        model.addAttribute("reportType", "category");
        return "report";
    }

    @RequestMapping(value = "/recipient", method = RequestMethod.GET)
    public String getRecipientReportDefault(Model model) {
        logger.debug("Received request to show category report");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        List<Currency> currencies = currencyService.getAllByUser(user);
        model.addAttribute("username", name);
        model.addAttribute("reportSettings", new ReportSettingsDTO());
        model.addAttribute("categoryAmount", new ArrayList<Object>());
        model.addAttribute("totalAmount", 0);
        model.addAttribute("currencies", currencies);
        model.addAttribute("transactionTypes", new TransactionType[]{TransactionType.WITHDRAW, TransactionType.DEPOSIT});
        model.addAttribute("reportType", "recipient");
        return "report";
    }

    @RequestMapping(value = "/recipient", method = RequestMethod.POST)
    public String getRecipientReport(@ModelAttribute("reportSettings") ReportSettingsDTO reportSettings, Model model) {
        logger.debug("Received request to show category report");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        Currency currency = reportSettings.getCurrency();
        TransactionType transactionType = reportSettings.getTransactionType();
        List<Currency> currencies = currencyService.getAllByUser(user);
        Date startDate = getStartOfDay(reportSettings.getStartDate());
        Date endDate = getEndOfDay(reportSettings.getEndDate());
        model.addAttribute("username", name);
        model.addAttribute("reportSettings", reportSettings);
        model.addAttribute("categoryAmount", reportService.getRecipientAmount(user, currency, startDate, endDate));
        model.addAttribute("totalAmount", reportService.getTotalRecipientAmount(user, currency, startDate, endDate));
        model.addAttribute("currencies", currencies);
        model.addAttribute("transactionTypes", new TransactionType[]{TransactionType.WITHDRAW, TransactionType.DEPOSIT});
        model.addAttribute("reportType", "recipient");
        return "report";
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(Currency.class, "currency", new CurrencyPropertyEditor(currencyService));
        binder.registerCustomEditor(java.util.Date.class, "startDate", new DateEditor(new SimpleDateFormat("yyyy-MM-dd")));
        binder.registerCustomEditor(java.util.Date.class, "endDate", new DateEditor(new SimpleDateFormat("yyyy-MM-dd")));
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
                if (text.length()!=0) {
                    Date date = this.dateFormat.parse(text);
                    super.setValue(date);
                } else {
                    super.setValue(null);
                }
            } catch (ParseException e) {
                throw new IllegalArgumentException("Could not parse date: " + e.getMessage(), e);
            }
        }

    }

    private Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (date == null) {
            try {
                date = dateFormat.parse("0001-01-01");
            } catch (ParseException e) {
            }
        }
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (date == null) {
            try {
                date = dateFormat.parse("9999-12-31");
            } catch (ParseException e) {
            }
        }
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
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