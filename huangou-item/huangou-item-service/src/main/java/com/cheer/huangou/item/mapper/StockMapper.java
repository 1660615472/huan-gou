package com.cheer.huangou.item.mapper;
import com.cheer.huangou.model.Stock;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author zhangjie
 * @title:
 * @data
 */

public interface StockMapper extends Mapper<Stock>, SelectByIdListMapper<Stock,Long> {
}
