package org.example.medlink.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {
    private long total;
    private int page;
    private int size;
    private List<T> data;

    public PagedResponse(Page<T> pageData) {
        this.total = pageData.getTotalElements();
        this.page = pageData.getNumber() + 1;
        this.size = pageData.getSize();
        this.data = pageData.getContent();
    }

}
