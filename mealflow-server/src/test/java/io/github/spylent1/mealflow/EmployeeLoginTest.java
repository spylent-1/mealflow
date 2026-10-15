package io.github.spylent1.mealflow;

import io.github.spylent1.mealflow.constant.StatusConstant;
import io.github.spylent1.mealflow.dto.EmployeeLoginDTO;
import io.github.spylent1.mealflow.entity.Employee;
import io.github.spylent1.mealflow.exception.AccountLockedException;
import io.github.spylent1.mealflow.exception.AccountNotFoundException;
import io.github.spylent1.mealflow.exception.PasswordErrorException;
import io.github.spylent1.mealflow.mapper.EmployeeMapper;
import io.github.spylent1.mealflow.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeLoginTest {
    private final EmployeeMapper mapper = mock(EmployeeMapper.class);
    private final EmployeeServiceImpl service = new EmployeeServiceImpl();
    private final EmployeeLoginDTO request = new EmployeeLoginDTO();
    private final Employee employee = new Employee();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "employeeMapper", mapper);
        request.setUsername("operator");
        request.setPassword("test-password");
        employee.setId(7L);
        employee.setPassword("test-password");
        employee.setStatus(StatusConstant.ENABLE);
        when(mapper.getByUsername("operator")).thenReturn(employee);
    }

    @Test
    void enabledEmployeeCanLogin() {
        assertSame(employee, service.login(request));
    }

    @Test
    void unknownEmployeeIsRejected() {
        when(mapper.getByUsername("operator")).thenReturn(null);
        assertThrows(AccountNotFoundException.class, () -> service.login(request));
    }

    @Test
    void incorrectPasswordIsRejected() {
        request.setPassword("incorrect");
        assertThrows(PasswordErrorException.class, () -> service.login(request));
    }

    @Test
    void disabledEmployeeIsRejected() {
        employee.setStatus(StatusConstant.DISABLE);
        assertThrows(AccountLockedException.class, () -> service.login(request));
    }
}
