package service;

import domain.*;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.CashManagerErrorType;
import utils.CashManagerException;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("transactionService")
@Transactional
public class TransactionService {

    protected static Logger logger = Logger.getLogger("org/service");

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    public List<Transaction> getAll() {
        logger.debug("Retrieving all transactions");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT t FROM Transaction as t ORDER BY t.date DESC, t.id DESC");
        return query.list();
    }

    public List<Transaction> getAllByUser(User user) {
        logger.debug("Retrieving all transactions");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT t FROM Transaction as t WHERE t.account.user = :user ORDER BY t.date DESC, t.id DESC");
        query.setParameter("user", user);
        return query.list();
    }

    public List<Transaction> getAllByAccount(Account account) {
        logger.debug("Retrieving transactions by specified account");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT t FROM Transaction as t WHERE t.account = :account OR t.accountTo = :account ORDER BY t.date DESC, t.id DESC");
        query.setParameter("account", account);
        return query.list();
    }

    public Transaction get(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = (Transaction) session.get(Transaction.class, id);
        return transaction;
    }

    public void add(Transaction transaction) throws CashManagerException {
        logger.debug("Adding new transaction");

        CashManagerErrorType errorType = checkTransaction(transaction);
        if (errorType != null) {
            throw new CashManagerException(errorType);
        }

        Session session = sessionFactory.getCurrentSession();

        // changing accounts amount according to transaction amount
        proceedTransaction(null, transaction);

        session.save(transaction);
    }

    public void delete(Long id) {
        logger.debug("Deleting existing transaction");
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = (Transaction) session.get(Transaction.class, id);

        // changing accounts amount according to transaction amount
        proceedTransaction(transaction, null);

        session.delete(transaction);
    }

    public void edit(Transaction transaction) throws CashManagerException {
        logger.debug("Editing existing transaction");

        CashManagerErrorType errorType = checkTransaction(transaction);
        if (errorType != null) {
            throw new CashManagerException(errorType);
        }

        Session session = sessionFactory.getCurrentSession();

        Transaction existingTransaction = (Transaction) session.get(Transaction.class, transaction.getId());

        // changing accounts amount according to transaction amount
        proceedTransaction(existingTransaction, transaction);

        existingTransaction.setType(transaction.getType());
        existingTransaction.setAmount(transaction.getAmount());
        existingTransaction.setAccount(transaction.getAccount());
        existingTransaction.setAccountTo(transaction.getAccountTo());
        existingTransaction.setCategory(transaction.getCategory());
        existingTransaction.setRecipient(transaction.getRecipient());
        existingTransaction.setComment(transaction.getComment());
        Date date = transaction.getDate();
        existingTransaction.setDate(date);

        session.save(existingTransaction);
    }

    // returns null if there is no errors
    private CashManagerErrorType checkTransaction(Transaction transaction) {
        if ((transaction.getType() == TransactionType.TRANSFER) && (!transaction.getAccount().getCurrency().equals(transaction.getAccountTo().getCurrency()))) {
            return CashManagerErrorType.TRANSFER_CROSS_CURRENCY;
        } else {
            return null;
        }
    }

    private void changeAmount(Account account, Integer delta) {
        logger.debug("Changing existing account amount");

        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("SELECT wa FROM AccountAmount as wa WHERE wa.account = :account");
        query.setParameter("account", account);
        AccountAmount accountAmount = (AccountAmount) query.uniqueResult();
        accountAmount.setAmount(accountAmount.getAmount() + delta);

        session.save(accountAmount);
    }

    private void proceedTransaction(Transaction existingTransaction, Transaction newTransaction) {
        Map<Account, Integer> accountAmountMap = new HashMap<>();
        if (existingTransaction != null) {
            Integer amount = existingTransaction.getAmount();
            switch (existingTransaction.getType()) {
                case DEPOSIT:
                    accountAmountMap.put(existingTransaction.getAccount(), -amount);
                    break;
                case WITHDRAW:
                    accountAmountMap.put(existingTransaction.getAccount(), +amount);
                    break;
                case TRANSFER:
                    accountAmountMap.put(existingTransaction.getAccount(), +amount);
                    accountAmountMap.put(existingTransaction.getAccountTo(), -amount);
                    break;
            }
        }
        if (newTransaction != null) {
            Integer amount = newTransaction.getAmount();
            Account account = newTransaction.getAccount();
            Account accountTo = newTransaction.getAccountTo();
            Integer beforeAmount = accountAmountMap.get(account);
            Integer beforeAmountTo = accountAmountMap.get(accountTo);
            switch (newTransaction.getType()) {
                case DEPOSIT:
                    if (beforeAmount != null) {
                        accountAmountMap.put(account, beforeAmount + amount);
                    } else {
                        accountAmountMap.put(account, +amount);
                    }
                    break;
                case WITHDRAW:
                    if (beforeAmount != null) {
                        accountAmountMap.put(account, beforeAmount - amount);
                    } else {
                        accountAmountMap.put(account, -amount);
                    }
                    break;
                case TRANSFER:
                    if (beforeAmount != null) {
                        accountAmountMap.put(account, beforeAmount - amount);
                    } else {
                        accountAmountMap.put(account, -amount);
                    }
                    if (beforeAmountTo != null) {
                        accountAmountMap.put(accountTo, beforeAmountTo + amount);
                    } else {
                        accountAmountMap.put(accountTo, +amount);
                    }
                    break;
            }
        }
        for (Account account : accountAmountMap.keySet()) {
            changeAmount(account, accountAmountMap.get(account));
        }
    }

}