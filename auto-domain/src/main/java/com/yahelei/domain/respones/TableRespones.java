package com.yahelei.domain.respones;

import java.util.List;

public class TableRespones {
    public Integer total;
    public List<?> rows;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public TableRespones(Integer total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "PromoData{" +
                "total=" + total +
                ", rows=" + rows +
                '}';
    }
}
