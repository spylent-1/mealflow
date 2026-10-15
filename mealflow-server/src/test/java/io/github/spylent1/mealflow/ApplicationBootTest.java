package io.github.spylent1.mealflow;

import io.github.spylent1.mealflow.controller.admin.EmployeeController;
import io.github.spylent1.mealflow.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {
        "DB_PASSWORD=test-only-no-live-database",
        "JWT_ADMIN_SECRET=mealflow-test-secret-not-used-outside-tests"
})
class ApplicationBootTest {
    @Autowired private ApplicationContext context;

    @Test
    void migratedApplicationDiscoversControllersAndMappers() {
        assertNotNull(context.getBean(EmployeeController.class));
        assertNotNull(context.getBean(EmployeeMapper.class));
    }
}
