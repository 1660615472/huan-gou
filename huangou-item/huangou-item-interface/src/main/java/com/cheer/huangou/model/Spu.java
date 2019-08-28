package com.cheer.huangou.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author zhangjie
 * @title:商品SUP实体类
 *Sup商品的共有属性
 * @data
 */

@Data
@Table(name="tb_spu")
public class Spu {
    public Spu(){

    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//商品id
    private Long brandId;//品牌Id
    private Long cid1;//1级类目
    private Long cid2;//2级类目
    private Long cid3;//3级类目
    private String title;//标题
    private String subTitle;//子标题
    private Boolean saleable;//是否上架
    private Boolean valid;// 是否有效，逻辑删除用,0代表假删除 1代表未删除
    private Date createTime;// 创建时间
    private Date lastUpdateTime;// 最后修改时间

    public Spu(Long brandId, Long cid1, Long cid2, Long cid3, String title, String subTitle, Boolean saleable, Boolean valid, Date createTime, Date lastUpdateTime) {
    }

  /*  @Transient //这些是
    private String bname;
    @Transient
    private String cname;
    @Transient
    private List<Sku> skus;

    @Transient
    private SpuDetail spuDetail;
*/

}
