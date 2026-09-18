package fu.de200049.dao;

import fu.de200049.pojo.Employee;
import fu.de200049.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class EmployeeDAO {

    public void save(Employee employee) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(employee);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Employee> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return em.createQuery(
                    "SELECT e FROM Employee e", Employee.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public Employee findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return em.find(Employee.class, id);
        } finally {
            em.close();
        }
    }

    public Employee update(Employee employee) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();
            employee = em.merge(employee);
            em.getTransaction().commit();
            return employee;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }



    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();

            Employee employee = em.find(Employee.class, id);
            if (employee != null) {
                em.remove(employee);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Employee findByIdWithDepartment(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return em.createQuery(
                            "SELECT e FROM Employee e " +
                                    "JOIN FETCH e.department " +
                                    "WHERE e.id = :id",
                            Employee.class
                    ).setParameter("id", id)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}