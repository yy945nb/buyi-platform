package com.buyi.framework.commons;

import com.buyi.framework.commons.exception.CheckedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link CheckedException}
 */
class CheckedExceptionTest {

    @Test
    void badRequestShouldHave400Code() {
        CheckedException ex = CheckedException.badRequest("bad request error");
        assertEquals(400, ex.getCode());
        assertEquals("bad request error", ex.getMessage());
    }

    @Test
    void badRequestWithArgsShouldFormatMessage() {
        CheckedException ex = CheckedException.badRequest("order {0} not found", "AP1001");
        assertEquals(400, ex.getCode());
        assertTrue(ex.getMessage().contains("AP1001"));
    }

    @Test
    void notFoundShouldHave404Code() {
        CheckedException ex = CheckedException.notFound("resource not found");
        assertEquals(404, ex.getCode());
        assertEquals("resource not found", ex.getMessage());
    }

    @Test
    void forbiddenShouldHave403Code() {
        CheckedException ex = CheckedException.forbidden();
        assertEquals(403, ex.getCode());
        assertNotNull(ex.getMessage());
    }

    @Test
    void badRequestWithCustomCodeShouldHaveCorrectCode() {
        CheckedException ex = CheckedException.badRequest(422, "validation error");
        assertEquals(422, ex.getCode());
        assertEquals("validation error", ex.getMessage());
    }

    @Test
    void checkedExceptionShouldBeRuntimeException() {
        CheckedException ex = CheckedException.badRequest("test");
        assertInstanceOf(RuntimeException.class, ex);
    }
}
