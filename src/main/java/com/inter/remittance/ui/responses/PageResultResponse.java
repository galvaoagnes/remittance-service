package com.inter.remittance.ui.responses;

import java.util.Set;

public record PageResultResponse<T>(
        Set<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
