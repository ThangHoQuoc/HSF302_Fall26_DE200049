package com.hsf302.student.controller;

import com.hsf302.student.entity.Student;
import com.hsf302.student.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /** Có sẵn trong Model của mọi request trong controller này → ${majors} */
    @ModelAttribute("majors")
    public Map<String, String> majors() {
        Map<String, String> majors = new LinkedHashMap<>();
        majors.put("SE", "Kỹ thuật phần mềm");
        majors.put("AI", "Trí tuệ nhân tạo");
        majors.put("IA", "An toàn thông tin");
        majors.put("GD", "Thiết kế đồ họa");
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
        Sort ordering = Sort.by(Sort.Direction.fromString(sortDirection), sortProperty);
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

        model.addAttribute("students", page.getContent());
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
                            "Không tìm thấy sinh viên có id = " + id
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
                            "Không tìm thấy sinh viên có id = " + id
                    );
                    return "redirect:/students";
                });
    }

    // TODO 7: POST /students/save
    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("student") Student student,
            BindingResult result,
            RedirectAttributes ra
    ) {
        if (studentService.isStudentCodeDuplicated(
                student.getStudentCode(),
                student.getId()
        )) {
            result.rejectValue(
                    "studentCode",
                    "duplicate",
                    "Mã sinh viên đã tồn tại"
            );
        }

        if (result.hasErrors()) {
            return "students/form";
        }

        boolean isNewStudent = student.getId() == null;
        String fullName = student.getFullName();

        studentService.save(student);

        String action = isNewStudent ? "Thêm" : "Cập nhật";
        ra.addFlashAttribute(
                "successMessage",
                action + " sinh viên " + fullName + " thành công!"
        );

        return "redirect:/students";
    }

    // Cho sẵn
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        studentService.deleteById(id);
        ra.addFlashAttribute(
                "successMessage",
                "Đã xóa sinh viên có id = " + id
        );
        return "redirect:/students";
    }
}
