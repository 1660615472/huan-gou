package com.cheer.huangou.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author zhangjie
 * @title:品牌实体类
 * @data
 */

@Data
@Table(name ="tb_brand")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//商品ID自增长
    private Long id;
    private String name;// 品牌名称
    private String image;// 品牌图片
    private Character letter;//品牌对应的首字母


}
