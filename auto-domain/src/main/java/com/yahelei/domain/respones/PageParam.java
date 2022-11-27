package com.yahelei.domain.respones;

import lombok.Data;

@Data
public class PageParam {
    private int page = 1;
    private int pageSize = 10;
}
