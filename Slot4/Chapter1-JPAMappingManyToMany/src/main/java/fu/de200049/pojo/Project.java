package fu.de200049.pojo;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String projectCode;

    private String projectName;

    private BigDecimal budget;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToMany(mappedBy = "projects")
    private Set<Employee> employees = new HashSet<>();

    public Project() {
    }

    public Project(String projectCode, String projectName,
                   BigDecimal budget, LocalDate startDate,
                   LocalDate endDate) {
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.budget = budget;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", projectCode='" + projectCode + '\'' +
                ", projectName='" + projectName + '\'' +
                ", budget=" + budget +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    // Sử dụng projectCode làm khóa nghiệp vụ thay vì id,
    // vì id được database tự sinh và có thể chưa có giá trị trước khi persist Project.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        Project project = (Project) o;
        return projectCode != null && projectCode.equals(project.projectCode);
    }

    @Override
    public int hashCode() {
        return projectCode != null ? projectCode.hashCode() : 0;
    }
}