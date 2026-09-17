package fu.de200049;

import fu.de200049.dao.DepartmentDAO;
import fu.de200049.pojo.Department;
import fu.de200049.pojo.Employee;
import fu.de200049.pojo.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        DepartmentDAO deptDAO = new DepartmentDAO();

        deptDAO.demonstrateNPlusOne();
    }
}