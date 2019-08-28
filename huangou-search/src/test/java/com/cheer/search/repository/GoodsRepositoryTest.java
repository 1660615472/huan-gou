package com.cheer.search.repository;
import com.cheer.huangou.common.vo.PageResult;
import com.cheer.huangou.model.SpuBo;
import com.cheer.search.client.GoodsClient;
import com.cheer.search.pojo.Goods;
import com.cheer.search.service.impl.SearchService;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangjie
 * @title:
 * @data
 */
@Log4j2
@RunWith (SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest {
    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template; //可以创建索引库啥的

    @Autowired
    private SearchService searchService;

    //测试创建索引库
    @Test
    public void testCreateIndex() {
        template.createIndex ( Goods.class );
        template.putMapping ( Goods.class ); //添加映射
    }

    //导入商品信息到索引库
    @Test
    public void loadData() throws Exception {
        int page = 1;//从第一页开始
        int rows = 100; //每页大小 一次查询100条
        int size = 0;
        ArrayList<SpuBo> list = new ArrayList<> ();
        do {

            //批量查询spu信息
            PageResult<SpuBo> result = goodsClient.querySpuByPage ( page, rows, null, true, null, true );
            List<SpuBo> spuBos = result.getItems ();
            size = spuBos.size ();
            page++;
            list.addAll ( spuBos );
        } while (size == 100);

        //创建GOODS集合
        List<Goods> goodsList = new ArrayList<> ();
        //遍历spu
        for (SpuBo spu : list) {
            log.debug ( "插入成功"+ spu.getSkus () );

            Goods goods = this.searchService.buildGoods ( spu );
            goodsList.add ( goods );
        }

        this.goodsRepository.saveAll ( goodsList );

    }

}


