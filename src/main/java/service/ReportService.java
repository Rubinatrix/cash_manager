package service;

import domain.Currency;
import domain.TransactionType;
import domain.User;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
        Query query;
        if (transactionType == TransactionType.WITHDRAW) {
            query = session.createQuery("SELECT t.category.name, -SUM(t.amount) FROM Transaction t WHERE t.account.user = :user AND t.account.currency = :currency AND t.date >= :from AND t.date <= :to AND t.type = :type GROUP BY t.type, t.category.id, t.category.name ORDER BY -SUM(t.amount)");
        } else {
            query = session.createQuery("SELECT t.category.name, SUM(t.amount) FROM Transaction t WHERE t.account.user = :user AND t.account.currency = :currency AND t.date >= :from AND t.date <= :to AND t.type = :type GROUP BY t.type, t.category.id, t.category.name ORDER BY -SUM(t.amount)");
        }
        query.setParameter("type", transactionType);
        query.setParameter("currency", currency);
        query.setParameter("user", user);
        query.setParameter("from", from);
        query.setParameter("to", to);
        return query.list();
    }

    public Double getTotalCategoryAmount(User user, Currency currency, TransactionType transactionType, Date from, Date to) {
        logger.debug("Retrieving category report total amount");
        Session session = sessionFactory.getCurrentSession();
        Query query;
        if (transactionType == TransactionType.WITHDRAW) {
            query = session.createQuery("SELECT -SUM(t.amount) FROM Transaction t WHERE t.account.user = :user AND t.account.currency = :currency AND t.category is not null AND t.date >= :from AND t.date <= :to AND t.type = :type GROUP BY t.type");
        } else {
            query = session.createQuery("SELECT SUM(t.amount) FROM Transaction t WHERE t.account.user = :user AND t.account.currency = :currency AND t.category is not null AND t.date >= :from AND t.date <= :to AND t.type = :type GROUP BY t.type");
        }
        query.setParameter("type", transactionType);
        query.setParameter("currency", currency);
        query.setParameter("user", user);
        query.setParameter("from", from);
        query.setParameter("to", to);
        Double result = (Double) query.uniqueResult();
        return result;
    }

    public List<Object> getRecipientAmount(User user, Currency currency, Date from, Date to) {
        logger.debug("Retrieving recipient report strings");
        Session session = sessionFactory.getCurrentSession();

        Map<String, Double> map = new HashMap<>();

        Query query_deposit = session.createQuery("SELECT t.recipient, -SUM(t.amount) FROM Transaction t WHERE t.account.user = :user AND t.account.currency = :currency AND t.recipient != :blank AND t.date >= :from AND t.date <= :to AND t.type = :type GROUP BY t.type, t.recipient");
        query_deposit.setParameter("type", TransactionType.DEPOSIT);
        query_deposit.setParameter("currency", currency);
        query_deposit.setParameter("user", user);
        query_deposit.setParameter("from", from);
        query_deposit.setParameter("to", to);
        query_deposit.setParameter("blank", "");
        List<Object> res_deposit = query_deposit.list();

        Query query_withdraw = session.createQuery("SELECT t.recipient, SUM(t.amount) FROM Transaction t WHERE t.account.user = :user AND t.account.currency = :currency AND t.recipient != :blank AND t.date >= :from AND t.date <= :to AND t.type = :type GROUP BY t.type, t.recipient");
        query_withdraw.setParameter("type", TransactionType.WITHDRAW);
        query_withdraw.setParameter("currency", currency);
        query_withdraw.setParameter("user", user);
        query_withdraw.setParameter("from", from);
        query_withdraw.setParameter("to", to);
        query_withdraw.setParameter("blank", "");
        List<Object> res_withdraw = query_withdraw.list();

        for (int i=0; i<res_deposit.size(); i++){
            String recipient = (String)((Object[]) res_deposit.get(i))[0];
            Double amount = (Double)((Object[]) res_deposit.get(i))[1];
            map.put(recipient, amount);
        }

        for (int i=0; i<res_withdraw.size(); i++){
            String recipient = (String)((Object[]) res_withdraw.get(i))[0];
            Double amount = (Double)((Object[]) res_withdraw.get(i))[1];
            Double existing_amount = map.get(recipient);
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

    public Double getTotalRecipientAmount(User user, Currency currency, Date from, Date to) {

        logger.debug("Retrieving recipient report total amount");
        Session session = sessionFactory.getCurrentSession();

        Map<String, Double> map = new HashMap<>();

        Query query_deposit = session.createQuery("SELECT -SUM(t.amount) FROM Transaction t WHERE t.account.user = :user AND t.account.currency = :currency AND t.recipient != :blank AND t.date >= :from AND t.date <= :to AND t.type = :type GROUP BY t.type");
        query_deposit.setParameter("type", TransactionType.DEPOSIT);
        query_deposit.setParameter("currency", currency);
        query_deposit.setParameter("user", user);
        query_deposit.setParameter("from", from);
        query_deposit.setParameter("to", to);
        query_deposit.setParameter("blank", "");
        Double result_deposit = (Double) query_deposit.uniqueResult();
        if (result_deposit == null){
            result_deposit = Double.valueOf(0);
        }

        Query query_withdraw = session.createQuery("SELECT SUM(t.amount) FROM Transaction t WHERE t.account.user = :user AND t.account.currency = :currency AND t.recipient != :blank AND t.date >= :from AND t.date <= :to AND t.type = :type GROUP BY t.type");
        query_withdraw.setParameter("type", TransactionType.WITHDRAW);
        query_withdraw.setParameter("currency", currency);
        query_withdraw.setParameter("user", user);
        query_withdraw.setParameter("from", from);
        query_withdraw.setParameter("to", to);
        query_withdraw.setParameter("blank", "");
        Double result_withdraw = (Double) query_withdraw.uniqueResult();
        if (result_withdraw == null){
            result_withdraw = Double.valueOf(0);
        }

        Double result = result_deposit + result_withdraw;

        return result;

    }
}
