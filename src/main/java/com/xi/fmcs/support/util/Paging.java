package com.xi.fmcs.support.util;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paging {
    private final int DEFAULT_PAGING_INDEX = 1;
    private final int DEFAULT_PAGING_SIZE = 10; // page count
    private final int DEFAULT_PAGING_COUNT = 10; //1 page per row count

    public int pageCount;
    public int pageSize;
    public int firstPageIndex;
    public int prevPageIndex;
    public int startPageIndex;
    public int pageIndex;
    public int endPageIndex;
    public int nextPageIndex;
    public int finalPageIndex;
    public int totalCount;

    public void makePaging() {
        if (this.pageCount == 0) this.pageCount = DEFAULT_PAGING_COUNT;
        if (this.pageIndex == 0) this.pageIndex = DEFAULT_PAGING_INDEX;
        if (this.pageSize == 0) this.pageSize = DEFAULT_PAGING_SIZE;
        if (this.totalCount == 0) return;


        int finalPage = (this.totalCount + (this.pageCount - 1)) / this.pageCount;
        if (this.pageIndex > finalPage) this.pageIndex = finalPage;

        if (this.pageIndex < 0 || this.pageIndex > finalPage) this.pageIndex = 1;

        Boolean isIndexwFirst = this.pageIndex == 1 ? true : false;
        Boolean isIndexwFinal = this.pageIndex == finalPage ? true : false;

        int startPage = ((this.pageIndex - 1) / this.pageSize) * this.pageSize + 1;
        int endPage = startPage + this.pageSize - 1;
        if (endPage > finalPage) {
            endPage = finalPage;
        }

        this.firstPageIndex = 1;

        if (isIndexwFirst) {
            this.prevPageIndex = 1;
        } else {
            this.prevPageIndex = (((this.pageIndex - 1) < 1 ? 1 : (this.pageIndex - 1)));
        }

        this.startPageIndex = startPage;
        this.endPageIndex = endPage;

        if (isIndexwFinal) {
            this.nextPageIndex = finalPage;
        } else {
            this.nextPageIndex = (((this.pageIndex + 1) > finalPage ? finalPage : (this.pageIndex + 1)));
        }

        this.finalPageIndex = finalPage;
    }

}
