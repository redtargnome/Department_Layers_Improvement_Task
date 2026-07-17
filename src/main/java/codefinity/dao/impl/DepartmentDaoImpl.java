package codefinity.dao.impl;

import codefinity.dao.DepartmentDao;
import codefinity.model.Department;
import codefinity.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class DepartmentDaoImpl implements DepartmentDao {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public Department add(Department department) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(department);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Can't add new Department", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return department;
    }

    @Override
    public Department getById(int id) {
        Session session = null;
        Department department = null;
        try {
            session = sessionFactory.openSession();
            department = session.get(Department.class, id);
        } catch (Exception e) {
            throw new HibernateException("Can't get Department by ID " + id, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return department;
    }

    @Override
    public List<Department> getAll() {
        Session session = null;
        List<Department> departments = null;

        try {
            session = sessionFactory.openSession();

            String hql = "FROM Department d";

            Query<Department> query = session.createQuery(hql, Department.class);

            departments = query.getResultList();

        } catch (Exception e) {
            throw new HibernateException("Can't get departments", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return departments;
    }

    @Override
    public Department updateDepartment(int departmentId, Department newDepartment) {
        Session session = null;
        Transaction transaction = null;
        Department department = null;

        if(newDepartment==null){
            throw new NullPointerException("Department is null");
        }
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            department = session.get(Department.class, departmentId);
            if (department == null) {
                throw new java.util.NoSuchElementException("Can't get department by ID " + departmentId);
            }

            department.setName(newDepartment.getName());
            department.setLocation(newDepartment.getLocation());
            session.merge(department);
            transaction.commit();
        }catch(Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            throw new HibernateException("Cannot update department " + departmentId, e);
        }finally{
            if(session!=null){
                session.close();
            }
        }


        return department;
    }
}
