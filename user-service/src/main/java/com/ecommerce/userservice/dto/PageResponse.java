package com.ecommerce.userservice.dto;

import java.util.List;

public class PageResponse<T> {

    private List<T> data;
    private PaginationResponse pagination;

    public PageResponse(List<T> data, PaginationResponse pagination) {
        this.data = data;
        this.pagination = pagination;
    }

    public List<T> getData() { return data; }
    public PaginationResponse getPagination() { return pagination; }
}