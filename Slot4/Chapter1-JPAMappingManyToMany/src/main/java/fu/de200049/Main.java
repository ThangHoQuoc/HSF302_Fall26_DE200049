package fu.de200049;

import fu.de200049.dao.DepartmentDAO;
import fu.de200049.pojo.Department;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        DepartmentDAO deptDAO = new DepartmentDAO();

        List<Department> departments = deptDAO.findAllWithEmployees();

        for (Department d : departments) {
            System.out.println(d.getName() + " - Employees: " + d.getEmployees().size());
        }
    }
}