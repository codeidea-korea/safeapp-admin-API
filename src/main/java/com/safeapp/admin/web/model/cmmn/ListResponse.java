package com.safeapp.admin.web.model.cmmn;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class ListResponse<T> {

    private long totalDataCnt;
    private long totalPages;

    private long requestPage;
    private long requestSize;

    private boolean isLastPage;
    private boolean isFirstPage;

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public class Page {
        public Page(Pages pages) {
            totalDataCnt = count;
            totalPages = count / pages.getPageSize() + (count % pages.getPageSize() == 0 ? 0 : 1);

            requestPage = pages.getPageNo();
            requestSize = pages.getPageSize();

            isFirstPage = pages.getPageNo() == 1;
            isLastPage = pages.getPageNo() == totalPages;
        }
    }

    private long count = 0;
    private List<T> list = new ArrayList<>();

    private Page page;
    
    public ListResponse(long count, List<T> list, Pages pages) {
        this.count = count;
        this.list = list;

        this.page = new Page(pages);
    }

}