package service;

import domain.Category;
import domain.Currency;
import domain.User;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("categoryService")
@Transactional
public class CategoryService {

    protected static Logger logger = Logger.getLogger("org/service");

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public List<Category> getAll() {
        logger.debug("Retrieving all categories");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Category");
        return query.list();
    }

    public List<Category> getAllByUser(User user) {
        logger.debug("Retrieving all categories of current user");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Category c WHERE c.user = :user");
        query.setParameter("user", user);
        return query.list();
    }

    public Category get(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Category category = (Category) session.get(Category.class, id);
        return category;
    }

    public void add(Category category, User user) {
        logger.debug("Adding new category");
        Session session = sessionFactory.getCurrentSession();
        category.setUser(user);
        session.save(category);
    }

    public boolean delete(Long id) {
        logger.debug("Deleting existing category");
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("SELECT t FROM Transaction as t WHERE t.category.id = :categoryId");
        query.setParameter("categoryId", id);
        if (query.list().size() == 0) {
            Category category = (Category) session.get(Category.class, id);
            session.delete(category);
            return true;
        } else {
            return false;
        }
    }

    public void edit(Category category) {
        logger.debug("Editing existing category");
        Session session = sessionFactory.getCurrentSession();
        Category existingCategory = (Category) session.get(Category.class, category.getId());
        existingCategory.setName(category.getName());
        session.save(existingCategory);
    }

}