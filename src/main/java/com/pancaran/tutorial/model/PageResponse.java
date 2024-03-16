package com.pancaran.tutorial.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse {
    private Integer currentPage;
    private Integer totalPage;
    private Integer size;
}
