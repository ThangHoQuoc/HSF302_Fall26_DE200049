package com.hsf302.student.repository;

import com.hsf302.student.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Long> {

    Optional<Major> findByCodeIgnoreCase(String code);
}
