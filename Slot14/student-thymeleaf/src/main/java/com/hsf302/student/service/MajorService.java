package com.hsf302.student.service;

import com.hsf302.student.entity.Major;
import com.hsf302.student.repository.MajorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MajorService {

    private final MajorRepository majorRepository;

    public MajorService(MajorRepository majorRepository) {
        this.majorRepository = majorRepository;
    }

    public List<Major> findAll() {
        return majorRepository.findAll(Sort.by("code"));
    }

    public Optional<Major> findById(Long id) {
        return majorRepository.findById(id);
    }
}
