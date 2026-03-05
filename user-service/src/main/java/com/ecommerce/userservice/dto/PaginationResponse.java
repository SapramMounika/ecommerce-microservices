package com.ecommerce.userservice.dto;

public class PaginationResponse {

    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isFirst;
    private boolean isLast;

    public PaginationResponse(int currentPage,
                              int pageSize,
                              long totalElements,
                              int totalPages,
                              boolean isFirst,
                              boolean isLast) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.isFirst = isFirst;
        this.isLast = isLast;
    }

    public int getCurrentPage() { return currentPage; }
    public int getPageSize() { return pageSize; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
    public boolean isFirst() { return isFirst; }
    public boolean isLast() { return isLast; }
}