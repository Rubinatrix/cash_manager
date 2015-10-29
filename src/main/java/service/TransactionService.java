package service;

import domain.Transaction;
import domain.User;
import domain.Wallet;
import domain.WalletAmount;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("transactionService")
@Transactional
public class TransactionService {

    protected static Logger logger = Logger.getLogger("org/service");

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public List<Transaction> getAll() {
        logger.debug("Retrieving all transactions");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT t FROM Transaction as t ORDER BY t.date DESC");
        return  query.list();
    }

    public List<Transaction> getAllByUser(User user) {
        logger.debug("Retrieving all transactions");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT t FROM Transaction as t WHERE t.wallet.user = :user ORDER BY t.date DESC");
        query.setParameter("user", user);
        return  query.list();
    }

    public List<Transaction> getAllByWallet(Wallet wallet) {
        logger.debug("Retrieving transactions by specified wallet");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT t FROM Transaction as t WHERE t.wallet = :wallet OR t.walletTo = :wallet ORDER BY t.date DESC");
        query.setParameter("wallet", wallet);
        return  query.list();
    }

    public Transaction get(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = (Transaction) session.get(Transaction.class, id);
        return transaction;
    }

    public void add(Transaction transaction) {
        logger.debug("Adding new transaction");
        Session session = sessionFactory.getCurrentSession();

        // changing wallets amount according to transaction amount
        proceedTransaction(null, transaction);

        session.save(transaction);
    }

    public void delete(Long id) {
        logger.debug("Deleting existing transaction");
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = (Transaction) session.get(Transaction.class, id);

        // changing wallets amount according to transaction amount
        proceedTransaction(transaction, null);

        session.delete(transaction);
    }

    public void edit(Transaction transaction) {
        logger.debug("Editing existing transaction");

        Session session = sessionFactory.getCurrentSession();

        Transaction existingTransaction = (Transaction) session.get(Transaction.class, transaction.getId());

        // changing wallets amount according to transaction amount
        proceedTransaction(existingTransaction, transaction);

        existingTransaction.setType(transaction.getType());
        existingTransaction.setAmount(transaction.getAmount());
        existingTransaction.setWallet(transaction.getWallet());
        existingTransaction.setWalletTo(transaction.getWalletTo());
        existingTransaction.setCategory(transaction.getCategory());
        existingTransaction.setRecipient(transaction.getRecipient());
        existingTransaction.setComment(transaction.getComment());
        Date date = transaction.getDate();
        existingTransaction.setDate(date);

        session.save(existingTransaction);
    }

    private void changeAmount(Wallet wallet, Integer delta){
        logger.debug("Changing existing wallet amount");

        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("SELECT wa FROM WalletAmount as wa WHERE wa.wallet = :wallet");
        query.setParameter("wallet", wallet);
        WalletAmount walletAmount = (WalletAmount) query.uniqueResult();
        walletAmount.setAmount(walletAmount.getAmount()+delta);

        session.save(walletAmount);
    }

    private void proceedTransaction(Transaction existingTransaction, Transaction newTransaction){
        Map<Wallet, Integer> walletAmountMap = new HashMap<>();
        if (existingTransaction != null){
            Integer amount = existingTransaction.getAmount();
            switch (existingTransaction.getType()){
                case DEPOSIT:
                    walletAmountMap.put(existingTransaction.getWallet(), - amount);
                    break;
                case WITHDRAW:
                    walletAmountMap.put(existingTransaction.getWallet(), + amount);
                    break;
                case TRANSFER:
                    walletAmountMap.put(existingTransaction.getWallet(), + amount);
                    walletAmountMap.put(existingTransaction.getWalletTo(), - amount);
                    break;
            }
        }
        if (newTransaction != null) {
            Integer amount = newTransaction.getAmount();
            Wallet wallet = newTransaction.getWallet();
            Wallet walletTo = newTransaction.getWalletTo();
            Integer beforeAmount = walletAmountMap.get(wallet);
            Integer beforeAmountTo = walletAmountMap.get(walletTo);
            switch (newTransaction.getType()){
                case DEPOSIT:
                    if (beforeAmount != null){
                        walletAmountMap.put(wallet, beforeAmount + amount);
                    } else {
                        walletAmountMap.put(wallet, + amount);
                    }
                    break;
                case WITHDRAW:
                    if (beforeAmount != null){
                        walletAmountMap.put(wallet, beforeAmount - amount);
                    } else {
                        walletAmountMap.put(wallet, - amount);
                    }
                    break;
                case TRANSFER:
                    if (beforeAmount != null){
                        walletAmountMap.put(wallet, beforeAmount - amount);
                    } else {
                        walletAmountMap.put(wallet, - amount);
                    }
                    if (beforeAmountTo != null){
                        walletAmountMap.put(walletTo, beforeAmountTo + amount);
                    } else {
                        walletAmountMap.put(walletTo, + amount);
                    }
                    break;
            }
        }
        for (Wallet wallet: walletAmountMap.keySet()){
            changeAmount(wallet, walletAmountMap.get(wallet));
        }
    }

}