package fu.de200049;

import fu.de200049.dao.EmployeeDAO;
import fu.de200049.pojo.Employee;
import fu.de200049.util.JPAUtil;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        EmployeeDAO employeeDAO = new EmployeeDAO();

        try {

            // TODO 5.10
            List<Employee> employees =
                    employeeDAO.findActiveEmployeesInMultipleProjects();

            System.out.println(
                    "=== TODO 5.10: Active Employees In Multiple Projects ==="
            );

            if (employees.isEmpty()) {
                System.out.println(
                        "Không có employee active nào tham gia hơn 1 project."
                );
            } else {
                for (Employee employee : employees) {

                    System.out.println(
                            "Employee: " + employee.getFullName()
                    );

                    System.out.println(
                            "Email: " + employee.getEmail()
                    );

                    System.out.println(
                            "Active: " + employee.isActive()
                    );

                    System.out.println("--------------------------------");
                }
            }

        } finally {
            JPAUtil.close();
        }
    }
}