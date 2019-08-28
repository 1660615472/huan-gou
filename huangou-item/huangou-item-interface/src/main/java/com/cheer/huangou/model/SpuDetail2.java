package com.cheer.huangou.model;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author zhangjie
 * @title:这是用于前台规格搜索的
 * @data
 */

@Table(name = "tb_spu_detail2")
@Data
public class SpuDetail2 {
    @Id
    private Long spuId;

    //商品描述
    private String description;

    //通用规格参数数据
    private String genericSpec;

    //特殊规格参数数据
    private String specialSpec;

    //包装清单
    private String packingList;

    //售后服务
    private String afterService;
}
