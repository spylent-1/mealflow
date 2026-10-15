package io.github.spylent1.mealflow;

import io.github.spylent1.mealflow.mapper.EmployeeMapper;
import io.github.spylent1.mealflow.properties.JwtProperties;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProjectMappingTest {
    @Test
    void migratedMapperXmlBindsTheEmployeeMapper() throws Exception {
        Configuration configuration = new Configuration();
        try (InputStream xml = getClass().getResourceAsStream("/mapper/EmployeeMapper.xml")) {
            assertNotNull(xml);
            new XMLMapperBuilder(xml, configuration, "mapper/EmployeeMapper.xml",
                    configuration.getSqlFragments()).parse();
        }
        assertTrue(configuration.hasMapper(EmployeeMapper.class));
    }

    @Test
    void jwtSettingsBindUsingTheRenamedPrefix() {
        String prefix = JwtProperties.class.getAnnotation(ConfigurationProperties.class).prefix();
        assertEquals("mealflow.jwt", prefix);
        Map<String, Object> values = new HashMap<>();
        values.put(prefix + ".admin-secret-key", "test-only-secret");
        values.put(prefix + ".admin-ttl", "7200000");
        values.put(prefix + ".admin-token-name", "token");
        JwtProperties properties = new Binder(new MapConfigurationPropertySource(values))
                .bind(prefix, JwtProperties.class).get();
        assertEquals("test-only-secret", properties.getAdminSecretKey());
        assertEquals(7200000, properties.getAdminTtl());
        assertEquals("token", properties.getAdminTokenName());
    }
}
