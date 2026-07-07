package com.inter.remittance.domain.entities;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PageResultTest {

    @Test
    void shouldStoreAllPageMetadataAndContent() {
        PageResult<String> pageResult = new PageResult<>(
                Set.of("a", "b"),
                2,
                20,
                40,
                2
        );

        assertEquals(Set.of("a", "b"), pageResult.content());
        assertEquals(2, pageResult.page());
        assertEquals(20, pageResult.size());
        assertEquals(40, pageResult.totalElements());
        assertEquals(2, pageResult.totalPages());
    }
}

