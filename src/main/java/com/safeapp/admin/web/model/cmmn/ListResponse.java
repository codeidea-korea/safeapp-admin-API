package com.safeapp.admin.web.model.cmmn;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ListResponse<T> {

    private long count = 0;
    private List<T> list = new ArrayList<>();
    
    private Page page;
    
    class Page {
        private long totalDataCnt;
        private long totalPages;
        private boolean isLastPage;
        private boolean isFirstPage;
        private long requestPage;
        private long requestSize;

        Page(BfPage bfPage) {
            totalDataCnt = count;
            totalPages = count / bfPage.getPageSize() + (count % bfPage.getPageSize() == 0 ? 0 : 1);
            requestPage = bfPage.getPageNo();
            requestSize = bfPage.getPageSize();
            isFirstPage = bfPage.getPageNo() == 1;
            isLastPage = bfPage.getPageNo() == totalPages;
        }
    }
    
    public ListResponse(long count, List<T> list, BfPage page) {
        this.list = list;
        this.count = count;
        
        this.page = new Page(page);
    }

}