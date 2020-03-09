package com.xiaojihua.test;

import com.xiaojihua.utils.UtilFuns;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class C07SpringDataRedisTest {

    @Autowired
    //泛型指定key和value的类型
    private RedisTemplate<String,String> template;

    @Test
    public void test1() throws IOException {
        //操作spring
        String value = template.opsForValue().get("genzTreeNodes4028a1c34ec2e5c8014ec2ec38cc0002");
        System.out.println(value);
    }


}
