package com.inter.remittance.ui.mappers;


import com.inter.remittance.domain.entities.PageResult;
import com.inter.remittance.ui.responses.PageResultResponse;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class PageResultResponseMapper {

    private PageResultResponseMapper() {
    }

    public static <D, R> PageResultResponse<R> toResponse(
            PageResult<D> pageResult,
            Function<D, R> itemMapper
    ) {
        if (pageResult == null) {
            return new PageResultResponse<>(
                    Collections.emptySet(),
                    0,
                    0,
                    0,
                    0
            );
        }

        Set<R> mappedContent = pageResult.content() == null
                ? Collections.emptySet()
                : pageResult.content().stream()
                  .map(itemMapper)
                  .collect(Collectors.toSet());

        return new PageResultResponse<>(
                mappedContent,
                pageResult.page(),
                pageResult.size(),
                pageResult.totalElements(),
                pageResult.totalPages()
        );
    }
}