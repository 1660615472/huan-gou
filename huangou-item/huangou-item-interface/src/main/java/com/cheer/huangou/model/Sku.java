package com.cheer.huangou.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author zhangjie
 * @title:SKU 商品的共有属性
 * @data
 */

@Data
@Table(name = "tb_sku")
public class Sku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long spuId;
    private String title;
    private String images;
    private Long price;
    private String ownSpec;// 商品特殊规格的键值对
    private String indexes;// 商品特殊规格的下标
    private Boolean enable;// 是否有效，逻辑删除用
    private Date createTime;// 创建时间
    private Date lastUpdateTime;// 最后修改时间
    @Transient //这个注解表示数据库没有的字段 用于临时添加设置属性
    private Long stock;// 库存
}
