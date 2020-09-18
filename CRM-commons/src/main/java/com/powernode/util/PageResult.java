package com.powernode.util;

import java.util.List;

/**
 * @Author AlanLin
 * @Description 分页结果类
 * @Date 2020/8/31
 */
public class PageResult<T> {
    private long total;
    private List<T> rows;
    private long pages;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }
}
