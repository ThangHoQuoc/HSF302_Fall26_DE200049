package com.se20b01.mvc.service;

import com.se20b01.mvc.dto.DeviceDTO;
import com.se20b01.mvc.entity.Device;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface DeviceService {
    List<Device> getAllDevices();
    List<Device> searchByName(String keyword);
    Device getById(Integer id);
    void validateBusinessRules(DeviceDTO deviceDTO, BindingResult bindingResult);
    void createDevice(DeviceDTO deviceDTO);
    void deleteDevice(Integer id);
}