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
            // =========================
            // 1. Tạo Employee
            // =========================
            Employee nv1 = new Employee(
                    "nv4@company.com",
                    "Nguyen Van 4",
                    Gender.MALE,
                    new BigDecimal("1500"),
                    LocalDate.of(2024, 1, 10)
            );

            Employee nv2 = new Employee(
                    "nv5@company.com",
                    "Nguyen Van 5",
                    Gender.FEMALE,
                    new BigDecimal("2000"),
                    LocalDate.of(2023, 5, 15)
            );

            Employee nv3 = new Employee(
                    "nv6@company.com",
                    "Nguyen Van 6",
                    Gender.OTHER,
                    new BigDecimal("1800"),
                    LocalDate.of(2022, 8, 20)
            );

            // =========================
            // 2. Tạo Project
            // =========================
            Project projectC = new Project(
                    "PRJ-C",
                    "Project C",
                    new BigDecimal("10000"),
                    LocalDate.of(2026, 1, 1),
                    null
            );

            Project projectD = new Project(
                    "PRJ-D",
                    "Project D",
                    new BigDecimal("20000"),
                    LocalDate.of(2026, 2, 1),
                    null
            );

            // =========================
            // 3. Phân công ban đầu
            // NV1 -> C + D
            // NV2 -> D
            // NV3 -> C
            // =========================
            nv1.assignToProject(projectC);
            nv1.assignToProject(projectD);

            nv2.assignToProject(projectD);

            nv3.assignToProject(projectC);

            // =========================
            // 4. Persist
            // =========================
            em.getTransaction().begin();

            em.persist(nv1);
            em.persist(nv2);
            em.persist(nv3);

            em.persist(projectC);
            em.persist(projectD);

            em.getTransaction().commit();

            // =========================
            // 5. Unassign NV1 khỏi Project C
            // =========================
            em.getTransaction().begin();

            nv1.unassignFromProject(projectC);

            em.getTransaction().commit();

            // =========================
            // 6. Kiểm tra kết quả
            // =========================
            System.out.println("Sau khi unassign:");

            System.out.println("Employee: " + nv1.getFullName());

            nv1.getProjects().forEach(
                    p -> System.out.println(" - " + p.getProjectName())
            );

            System.out.println();

            System.out.println("Project C employees: "
                    + projectC.getEmployees().size());

            System.out.println("Project D employees: "
                    + projectD.getEmployees().size());

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