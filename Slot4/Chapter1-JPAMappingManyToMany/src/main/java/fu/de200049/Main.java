package fu.de200049;

import fu.de200049.pojo.Employee;
import fu.de200049.pojo.Gender;
import fu.de200049.pojo.Project;
import fu.de200049.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        EntityManager em =
                JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();

            // Tạo 3 Employee
            Employee nv1 = new Employee(
                    "nv1@company.com",
                    "Nguyen Van 1",
                    Gender.MALE,
                    new BigDecimal("1500"),
                    LocalDate.of(2024, 1, 10)
            );

            Employee nv2 = new Employee(
                    "nv2@company.com",
                    "Nguyen Van 2",
                    Gender.FEMALE,
                    new BigDecimal("2000"),
                    LocalDate.of(2023, 5, 15)
            );

            Employee nv3 = new Employee(
                    "nv3@company.com",
                    "Nguyen Van 3",
                    Gender.OTHER,
                    new BigDecimal("1800"),
                    LocalDate.of(2022, 8, 20)
            );

            // Tạo 2 Project
            Project projectA = new Project(
                    "PRJ-A",
                    "Project A",
                    new BigDecimal("10000"),
                    LocalDate.of(2026, 1, 1),
                    null
            );

            Project projectB = new Project(
                    "PRJ-B",
                    "Project B",
                    new BigDecimal("20000"),
                    LocalDate.of(2026, 2, 1),
                    null
            );

            // Phân công nhân viên vào project
            // NV1 -> A + B
            nv1.assignToProject(projectA);
            nv1.assignToProject(projectB);

            // NV2 -> B
            nv2.assignToProject(projectB);

            // NV3 -> A
            nv3.assignToProject(projectA);

            // Lưu Employee và Project
            em.persist(nv1);
            em.persist(nv2);
            em.persist(nv3);

            em.persist(projectA);
            em.persist(projectB);

            em.getTransaction().commit();

            // In project của từng employee
            System.out.println("Employee: " + nv1.getFullName());
            nv1.getProjects().forEach(
                    p -> System.out.println(" - " + p.getProjectName())
            );

            System.out.println("Employee: " + nv2.getFullName());
            nv2.getProjects().forEach(
                    p -> System.out.println(" - " + p.getProjectName())
            );

            System.out.println("Employee: " + nv3.getFullName());
            nv3.getProjects().forEach(
                    p -> System.out.println(" - " + p.getProjectName())
            );

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}