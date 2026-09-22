package fu.de200049.dao;

import fu.de200049.pojo.Employee;
import fu.de200049.pojo.Project;
import fu.de200049.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
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

    public void assignEmployeeToProject(Long employeeId, Long projectId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();

            Employee employee = em.find(Employee.class, employeeId);
            Project project = em.find(Project.class, projectId);

            employee.assignToProject(project);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void getProjectStatistics() {
        EntityManager em =
                JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            List<Object[]> results = em.createQuery(
                    "SELECT p.projectName, COUNT(e), SUM(e.salary) " +
                            "FROM Project p JOIN p.employees e " +
                            "WHERE e.active = true " +
                            "GROUP BY p.projectName",
                    Object[].class
            ).getResultList();

            for (Object[] row : results) {
                String projectName = (String) row[0];
                Long employeeCount = (Long) row[1];
                BigDecimal totalSalary = (BigDecimal) row[2];

                System.out.println(
                        projectName
                                + " | Employees: " + employeeCount
                                + " | Total Salary: " + totalSalary
                );
            }

        } finally {
            em.close();
        }
    }
    public List<Employee> findActiveEmployeesInMultipleProjects() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            String jpql = """
                SELECT e
                FROM Employee e
                WHERE e.active = true
                  AND SIZE(e.projects) > 1
                """;

            TypedQuery<Employee> query =
                    em.createQuery(jpql, Employee.class);

            return query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }


    public void deactivateEmployee(Long employeeId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Employee employee = em.find(Employee.class, employeeId);

            if (employee == null) {
                throw new IllegalArgumentException(
                        "Employee not found: " + employeeId
                );
            }

            employee.setActive(false);

            tx.commit();

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }


}