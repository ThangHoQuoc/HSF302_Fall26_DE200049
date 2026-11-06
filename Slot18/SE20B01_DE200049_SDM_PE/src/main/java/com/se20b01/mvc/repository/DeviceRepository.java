package com.se20b01.mvc.repository;

import com.se20b01.mvc.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {

    // Q1.1 - Lấy danh sách mặc định sắp xếp giảm dần theo Installation Date
    List<Device> findAllByOrderByInstallationDateDesc();

    // Q1.2 - Tìm kiếm theo tên (chứa từ khóa, không phân biệt hoa thường) và sắp xếp giảm dần theo Installation Date
    List<Device> findByDeviceNameContainingIgnoreCaseOrderByInstallationDateDesc(String deviceName);

    // Q3 - Tìm thiết bị theo mã (phục vụ validate trùng code)
    Optional<Device> findByDeviceCode(String deviceCode);
}