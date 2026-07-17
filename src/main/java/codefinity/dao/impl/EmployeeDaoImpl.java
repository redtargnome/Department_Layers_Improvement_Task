package codefinity.dao.impl;

import codefinity.dao.EmployeeDao;
import codefinity.model.Department;
import codefinity.model.Employee;
import codefinity.model.Role;
import codefinity.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

public class EmployeeDaoImpl implements EmployeeDao {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public Employee add(Employee employee) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(employee);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Can't add new Employee", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return employee;
    }
    @Override
    public Employee getById(int id) {
        Session session = null;
        Employee employee = null;
        try {
            session = sessionFactory.openSession();
            employee = session.get(Employee.class, id);
        } catch (Exception e) {
            throw new HibernateException("Can't get Employee by ID " + id, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return employee;
    }

    @Override
    public List<Employee> getEmployeesHiredInASpecificTimeframe(LocalDate startDate, LocalDate endDate) {
        Session session = null;
        List<Employee> employees = null;
        try {
            session = sessionFactory.openSession();

            String hql = "FROM Employee WHERE hireDate >= :startDate AND hireDate <= :endDate";

            Query<Employee> query = session.createQuery(hql, Employee.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            employees = query.getResultList();
        } catch (Exception e) {
            throw new HibernateException("Can't get employees hired in the specified timeframe", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return employees;
    }

    @Override
    public List<Employee> getAll() {
        Session session = null;
        List<Employee> employees = null;
        try {
            session = sessionFactory.openSession();

            String hql = "FROM Employee e";

            Query<Employee> query = session.createQuery(hql, Employee.class);
            employees = query.getResultList();
        } catch (Exception e) {
            throw new NoSuchElementException("Can't get Employees from the DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return employees;
    }

    @Override
    public List<Employee> getEmployeesWithSalaryMoreThan(Double salary) {
        Session session = null;
        List<Employee> employees = null;
        try {
            session = sessionFactory.openSession();

            String hql = "FROM Employee e WHERE e.salary > :salary";

            Query<Employee> query = session.createQuery(hql, Employee.class);
            query.setParameter("salary", salary);
            employees = query.getResultList();
        } catch (Exception e) {
            throw new HibernateException("Can't get employees with salary greater than " + salary, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return employees;
    }

    @Override
    public Employee setDepartmentById(int employeeId, int departmentId) {
        Employee employee = null;
        Department department = null;
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            employee = session.get(Employee.class, employeeId);
            department = session.get(Department.class, departmentId);
            if (employee == null) {
                throw new NoSuchElementException("Can't get employee by ID " + employeeId);
            }
            if (department == null) {
                throw new NoSuchElementException("Can't get department by ID " + departmentId);
            }

            employee.setDepartment(department);
            session.merge(employee);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Can't set department for employee " + employeeId, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return employee;
    }

    @Override
    public Employee setRoleById(int employeeId, int roleId) {
        Employee employee = null;
        Role role = null;
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            employee = session.get(Employee.class, employeeId);
            role = session.get(Role.class, roleId);
            if (employee == null) {
                throw new NoSuchElementException("Can't get employee by ID " + employeeId);
            }
            if (role == null) {
                throw new NoSuchElementException("Can't get role by ID " + roleId);
            }

            employee.setRole(role);
            session.merge(employee);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Can't set role for employee " + employeeId, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return employee;
    }

    @Override
    public Employee updateEmployee(int employeeId, Employee newEmployee) {
        Employee employee = null;
        Session session = null;
        Transaction transaction = null;

        if (newEmployee == null) {
            throw new NullPointerException("Employee is null!");
        }

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            employee = session.get(Employee.class, employeeId);
            if (employee == null) {
                throw new NoSuchElementException("Can't get employee by ID " + employeeId);
            }
            employee.setName(newEmployee.getName());
            employee.setSalary(newEmployee.getSalary());
            employee.setHireDate(newEmployee.getHireDate());
            employee.setDepartment(newEmployee.getDepartment());
            employee.setRole(newEmployee.getRole());

            session.merge(employee);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Can't update employee " + employeeId, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return employee;
    }
}
