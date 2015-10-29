package service;

import domain.Currency;
import domain.Transaction;
import domain.TransactionType;
import domain.User;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.*;

@Service("reportService")
@Transactional
public class ReportService {

    protected static Logger logger = Logger.getLogger("org/service");

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public ReportService() {

    }

    public List<Object> getCategoryAmount(User user, Currency currency, TransactionType transactionType, Date from, Date to) {
        logger.debug("Retrieving category report strings");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT t.category.name, -SUM(t.amount) FROM Transaction t WHERE t.wallet.user = :user AND t.wallet.currency = :currency AND t.date >= :from AND t.date <= :to GROUP BY t.type, t.category.id, t.category.name HAVING t.type = :type ORDER BY -SUM(t.amount)");
        query.setParameter("type", transactionType);
        query.setParameter("currency", currency);
        query.setParameter("user", user);
        query.setParameter("from", from);
        query.setParameter("to", to);
        return query.list();
    }

    public Long getTotalCategoryAmount(User user, Currency currency, TransactionType transactionType, Date from, Date to) {
        logger.debug("Retrieving category report total amount");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT -SUM(t.amount) FROM Transaction t WHERE t.wallet.user = :user AND t.wallet.currency = :currency AND t.category is not null AND t.date >= :from AND t.date <= :to GROUP BY t.type HAVING t.type = :type");
        query.setParameter("type", transactionType);
        query.setParameter("currency", currency);
        query.setParameter("user", user);
        query.setParameter("from", from);
        query.setParameter("to", to);
        Long result = (Long) query.uniqueResult();
        return result;
    }

    public List<Object> getRecipientAmount(User user, Currency currency, Date from, Date to) {
        logger.debug("Retrieving recipient report strings");
        Session session = sessionFactory.getCurrentSession();

        Map<String, Long> map = new HashMap<>();

        Query query_deposit = session.createQuery("SELECT t.recipient, -SUM(t.amount) FROM Transaction t WHERE t.wallet.user = :user AND t.wallet.currency = :currency AND t.recipient != :blank AND t.date >= :from AND t.date <= :to GROUP BY t.type, t.recipient HAVING t.type = :type");
        query_deposit.setParameter("type", TransactionType.DEPOSIT);
        query_deposit.setParameter("currency", currency);
        query_deposit.setParameter("user", user);
        query_deposit.setParameter("from", from);
        query_deposit.setParameter("to", to);
        query_deposit.setParameter("blank", "");
        List<Object> res_deposit = query_deposit.list();

        Query query_withdraw = session.createQuery("SELECT t.recipient, SUM(t.amount) FROM Transaction t WHERE t.wallet.user = :user AND t.wallet.currency = :currency AND t.recipient != :blank AND t.date >= :from AND t.date <= :to GROUP BY t.type, t.recipient HAVING t.type = :type");
        query_withdraw.setParameter("type", TransactionType.WITHDRAW);
        query_withdraw.setParameter("currency", currency);
        query_withdraw.setParameter("user", user);
        query_withdraw.setParameter("from", from);
        query_withdraw.setParameter("to", to);
        query_withdraw.setParameter("blank", "");
        List<Object> res_withdraw = query_withdraw.list();

        for (int i=0; i<res_deposit.size(); i++){
            String recipient = (String)((Object[]) res_deposit.get(i))[0];
            Long amount = (Long)((Object[]) res_deposit.get(i))[1];
            map.put(recipient, amount);
        }

        for (int i=0; i<res_withdraw.size(); i++){
            String recipient = (String)((Object[]) res_withdraw.get(i))[0];
            Long amount = (Long)((Object[]) res_withdraw.get(i))[1];
            Long existing_amount = map.get(recipient);
            if (existing_amount!=null) {
                map.put(recipient, existing_amount+amount);
            } else {
                map.put(recipient, amount);
            }
        }

        List<Object> result = new ArrayList<>();
        for (String key: map.keySet()){
            Object[] result_el = new Object[2];
            result_el[0] = key;
            result_el[1] = map.get(key);
            result.add(result_el);
        }

        return result;
    }

    public Long getTotalRecipientAmount(User user, Currency currency, Date from, Date to) {

        logger.debug("Retrieving recipient report total amount");
        Session session = sessionFactory.getCurrentSession();

        Map<String, Long> map = new HashMap<>();

        Query query_deposit = session.createQuery("SELECT -SUM(t.amount) FROM Transaction t WHERE t.wallet.user = :user AND t.wallet.currency = :currency AND t.recipient != :blank AND t.date >= :from AND t.date <= :to GROUP BY t.type HAVING t.type = :type");
        query_deposit.setParameter("type", TransactionType.DEPOSIT);
        query_deposit.setParameter("currency", currency);
        query_deposit.setParameter("user", user);
        query_deposit.setParameter("from", from);
        query_deposit.setParameter("to", to);
        query_deposit.setParameter("blank", "");
        Long result_deposit = (Long) query_deposit.uniqueResult();
        if (result_deposit == null){
            result_deposit = Long.valueOf(0);
        }

        Query query_withdraw = session.createQuery("SELECT SUM(t.amount) FROM Transaction t WHERE t.wallet.user = :user AND t.wallet.currency = :currency AND t.recipient != :blank AND t.date >= :from AND t.date <= :to GROUP BY t.type HAVING t.type = :type");
        query_withdraw.setParameter("type", TransactionType.WITHDRAW);
        query_withdraw.setParameter("currency", currency);
        query_withdraw.setParameter("user", user);
        query_withdraw.setParameter("from", from);
        query_withdraw.setParameter("to", to);
        query_withdraw.setParameter("blank", "");
        Long result_withdraw = (Long) query_withdraw.uniqueResult();
        if (result_withdraw == null){
            result_withdraw = Long.valueOf(0);
        }

        Long result = result_deposit + result_withdraw;

        return result;

    }
}