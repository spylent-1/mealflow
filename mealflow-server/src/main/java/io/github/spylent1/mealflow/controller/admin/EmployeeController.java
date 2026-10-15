package io.github.spylent1.mealflow.controller.admin;

import io.github.spylent1.mealflow.constant.JwtClaimsConstant;
import io.github.spylent1.mealflow.dto.EmployeeLoginDTO;
import io.github.spylent1.mealflow.entity.Employee;
import io.github.spylent1.mealflow.properties.JwtProperties;
import io.github.spylent1.mealflow.result.Result;
import io.github.spylent1.mealflow.service.EmployeeService;
import io.github.spylent1.mealflow.utils.JwtUtil;
import io.github.spylent1.mealflow.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     *
     *
     * @param employeeLoginDTO
     * @return
     */

    @PostMapping("/login")
    @ResponseBody
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录请求");

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

}
