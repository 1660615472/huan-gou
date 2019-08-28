package com.cheer.huangou.item.mapper;

import com.cheer.huangou.model.SpuDetail;
import org.apache.ibatis.annotations.Insert;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author zhangjie
 * @title:
 * @data
 */
@org.apache.ibatis.annotations.Mapper
public interface SpuDetailMapper extends Mapper<SpuDetail> {

/*    @Insert({"insert into tb_spu_detail values #{spu_id},#{description},#{generic_spec},#{special_spec},#{packing_list},#{after_service}"})
    void insertSpuDetail(SpuDetail spuDetail);*/


}
