package fu.de200049;

import fu.de200049.dao.EmployeeDAO;

public class Main {

    public static void main(String[] args) {

        EmployeeDAO employeeDAO = new EmployeeDAO();
        employeeDAO.getProjectStatistics();
    }
}