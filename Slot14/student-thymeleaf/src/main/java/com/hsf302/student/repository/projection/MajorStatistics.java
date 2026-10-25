package com.hsf302.student.repository.projection;

/** Kết quả COUNT và AVG theo ngành từ truy vấn JPQL. */
public interface MajorStatistics {

    String getMajor();

    Long getStudentCount();

    Double getAverageGpa();
}
