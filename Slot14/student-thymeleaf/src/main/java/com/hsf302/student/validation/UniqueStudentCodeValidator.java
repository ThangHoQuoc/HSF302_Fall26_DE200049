package com.hsf302.student.validation;

import com.hsf302.student.entity.Student;
import com.hsf302.student.repository.StudentRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueStudentCodeValidator implements ConstraintValidator<UniqueStudentCode, Student> {

    private final StudentRepository studentRepository;

    public UniqueStudentCodeValidator(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public boolean isValid(Student student, ConstraintValidatorContext context) {
        if (student == null || student.getStudentCode() == null || student.getStudentCode().isBlank()) {
            return true; // Để @NotBlank xử lý mã bị bỏ trống.
        }

        String code = student.getStudentCode().trim();
        boolean duplicated = student.getId() == null
                ? studentRepository.existsByStudentCodeIgnoreCase(code)
                : studentRepository.existsByStudentCodeIgnoreCaseAndIdNot(code, student.getId());

        if (!duplicated) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("studentCode")
                .addConstraintViolation();
        return false;
    }
}
