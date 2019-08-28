package com.cheer.search.repository;

import com.cheer.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author zhangjie
 * @title:文档操作,实现索引库的简单增删改查
 * @data
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {

}
