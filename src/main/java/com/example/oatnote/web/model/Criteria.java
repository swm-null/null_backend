package com.example.oatnote.web.model;

import java.util.Objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Criteria {
    public static final Integer DEFAULT_PAGE = 1;
    private static final Integer MIN_PAGE = 1;

    public static final Integer DEFAULT_LIMIT = 10;
    private static final Integer MIN_LIMIT = 1;
    private static final Integer MAX_LIMIT = 50;

    private final int page;
    private final int limit;

    public static Criteria of(Integer page, Integer limit, Integer total) {
        int validatedLimit = validateAndCalculateLimit(limit);
        return new Criteria(validateAndCalculatePage(page, validatedLimit, total), validatedLimit);
    }

    private static int validateAndCalculatePage(Integer page, Integer limit, Integer total) {
        int totalPage = Objects.equals(total, 0) ? 1 : (int)Math.ceil((double)total / limit);

        if (Objects.isNull(page)) {
            page = DEFAULT_PAGE;
        }
        if (page < MIN_PAGE) {
            page = MIN_PAGE;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        page -= 1; // start from 0
        return page;
    }

    private static int validateAndCalculateLimit(Integer limit) {
        if (Objects.isNull(limit)) {
            limit = DEFAULT_LIMIT;
        }
        if (limit < MIN_LIMIT) {
            limit = MIN_LIMIT;
        }
        if (limit > MAX_LIMIT) {
            limit = MAX_LIMIT;
        }
        return limit;
    }

    public int getCurrentPage() {
        return page + 1;
    }
}

