package com.cheer.huangou.item.mapper;

import com.cheer.huangou.model.Brand;
import com.cheer.huangou.model.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhangjie
 * @title:
 * @data
 */
public interface BrandMapper extends Mapper<Brand> {


    /**
     * 新增商品分类和品牌中间表数据
     * @param cid 商品分类id
     * @param bid 品牌id
     * @return
     */
    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertCategoryBrand(@Param ( "cid" ) Long cid,@Param ( "bid" ) Long bid); //方法参数有多个参数时指定参数名

    //根据品牌id查询商品分类信息
    @Select ( "select * from tb_category where id in (select category_id from tb_category_brand where brand_id=#{bid})" )
    List<Category>  queryByBrandId(Long bid);

    /**
     * 根据brandId 删除中间表相关数据
     * @param bid
     */
    @Delete ( "DELETE from tb_category_brand where brand_id = #{bid}" )
   void deleteByBrandIdInCategoryBrand(@Param ( "bid" ) Long bid);

    /**
     * 根据种类id查询商品集合
     * @param cid
     * @return
     */
     @Select ( "select * from tb_brand b inner join tb_category_brand cb on b.id = cb.brand_id where category_id = #{cid}" )
    List<Brand> queryBrandByCid(@Param ( "cid" ) Long cid);

    /**
     * 根据品牌id查询品牌
     * @param bid
     * @return
     */
    @Select ( "select * from tb_brand where id =#{bid}" )
     Brand queryBrandByBid(Long bid);
}
