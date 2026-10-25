package com.hsf302.student.repository;

import com.hsf302.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // TODO 19.1: Tìm theo mã sinh viên HOẶC họ tên chứa từ khóa,
    // không phân biệt chữ hoa/thường, sắp xếp theo id tăng dần
    List<Student> findByStudentCodeContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrderByIdAsc(
            String code,
            String name
    );

    // Phần G - Bài 1: Tìm kiếm kết hợp sắp xếp động
    List<Student> findByStudentCodeContainingIgnoreCaseOrFullNameContainingIgnoreCase(
            String code, String name, Sort sort
    );

    // Phần G - Bài 2: Lọc ngành kết hợp tìm theo mã sinh viên hoặc họ tên
    @Query("""
            select s from Student s
            where s.major = :major
              and (:keyword = ''
                   or lower(s.studentCode) like lower(concat('%', :keyword, '%'))
                   or lower(s.fullName) like lower(concat('%', :keyword, '%')))
            """)
    List<Student> findByMajorAndKeyword(@Param("major") String major,
                                        @Param("keyword") String keyword,
                                        Sort sort);

    // TODO 19.2: Kiểm tra mã sinh viên đã tồn tại khi thêm mới
    boolean existsByStudentCodeIgnoreCase(String studentCode);

    // TODO 19.3: Kiểm tra mã sinh viên đã tồn tại ở sinh viên KHÁC khi cập nhật
    boolean existsByStudentCodeIgnoreCaseAndIdNot(String studentCode, Long id);
}
