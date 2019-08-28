package com.cheer.huangou.model;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author zhangjie
 * @title:库存实体类
 * @data
 */
@Data
@Table(name = "tb_stock")
public class Stock {
    @Id
    private Long skuId;
    private Integer seckillStock;//秒杀可用库存
    private Integer seckillTotal;//已秒杀数量
    private Long stock;//正常库存
}
