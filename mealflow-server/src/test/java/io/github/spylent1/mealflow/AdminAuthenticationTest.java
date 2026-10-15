package io.github.spylent1.mealflow;

import io.github.spylent1.mealflow.constant.JwtClaimsConstant;
import io.github.spylent1.mealflow.interceptor.JwtTokenAdminInterceptor;
import io.github.spylent1.mealflow.properties.JwtProperties;
import io.github.spylent1.mealflow.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.method.HandlerMethod;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class AdminAuthenticationTest {
    private final JwtTokenAdminInterceptor interceptor = new JwtTokenAdminInterceptor();
    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();
    private final String secret = "mealflow-test-secret-never-used-in-production";
    private HandlerMethod handler;

    public void protectedEndpoint() { }

    @BeforeEach
    void setUp() throws Exception {
        JwtProperties properties = new JwtProperties();
        properties.setAdminSecretKey(secret);
        properties.setAdminTokenName("token");
        ReflectionTestUtils.setField(interceptor, "jwtProperties", properties);
        handler = new HandlerMethod(this, getClass().getMethod("protectedEndpoint"));
    }

    @Test
    void validTokenCanAccessManagementEndpoint() throws Exception {
        String token = JwtUtil.createJWT(secret, 60000,
                new HashMap<>(Collections.singletonMap(JwtClaimsConstant.EMP_ID, 7L)));
        request.addHeader("token", token);
        assertTrue(interceptor.preHandle(request, response, handler));
    }

    @Test
    void missingTokenReturnsUnauthorized() throws Exception {
        assertFalse(interceptor.preHandle(request, response, handler));
        assertEquals(401, response.getStatus());
    }

    @Test
    void tokenSignedWithAnotherKeyReturnsUnauthorized() throws Exception {
        request.addHeader("token", JwtUtil.createJWT("another-test-secret", 60000,
                new HashMap<>(Collections.singletonMap(JwtClaimsConstant.EMP_ID, 7L))));
        assertFalse(interceptor.preHandle(request, response, handler));
        assertEquals(401, response.getStatus());
    }
}
