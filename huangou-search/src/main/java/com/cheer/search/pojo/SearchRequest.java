package com.cheer.search.pojo;

/**
 * @author zhangjie
 * @title: 反馈给前台的搜索对象
 * @data
 */
public class SearchRequest {
    private String key;//搜索字段
    private Integer page;//当前页
    private static final int DEFAULT_SIZE = 20;//默认展示20个商品，每页大小
    private static final int DEFAULT_PAGE =1 ; //默认1页
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if (page == null){
            return DEFAULT_PAGE;
        }
        // 获取页码时做一些校验，不能小于1
        //Math.max返回最大值，如果page为负数则给予默认值
        return Math.max(DEFAULT_PAGE, page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return DEFAULT_SIZE;
    }


}
