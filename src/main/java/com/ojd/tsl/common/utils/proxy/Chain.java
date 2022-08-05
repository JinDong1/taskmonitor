package com.ojd.tsl.common.utils.proxy;

import java.util.List;

public class Chain {
    private List<Point> list;
    private int index = -1;

    public Chain() {
    }

    public Chain(List<Point> list) {
        this.list = list;
    }

    public List<Point> getList() {
        return list;
    }

    public void setList(List<Point> list) {
        this.list = list;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * 索引自增1
     */
    public int incIndex() {
        return ++index;
    }

    /**
     * 索引还原
     */
    public void resetIndex() {
        this.index = -1;
    }
}
