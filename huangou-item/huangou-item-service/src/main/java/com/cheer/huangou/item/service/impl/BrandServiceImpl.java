package com.cheer.huangou.item.service.impl;

import com.cheer.huangou.common.vo.PageResult;
import com.cheer.huangou.item.mapper.BrandMapper;
import com.cheer.huangou.item.service.BrandService;
import com.cheer.huangou.model.Brand;
import com.cheer.huangou.model.Category;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author zhangjie
 * @title:
 * @data
 */

@Log4j2
@Transactional
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;


    /**
     *
     * @param page 一共多少页
     * @param rows 一页多少行
     * @param sortBy 按什么排序
     * @param desc 升序还是降序
     * @param key 查询条件
     * @return
     */
    @Override
    public PageResult<Brand> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key) {

        //使用分页助手分页
        PageHelper.startPage ( page, rows );

        //过滤查询条件
        Example example = new Example ( Brand.class ); //利用反射得到传入的字节码类中对应的数据库对象信息

        if (StringUtils.isNotBlank ( key )) {
            //构造查询条件 select * from tb_brand where name like %key% or little == key order by id desc
            example.createCriteria ().andLike ( "name", "%" + key + "%" ).orEqualTo ( "letter", key.toUpperCase () );


        }

        // 排序(根据什么排序，并追加sql语句)
        if (StringUtils.isNotBlank ( sortBy )) {
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause ( orderByClause );
        }

        // 查询
        Page<Brand> pageInfo = (Page<Brand>) this.brandMapper.selectByExample ( example );
        //总条数            //当前页
        return new PageResult<> ( pageInfo.getTotal (), pageInfo );
    }


    @Override
    public void saveBrand(Brand brand, List<Long> cids) {
        // 新增品牌信息
        this.brandMapper.insertSelective ( brand ); //insertSelective() 有选择性的新增，模板类中某个字段为null就不新增该字段

        // 新增品牌和分类中间表信息
        for (Long cid : cids) {
            this.brandMapper.insertCategoryBrand ( cid, brand.getId () );
        }
    }

    //根据品牌id查找商品种类id
    @Override
    public List<Category> queryByBrandId(Long bid) {
        List<Category> categories = this.brandMapper.queryByBrandId ( bid );
        return categories;
    }


    //跟新品牌方法
    @Override
    public void updateBread(Brand brand, List<Long> cids) {
        //删除原来中间表的数据
        this.brandMapper.deleteByBrandIdInCategoryBrand ( brand.getId () );
        //跟新修改信息
         this.brandMapper.updateByPrimaryKeySelective ( brand);

         //跟新修改后的品牌和中间表信息
        for(Long cid : cids){
            this.brandMapper.insertCategoryBrand ( cid,brand.getId () );
        }

    }


    //删除品牌方法
    @Override
    public void deleteBrand(Long brandId) {
        this.brandMapper.deleteByPrimaryKey ( brandId );
    }

    //商品列表新增商品功能
    //根据种类id查询品牌集合
    @Override
    public List<Brand> queryBrandByCid(Long cid) {
       return this.brandMapper.queryBrandByCid ( cid );
    }

    /**
     * 根据品牌id查询品牌
     * @param bid
     * @return
     */
    @Override
    public Brand queryBrandIdbyBid(Long bid) {

      return this.brandMapper.queryBrandByBid ( bid );
    }
}
