package com.hsf302.student.controller;

import com.hsf302.student.entity.Student;
import com.hsf302.student.entity.Major;
import com.hsf302.student.repository.projection.MajorStatistics;
import com.hsf302.student.service.StudentService;
import com.hsf302.student.service.MajorService;
import com.hsf302.student.validation.StudentFormChecks;
import jakarta.validation.groups.Default;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final MessageSource messageSource;
    private final MajorService majorService;

    public StudentController(StudentService studentService, MessageSource messageSource, MajorService majorService) {
        this.studentService = studentService;
        this.messageSource = messageSource;
        this.majorService = majorService;
    }

    @ModelAttribute("majorOptions")
    public List<Major> majorOptions() {
        return majorService.findAll();
    }

    /** Tên ngành theo ngôn ngữ hiện tại, lấy từ database. */
    @ModelAttribute("majors")
    public Map<String, String> majors() {
        Map<String, String> majors = new LinkedHashMap<>();
        for (Major major : majorService.findAll()) {
            majors.put(major.getCode(), major.getLocalizedName(LocaleContextHolder.getLocale().getLanguage()));
        }
        return majors;
    }

    // TODO 4: GET /students?keyword=...
    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String major,
                       @RequestParam(defaultValue = "id") String sort,
                       @RequestParam(defaultValue = "asc") String dir,
                       @RequestParam(defaultValue = "0", name = "page") int pageNumber,
                       Model model) {
        List<String> allowedSorts = List.of(
                "id", "studentCode", "fullName", "email", "dateOfBirth", "major", "gpa", "active"
        );
        String sortProperty = allowedSorts.contains(sort) ? sort : "id";
        String sortDirection = "desc".equals(dir) ? "desc" : "asc";
        String sortPath = "major".equals(sortProperty) ? "major.code" : sortProperty;
        Sort ordering = Sort.by(Sort.Direction.fromString(sortDirection), sortPath);
        if (!"id".equals(sortProperty)) {
            ordering = ordering.and(Sort.by("id"));
        }
        Page<Student> page = studentService.search(
                keyword, major, PageRequest.of(Math.max(0, pageNumber), 3, ordering)
        );
        if (page.getTotalPages() > 0 && page.getNumber() >= page.getTotalPages()) {
            page = studentService.search(
                    keyword, major, PageRequest.of(page.getTotalPages() - 1, 3, ordering)
            );
        }
        // Thống kê vẫn tính trên toàn bộ kết quả lọc, không chỉ trang hiện tại.
        List<Student> matchingStudents = studentService.search(keyword, major, ordering);

        // Phần G - Bài 6 và 8: Dùng kết quả COUNT/AVG của JPQL để tạo Map cho view.
        Map<String, Long> stats = new LinkedHashMap<>();
        Map<String, Double> avgGpaByMajor = new LinkedHashMap<>();
        for (String majorCode : majors().keySet()) {
            stats.put(majorCode, 0L);
            avgGpaByMajor.put(majorCode, 0.0);
        }
        for (MajorStatistics summary : studentService.statisticsByMajor(keyword, major)) {
            stats.put(summary.getMajor(), summary.getStudentCount());
            avgGpaByMajor.put(summary.getMajor(),
                    summary.getAverageGpa() == null ? 0.0 : summary.getAverageGpa());
        }

        model.addAttribute("students", page.getContent());
        model.addAttribute("stats", stats);
        model.addAttribute("avgGpaByMajor", avgGpaByMajor);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("major", major);
        model.addAttribute("sort", sortProperty);
        model.addAttribute("dir", sortDirection);
        model.addAttribute("totalStudents", page.getTotalElements());
        model.addAttribute("avgGpa", studentService.averageGpa(matchingStudents));
        model.addAttribute("activeStudents", matchingStudents.stream().filter(Student::isActive).count());
        model.addAttribute("activeMenu", "list");

        return "students/list";
    }

    // TODO 5: GET /students/{id}
    @GetMapping("/{id}")
    public String detail(
            @PathVariable Long id,
            Model model,
            RedirectAttributes ra
    ) {
        return studentService.findById(id)
                .map(student -> {
                    model.addAttribute("student", student);
                    model.addAttribute("activeMenu", "list");
                    return "students/detail";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute(
                            "errorMessage",
                            message("student.notFound", id)
                    );
                    return "redirect:/students";
                });
    }

    // TODO 6: GET /students/new
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("activeMenu", "new");
        return "students/form";
    }

    // TODO 6: GET /students/{id}/edit
    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable Long id,
            Model model,
            RedirectAttributes ra
    ) {
        return studentService.findById(id)
                .map(student -> {
                    model.addAttribute("student", student);
                    model.addAttribute("activeMenu", "new");
                    return "students/form";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute(
                            "errorMessage",
                            message("student.notFound", id)
                    );
                    return "redirect:/students";
                });
    }

    // TODO 7: POST /students/save
    @PostMapping("/save")
    public String save(
            @Validated({Default.class, StudentFormChecks.class}) @ModelAttribute("student") Student student,
            BindingResult result,
            RedirectAttributes ra
    ) {
        if (result.hasErrors()) {
            return "students/form";
        }

        boolean isNewStudent = student.getId() == null;
        String fullName = student.getFullName();

        studentService.save(student);

        ra.addFlashAttribute(
                "successMessage",
                message(isNewStudent ? "student.created" : "student.updated", fullName)
        );

        return "redirect:/students";
    }

    // Cho sẵn
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        studentService.deleteById(id);
        ra.addFlashAttribute(
                "successMessage",
                message("student.deleted", id)
        );
        return "redirect:/students";
    }

    private String message(String code, Object... arguments) {
        return messageSource.getMessage(code, arguments, LocaleContextHolder.getLocale());
    }
}
