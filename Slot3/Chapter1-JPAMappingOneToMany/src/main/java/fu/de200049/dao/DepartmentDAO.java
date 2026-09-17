package fu.de200049.dao;

import fu.de200049.pojo.Department;
import fu.de200049.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class DepartmentDAO {

    public void save(Department department) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(department);
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

    public List<Department> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return em.createQuery(
                    "SELECT d FROM Department d",
                    Department.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public Department findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return em.find(Department.class, id);
        } finally {
            em.close();
        }
    }

    public Department update(Department department) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();
            department = em.merge(department);
            em.getTransaction().commit();
            return department;
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

            Department department = em.find(Department.class, id);

            if (department != null) {
                em.remove(department);
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


    public void demonstrateNPlusOne() {
        EntityManager em =
                JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            List<Department> departments = em.createQuery(
                    "SELECT d FROM Department d",
                    Department.class
            ).getResultList();

            for (Department d : departments) {
                System.out.println(
                        d.getName() + " - Employees: "
                                + d.getEmployees().size()
                );
            }
        } finally {
            em.close();
        }
    }
}