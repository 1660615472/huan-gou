package com.cheer.huangou.model;

import lombok.Data;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;


/**
 * @author zhangjie
 * @title:商品列表信息拓展
 * @data
 */

@Data
public class SpuBo extends Spu{
    public SpuBo(){

    }
    String cname;//商品分类名称
    String bname;//品牌名称
    @Transient
    private SpuDetail spuDetail; //商品详情

    @Transient
    private List<Sku> skus; //SKU列表（商品特有属性）


    public SpuBo(Long brandId, Long cid1, Long cid2, Long cid3, String title, String subTitle, Boolean saleable, Boolean valid, Date createTime, Date lastUpdateTime) {
        super(brandId, cid1, cid2, cid3, title, subTitle, saleable, valid, createTime, lastUpdateTime);
    }

    public SpuBo(String cname, String bname, SpuDetail spuDetail, List<Sku> skus) {
        this.cname = cname;
        this.bname = bname;
        this.spuDetail = spuDetail;
        this.skus = skus;
    }
}
