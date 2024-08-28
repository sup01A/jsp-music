package com.example.jspmusic.vo;

public class Page {
    private int fromIndex;
    private int pageSize;

    public Page(int fromIndex, int pageSize) {
        this.fromIndex = fromIndex;
        this.pageSize = pageSize;
    }

    public int getFromIndex() {
        return fromIndex;
    }

    public void setFromIndex(int fromIndex) {
        this.fromIndex = fromIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
