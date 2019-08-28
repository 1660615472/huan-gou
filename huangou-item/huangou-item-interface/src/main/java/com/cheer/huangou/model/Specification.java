package com.cheer.huangou.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author zhangjie
 * @title:商品规格实体类
 * @data
 */

@Data
@Table(name = "tb_specification") //table注解标识实体类与数据库表名不想同时指定该数据库映射这个实体类
public class Specification {
    @Id
    private Long categoryId; //种类id
    private String specifications; //规格参数模板


}
