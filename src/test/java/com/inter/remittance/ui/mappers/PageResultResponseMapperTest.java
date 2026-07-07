package com.inter.remittance.ui.mappers;

import com.inter.remittance.domain.entities.PageResult;
import com.inter.remittance.ui.responses.PageResultResponse;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PageResultResponseMapperTest {

    @Test
    void shouldMapPageResultApplyingItemMapper() {
        PageResult<String> pageResult = new PageResult<>(Set.of("a", "bb", "ccc"), 0, 10, 3, 1);

        PageResultResponse<Integer> response = PageResultResponseMapper.toResponse(pageResult, String::length);

        assertNotNull(response);
        assertEquals(Set.of(1, 2, 3), response.content());
        assertEquals(0, response.page());
        assertEquals(10, response.size());
        assertEquals(3, response.totalElements());
        assertEquals(1, response.totalPages());
    }

    @Test
    void shouldPreservePaginationMetadata() {
        PageResult<String> pageResult = new PageResult<>(Set.of("item"), 3, 20, 75, 4);

        PageResultResponse<String> response = PageResultResponseMapper.toResponse(pageResult, s -> s);

        assertEquals(3, response.page());
        assertEquals(20, response.size());
        assertEquals(75, response.totalElements());
        assertEquals(4, response.totalPages());
    }

    @Test
    void shouldReturnEmptyResponseWhenPageResultIsNull() {
        PageResultResponse<String> response = PageResultResponseMapper.toResponse(null, Object::toString);

        assertNotNull(response);
        assertTrue(response.content().isEmpty());
        assertEquals(0, response.page());
        assertEquals(0, response.size());
        assertEquals(0, response.totalElements());
        assertEquals(0, response.totalPages());
    }

    @Test
    void shouldReturnEmptyContentWhenPageResultContentIsNull() {
        PageResult<String> pageResult = new PageResult<>(null, 0, 10, 0, 0);

        PageResultResponse<String> response = PageResultResponseMapper.toResponse(pageResult, s -> s);

        assertNotNull(response);
        assertTrue(response.content().isEmpty());
    }

    @Test
    void shouldReturnEmptyContentWhenPageResultContentIsEmpty() {
        PageResult<String> pageResult = new PageResult<>(Set.of(), 0, 10, 0, 0);

        PageResultResponse<String> response = PageResultResponseMapper.toResponse(pageResult, s -> s);

        assertNotNull(response);
        assertTrue(response.content().isEmpty());
    }

    @Test
    void shouldApplyTransformationFunctionToEachItem() {
        PageResult<Integer> pageResult = new PageResult<>(Set.of(1, 2, 3), 0, 10, 3, 1);

        PageResultResponse<Integer> response = PageResultResponseMapper.toResponse(pageResult, n -> n * 2);

        assertEquals(Set.of(2, 4, 6), response.content());
    }
}

