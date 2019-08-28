package com.cheer.search.client;

import com.cheer.huangou.model.Category;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author zhangjie
 * @title:
 * @data
 */
@RunWith (SpringRunner.class)
@SpringBootTest
public class CategoryClientTest {
    @Autowired
    private CategoryClient categoryClient;

    @Test
   public void queryCategoryByIds() {
        List<Category> categories = categoryClient.queryCategoryByIds ( Arrays.asList ( 1L, 2L, 3L ) );
        //断言查出3个规格
        Assert.assertEquals ( 3,categories.size () );
        for (Category category : categories) {
            System.out.println("category="+category);
        }
    }
}