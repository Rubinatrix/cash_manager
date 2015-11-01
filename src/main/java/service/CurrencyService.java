package service;

import utils.CashManagerErrorType;
import domain.Currency;
import domain.User;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.CashManagerException;

import javax.annotation.Resource;
import java.util.List;

@Service("currencyService")
@Transactional
public class CurrencyService {

    protected static Logger logger = Logger.getLogger("org/service");

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public List<Currency> getAll() {
        logger.debug("Retrieving all currencies");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Currency");
        return query.list();
    }

    public List<Currency> getAllByUser(User user) {
        logger.debug("Retrieving all currencies of current user");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Currency c WHERE c.user = :user");
        query.setParameter("user", user);
        return query.list();
    }

    public Currency get(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Currency currency = (Currency) session.get(Currency.class, id);
        return currency;
    }

    public void add(Currency currency, User user) {
        logger.debug("Adding new currency");
        Session session = sessionFactory.getCurrentSession();
        currency.setUser(user);
        session.save(currency);
    }

    public void delete(Long id) throws CashManagerException {
        logger.debug("Deleting existing currency");
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("SELECT acc FROM Account as acc WHERE acc.currency.id = :currencyId");
        query.setParameter("currencyId", id);
        if (query.list().size() == 0) {
            Currency currency = (Currency) session.get(Currency.class, id);
            session.delete(currency);
        } else {
            throw new CashManagerException(CashManagerErrorType.CURRENCY_DELETE);
        }
    }

    public void edit(Currency currency) {
        logger.debug("Editing existing currency");
        Session session = sessionFactory.getCurrentSession();
        Currency existingCurrency = (Currency) session.get(Currency.class, currency.getId());
        existingCurrency.setName(currency.getName());
        session.save(existingCurrency);
    }

}