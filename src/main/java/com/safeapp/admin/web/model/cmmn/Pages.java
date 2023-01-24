package com.safeapp.admin.web.model.cmmn;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class Pages {

    private int pageNo = 1;
    private int pageSize = 10;

    public Pages(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return (pageNo - 1) * pageSize;
    }

    public Pageable generatePageable() {
        return PageRequest.of(pageNo - 1, pageSize);
    }

}