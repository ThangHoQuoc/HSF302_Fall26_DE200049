package com.hsf302.student.entity;

import com.hsf302.student.validation.StudentFormChecks;
import com.hsf302.student.validation.UniqueStudentCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Nationalized;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "Students")
@UniqueStudentCode(groups = StudentFormChecks.class)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO 1: Validation cho mã sinh viên
    @NotBlank(message = "{validation.code.required}")
    @Pattern(regexp = "^(SE|SS|SA|HE)\\d{6}$", message = "{validation.code.pattern}")
    @Column(name = "student_code", nullable = false, length = 8, unique = true)
    private String studentCode;

    // TODO 1: Validation cho họ tên
    @NotBlank(message = "{validation.name.required}")
    @Size(min = 2, max = 50, message = "{validation.name.size}")
    @Nationalized
    @Column(name = "full_name", nullable = false, length = 50)
    private String fullName;

    // TODO 1: Validation cho email
    @NotBlank(message = "{validation.email.required}")
    @Email(message = "{validation.email.format}")
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    // TODO 2: Validation cho ngày sinh
    @NotNull(message = "{validation.birthDate.required}")
    @Past(message = "{validation.birthDate.past}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    // TODO 2: Validation cho GPA
    @NotNull(message = "{validation.gpa.required}")
    @DecimalMin(value = "0.0", message = "{validation.gpa.min}")
    @DecimalMax(value = "10.0", message = "{validation.gpa.max}")
    @Column(name = "gpa", nullable = false)
    private Double gpa;

    // TODO 2: Validation cho ngành học
    @NotNull(message = "{validation.major.required}")
    @ManyToOne(optional = false)
    @JoinColumn(name = "major_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_Students_Majors"))
    private Major major;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    // JPA yêu cầu constructor không tham số
    public Student() {
    }

    public Student(Long id, String studentCode, String fullName, String email, LocalDate dateOfBirth, Double gpa, Major major, boolean active) {
        this.id = id;
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gpa = gpa;
        this.major = major;
        this.active = active;
    }

    // TODO 3: Tính tuổi từ ngày sinh
    public int getAge() {
        if (dateOfBirth == null) {
            return 0;
        }

        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    // TODO 3: Xếp loại theo GPA
    public String getRank() {
        if (gpa == null) {
            return "Chưa xếp loại";
        }

        if (gpa >= 9.0) {
            return "Xuất sắc";
        } else if (gpa >= 8.0) {
            return "Giỏi";
        } else if (gpa >= 6.5) {
            return "Khá";
        } else if (gpa >= 5.0) {
            return "Trung bình";
        } else {
            return "Yếu";
        }
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
