package service;

import domain.*;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("accountService")
@Transactional
public class AccountService {

    protected static Logger logger = Logger.getLogger("org/service");

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public List<Object[]> getAllWithAmount() {
        logger.debug("Retrieving all accounts with amount");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT acc, aa.amount FROM Account as acc, AccountAmount as aa WHERE acc.id = aa.account");
        return  query.list();
    }

    public List<Account> getAll() {
        logger.debug("Retrieving all accounts");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Account");
        return  query.list();
    }

    public List<Object[]> getAllWithAmountByUser(User user) {
        logger.debug("Retrieving all accounts with amount");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT acc, aa.amount FROM Account as acc, AccountAmount as aa WHERE acc.user = :user AND acc.id = aa.account");
        query.setParameter("user", user);
        return  query.list();
    }

    public List<Account> getAllByUser(User user) {
        logger.debug("Retrieving all accounts");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Account acc WHERE acc.user = :user");
        query.setParameter("user", user);
        return  query.list();
    }

    public Integer getAmountForAccount(Account account) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT wa.amount FROM AccountAmount as wa WHERE wa.account = :account");
        query.setParameter("account", account);
        Integer amount = (Integer) query.uniqueResult();
        return amount;
    }

    public Account get(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Account account = (Account) session.get(Account.class, id);
        return account;
    }

    public void add(Account account, User user) {
        logger.debug("Adding new account");
        Session session = sessionFactory.getCurrentSession();

        // initialize account with 0 amount
        AccountAmount accountAmount = new AccountAmount(account, 0);

        account.setUser(user);
        session.save(account);
        session.save(accountAmount);
    }

    public void delete(Long id) {
        logger.debug("Deleting existing account");

        Session session = sessionFactory.getCurrentSession();

        // delete amount of deleting account
        Query query = session.createQuery("SELECT wa FROM AccountAmount as wa WHERE wa.account.id = :accountId");
        query.setParameter("accountId", id);

        for (Object result: query.list()) {
            AccountAmount accountAmount = (AccountAmount) result;
            session.delete(accountAmount);
        }

        // delete regular transactions with deleting account
        query = session.createQuery("SELECT t FROM Transaction as t WHERE t.account.id = :accountId and t.type != :type");
        query.setParameter("accountId", id);
        query.setParameter("type", TransactionType.TRANSFER);
        for (Object result: query.list()) {
            Transaction transaction = (Transaction) result;
            session.delete(transaction);
        }

        // converting transfer transactions with deleting account to regular
        query = session.createQuery("SELECT t FROM Transaction as t WHERE t.account.id = :accountId and t.type = :type");
        query.setParameter("accountId", id);
        query.setParameter("type", TransactionType.TRANSFER);
        for (Object result: query.list()) {
            Transaction transaction = (Transaction) result;
            transaction.setAccount(transaction.getAccountTo());
            transaction.setAccountTo(null);
            transaction.setType(TransactionType.DEPOSIT);
            session.save(transaction);
        }

        // converting transfer transactions with deleting accountTo to regular
        query = session.createQuery("SELECT t FROM Transaction as t WHERE t.accountTo.id = :accountId and t.type = :type");
        query.setParameter("accountId", id);
        query.setParameter("type", TransactionType.TRANSFER);
        for (Object result: query.list()) {
            Transaction transaction = (Transaction) result;
            transaction.setAccountTo(null);
            transaction.setType(TransactionType.WITHDRAW);
            session.save(transaction);
        }

        Account account = (Account) session.get(Account.class, id);
        session.delete(account);
    }

    public void edit(Account account) {
        logger.debug("Editing existing account");

        Session session = sessionFactory.getCurrentSession();

        Account existingAccount = (Account) session.get(Account.class, account.getId());

        existingAccount.setName(account.getName());
        existingAccount.setCurrency(account.getCurrency());

        session.save(existingAccount);
    }

}