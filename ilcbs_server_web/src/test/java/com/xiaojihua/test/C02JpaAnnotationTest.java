package com.xiaojihua.test;

import com.xiaojihua.dao.UserDao;
import com.xiaojihua.domain.User;
import com.xiaojihua.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext.xml")
public class C02JpaAnnotationTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;

    /**
     * 测试spring data jap的DynamicUpdate，
     * 这个注解的作用是，加载到实体类上以后，在更新字段的时候只更新变化的列
     * 注意是更新变化的列，也就是需要先从数据库中加载到程序中，然后进行更改
     * 这样生成的sql语句就只包含更改的列，但是如果没有这个注解，那么生成的
     * SQL语句就会包含所有列。
     *
     * 如果仅仅是单纯new一个user对象然后设置了ID属性，然后更改其中一列还是会发
     * 所有列的SQL语句
     *
     * 也就是说对象必须由session管理才行。
     */
    @Test
    public void testDynamicUpdate(){

        User user = userService.get("8fe436ac-87e9-4d35-a3a4-0eb33cae6c86");
        user.setUserName("张三1");
        userService.saveOrUpdate(user);

        //下面的报错，由于在托管态，会发所有字段的更新语句，由于CREATETIME是null所有报错。
      /* User user = new User();
       user.setId("8fe436ac-87e9-4d35-a3a4-0eb33cae6c86");
       user.setUserName("张三1");
       userService.saveOrUpdate(user);*/
    }

    /**
     * 测试spring data jap 的DynamicInsert注解
     * 其作用是在生成SQL语句的时候之生成不为null的列
     * 比如下面的程序，只设置了id和name那么生成的SQL
     * 就只包含这两列，但是如果没有这个注解那么就回生成
     * 所有咧的SQL，没有设置的自动设置为Null.
     *
     * 设置这个注解有助于使用oracle列的默认值。比如本里就是使用了
     * user表中的createtime的默认值以及updatetime的默认值。
     */
    @Test
    public void testDynamicInsert(){
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUserName("李四");
        userService.saveOrUpdate(user);
    }
}
