package fu.de200049;

import fu.de200049.dao.EmployeeDAO;
import fu.de200049.pojo.Employee;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        EmployeeDAO employeeDAO = new EmployeeDAO();
        List<Employee> employees = employeeDAO.findAll();

        if (!employees.isEmpty()) {
            Long employeeId = employees.get(0).getId();

            employeeDAO.deactivateEmployee(employeeId);

            System.out.println("Employee " + employeeId + " has been deactivated.");
        }
    }
}