package com.inter.remittance.domain.entities;

import java.util.Set;

public record PageResult<T>(
        Set<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {}
