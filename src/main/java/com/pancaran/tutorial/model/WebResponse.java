package com.pancaran.tutorial.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse<T> {
    private String message;
    private T data;
    private PageResponse pagination;
    private String errors;
}
