package codefinity.dao.impl;

import codefinity.dao.TaskDao;
import codefinity.model.Task;
import codefinity.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.NoSuchElementException;

public class TaskDaoImpl implements TaskDao {
  private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    @Override
    public Task add(Task task) {
        Session session = null;
        Transaction transaction = null;
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(task);
            transaction.commit();

        }catch (Exception e){
            if(transaction !=null){
                transaction.rollback();
            }
            throw new HibernateException("Cannot add task", e);
        } finally{
            if(session !=null){
                session.close();
            }
        }


        return task;
    }

    @Override
    public Task getById(int id) {
        Session session = null;
        Task task = null;
        try{
            session = sessionFactory.openSession();
            task = session.get(Task.class, id);

        } catch(Exception e){
            throw new HibernateException("Can't get task by ID " + id, e);

        }finally{
            if(session!= null){
                session.close();
            }
        }
        return task;
    }

    @Override
    public List<Task> getAll() {
        Session session = null;
        List<Task>tasks = null;
        try{
            session = sessionFactory.openSession();
            String hql = "FROM Task";
            Query<Task> query = session.createQuery(hql, Task.class);

            tasks = query.getResultList();
        } catch (Exception e) {
            throw new NoSuchElementException("Cannot get tasks from the db", e);
        } finally{
            if(session!=null){
                session.close();
            }
        }

        return tasks;
    }

    @Override
    public Task updateTask(int id, Task newTask) {
        Session session =null;
        Transaction transaction = null;
        Task task = null;
        if(newTask == null){
            throw new NullPointerException("task is null");
        }
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            task = session.get(Task.class, id);
            if (task == null) {
                throw new NoSuchElementException("Can't get task by ID " + id);
            }

            task.setDeadline(newTask.getDeadline());
            task.setDescription(newTask.getDescription());
            task.setTitle(newTask.getTitle());
            task.setStatus(newTask.getStatus());
            task.setEmployee(newTask.getEmployee());
            session.merge(task);
            transaction.commit();
        }catch(Exception e){
            if(transaction!=null){
                transaction.rollback();
            }
            throw new HibernateException("Cannot update task " + id, e);
        }finally {
            if(session!=null){
                session.close();
            }
        }



        return task;
    }
}
