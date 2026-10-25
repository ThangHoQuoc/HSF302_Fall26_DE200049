package com.hsf302.student.service;

import com.hsf302.student.entity.Student;
import com.hsf302.student.repository.StudentRepository;
import com.hsf302.student.repository.projection.MajorStatistics;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll(Sort.by("id"));
    }

    public List<Student> search(String keyword) {
        return search(keyword, Sort.by("id"));
    }

    public List<Student> search(String keyword, Sort sort) {
        if (keyword == null || keyword.isBlank()) {
            return studentRepository.findAll(sort);
        }
        String kw = keyword.trim();
        return studentRepository
                .findByStudentCodeContainingIgnoreCaseOrFullNameContainingIgnoreCase(kw, kw, sort);
    }

    public List<Student> search(String keyword, String major, Sort sort) {
        if (major == null || major.isBlank()) {
            return search(keyword, sort);
        }
        String kw = keyword == null ? "" : keyword.trim();
        return studentRepository.findByMajorAndKeyword(major.trim(), kw, sort);
    }

    public Page<Student> search(String keyword, String major, Pageable pageable) {
        String kw = keyword == null ? "" : keyword.trim();
        String majorCode = major == null ? "" : major.trim();
        return studentRepository.searchPage(majorCode, kw, pageable);
    }

    public List<MajorStatistics> statisticsByMajor(String keyword, String major) {
        String kw = keyword == null ? "" : keyword.trim();
        String majorCode = major == null ? "" : major.trim();
        return studentRepository.summarizeByMajor(kw, majorCode);
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    /** id == null → INSERT, id != null → UPDATE */
    @Transactional
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }

    public double averageGpa(List<Student> list) {
        return list.stream()
                .map(Student::getGpa)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }
}
