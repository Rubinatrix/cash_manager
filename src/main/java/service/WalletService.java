package service;

import domain.*;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("walletService")
@Transactional
public class WalletService {

    protected static Logger logger = Logger.getLogger("org/service");

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public List<Object[]> getAllWithAmount() {
        logger.debug("Retrieving all wallets with amount");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT w, wa.amount FROM Wallet as w, WalletAmount as wa WHERE w.id = wa.wallet");
        return  query.list();
    }

    public List<Wallet> getAll() {
        logger.debug("Retrieving all wallets");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Wallet");
        return  query.list();
    }

    public List<Object[]> getAllWithAmountByUser(User user) {
        logger.debug("Retrieving all wallets with amount");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT w, wa.amount FROM Wallet as w, WalletAmount as wa WHERE w.user = :user AND w.id = wa.wallet");
        query.setParameter("user", user);
        return  query.list();
    }

    public List<Wallet> getAllByUser(User user) {
        logger.debug("Retrieving all wallets");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Wallet w WHERE w.user = :user");
        query.setParameter("user", user);
        return  query.list();
    }

    public Integer getAmountForWallet(Wallet wallet) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT wa.amount FROM WalletAmount as wa WHERE wa.wallet = :wallet");
        query.setParameter("wallet", wallet);
        Integer amount = (Integer) query.uniqueResult();
        return amount;
    }

    public Wallet get(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Wallet wallet = (Wallet) session.get(Wallet.class, id);
        return wallet;
    }

    public void add(Wallet wallet, User user) {
        logger.debug("Adding new wallet");
        Session session = sessionFactory.getCurrentSession();

        // initialize wallet with 0 amount
        WalletAmount walletAmount = new WalletAmount(wallet, 0);

        wallet.setUser(user);
        session.save(wallet);
        session.save(walletAmount);
    }

    public void delete(Long id) {
        logger.debug("Deleting existing wallet");

        Session session = sessionFactory.getCurrentSession();

        // delete amount of deleting wallet
        Query query = session.createQuery("SELECT wa FROM WalletAmount as wa WHERE wa.wallet.id = :walletId");
        query.setParameter("walletId", id);

        for (Object result: query.list()) {
            WalletAmount walletAmount = (WalletAmount) result;
            session.delete(walletAmount);
        }

        // delete regular transactions with deleting wallet
        query = session.createQuery("SELECT t FROM Transaction as t WHERE t.wallet.id = :walletId and t.type != :type");
        query.setParameter("walletId", id);
        query.setParameter("type", TransactionType.TRANSFER);
        for (Object result: query.list()) {
            Transaction transaction = (Transaction) result;
            session.delete(transaction);
        }

        // converting transfer transactions with deleting walletFrom to regular
        query = session.createQuery("SELECT t FROM Transaction as t WHERE t.wallet.id = :walletId and t.type = :type");
        query.setParameter("walletId", id);
        query.setParameter("type", TransactionType.TRANSFER);
        for (Object result: query.list()) {
            Transaction transaction = (Transaction) result;
            transaction.setWallet(transaction.getWalletTo());
            transaction.setWalletTo(null);
            transaction.setType(TransactionType.DEPOSIT);
            session.save(transaction);
        }

        // converting transfer transactions with deleting walletTo to regular
        query = session.createQuery("SELECT t FROM Transaction as t WHERE t.walletTo.id = :walletId and t.type = :type");
        query.setParameter("walletId", id);
        query.setParameter("type", TransactionType.TRANSFER);
        for (Object result: query.list()) {
            Transaction transaction = (Transaction) result;
            transaction.setWalletTo(null);
            transaction.setType(TransactionType.WITHDRAW);
            session.save(transaction);
        }

        Wallet wallet = (Wallet) session.get(Wallet.class, id);
        session.delete(wallet);
    }

    public void edit(Wallet wallet) {
        logger.debug("Editing existing wallet");

        Session session = sessionFactory.getCurrentSession();

        Wallet existingWallet = (Wallet) session.get(Wallet.class, wallet.getId());

        existingWallet.setName(wallet.getName());
        existingWallet.setCurrency(wallet.getCurrency());

        session.save(existingWallet);
    }

}