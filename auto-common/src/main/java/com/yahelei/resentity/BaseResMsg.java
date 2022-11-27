package com.yahelei.resentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BaseResMsg implements Serializable {

    protected Integer code;
    protected String msg;
    private Object data;

}
