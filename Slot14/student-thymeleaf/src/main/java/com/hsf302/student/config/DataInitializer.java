package com.hsf302.student.config;

import com.hsf302.student.entity.Student;
import com.hsf302.student.entity.Major;
import com.hsf302.student.repository.MajorRepository;
import com.hsf302.student.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final MajorRepository majorRepository;

    public DataInitializer(StudentRepository studentRepository, MajorRepository majorRepository) {
        this.studentRepository = studentRepository;
        this.majorRepository = majorRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Major se = findOrCreateMajor("SE", "Kỹ thuật phần mềm", "Software Engineering");
        Major ai = findOrCreateMajor("AI", "Trí tuệ nhân tạo", "Artificial Intelligence");
        Major ia = findOrCreateMajor("IA", "An toàn thông tin", "Information Assurance");
        Major gd = findOrCreateMajor("GD", "Thiết kế đồ họa", "Graphic Design");

        if (studentRepository.count() > 0) {
            return;
        }
        studentRepository.saveAll(List.of(
                new Student(null, "SE180001", "Nguyễn Văn An",  "annv@fpt.edu.vn",    LocalDate.of(2004, 5, 12),  8.75, se, true),
                new Student(null, "SE180002", "Trần Thị Bình",  "binhtt@fpt.edu.vn",  LocalDate.of(2004, 9, 3),   9.20, ai, true),
                new Student(null, "SS180003", "Lê Hoàng Cường", "cuonglh@fpt.edu.vn", LocalDate.of(2003, 1, 25),  6.80, ia, true),
                new Student(null, "SA180004", "Phạm Minh Dũng", "dungpm@fpt.edu.vn",  LocalDate.of(2004, 11, 30), 4.50, gd, false),
                new Student(null, "HE180005", "Võ Thị Em",      "emvt@fpt.edu.vn",    LocalDate.of(2005, 2, 14),  8.35, se, true)
        ));
    }

    private Major findOrCreateMajor(String code, String name, String nameEn) {
        return majorRepository.findByCodeIgnoreCase(code)
                .orElseGet(() -> majorRepository.save(new Major(code, name, nameEn)));
    }
}
