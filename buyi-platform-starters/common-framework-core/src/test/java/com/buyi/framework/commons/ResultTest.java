package com.buyi.framework.commons;

import com.buyi.framework.commons.entity.Result;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Result}
 */
class ResultTest {

    @Test
    void successResultShouldHaveSuccessCode() {
        List<String> data = List.of("item1", "item2");
        Result<List<String>> result = Result.success(data);
        assertEquals(Result.SUCCESS_CODE, result.getCode());
        assertTrue(result.isSuccessful());
        assertEquals(data, result.getData());
    }

    @Test
    void successResultWithoutDataShouldHaveDefaultMessage() {
        Result<Void> result = Result.success();
        assertEquals(Result.SUCCESS_CODE, result.getCode());
        assertTrue(result.isSuccessful());
        assertEquals(Result.OPERATION_SUCCESS, result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void failResultShouldHaveNonSuccessCode() {
        Result<String> result = Result.fail("error occurred");
        assertFalse(result.isSuccessful());
        assertEquals("error occurred", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void failResultWithCodeShouldHaveCorrectCode() {
        Result<Void> result = Result.fail(500, "internal error");
        assertEquals(500, result.getCode());
        assertFalse(result.isSuccessful());
        assertEquals("internal error", result.getMessage());
    }

    @Test
    void failResultWithThrowableShouldContainExceptionMessage() {
        RuntimeException ex = new RuntimeException("test exception");
        Result<Void> result = Result.fail(ex);
        assertFalse(result.isSuccessful());
        assertEquals("test exception", result.getMessage());
    }

    @Test
    void okResultShouldHaveSuccessCodeAndData() {
        List<String> data = List.of("item");
        Result<List<String>> result = Result.ok(data);
        assertEquals(Result.SUCCESS_CODE, result.getCode());
        assertTrue(result.isSuccessful());
        assertEquals(data, result.getData());
    }

    @Test
    void successResultWithCustomMessageAndData() {
        List<String> data = List.of("a", "b");
        Result<List<String>> result = Result.success("custom message", data);
        assertEquals(Result.SUCCESS_CODE, result.getCode());
        assertTrue(result.isSuccessful());
        assertEquals("custom message", result.getMessage());
        assertEquals(data, result.getData());
    }

    @Test
    void resultTimestampShouldBePositive() {
        Result<Void> result = Result.success();
        assertTrue(result.getTimestamp() > 0);
    }
}
