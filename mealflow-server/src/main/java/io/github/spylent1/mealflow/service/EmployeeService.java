package io.github.spylent1.mealflow.service;

import io.github.spylent1.mealflow.dto.EmployeeLoginDTO;
import io.github.spylent1.mealflow.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
