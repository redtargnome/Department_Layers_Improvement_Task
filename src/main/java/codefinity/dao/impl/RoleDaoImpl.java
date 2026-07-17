package codefinity.dao.impl;

import codefinity.dao.RoleDao;
import codefinity.model.Role;
import codefinity.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.NoSuchElementException;

public class RoleDaoImpl implements RoleDao {
    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public Role add(Role role) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            session.persist(role);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Can't add new Role", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return role;
    }

    @Override
    public Role getById(int id) {
        Session session = null;
        Role role = null;

        try {
            session = sessionFactory.openSession();
            role = session.get(Role.class, id);
        } catch (Exception e) {
            throw new NoSuchElementException("Can't get Role by ID " + id, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return role;
    }

    @Override
    public List<Role> getAll() {
        Session session = null;
        List<Role> roles = null;

        try {
            session = sessionFactory.openSession();

            String hql = "FROM Role";
            Query<Role> query = session.createQuery(hql, Role.class);

            roles = query.getResultList();
        } catch (Exception e) {
            throw new NoSuchElementException("Can't get Employees from the DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return roles;
    }

    @Override
    public Role updateRole(int id, Role newRole) {
        Session session = null;
        Role role = null;
        Transaction transaction = null;

        if (newRole == null) {
            throw new NullPointerException("The `newRole` is null!");
        }

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            role = session.get(Role.class, id);
            if (role == null) {
                throw new NoSuchElementException("Can't get role by ID " + id);
            }
            role.setName(newRole.getName());
            role.setDescription(newRole.getDescription());

            session.merge(role);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Can't update role " + id, e);
        }finally {
            if(session != null){
                session.close();
            }
        }
        return role;
    }
}
