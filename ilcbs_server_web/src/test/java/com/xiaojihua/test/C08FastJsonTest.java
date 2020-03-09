package com.xiaojihua.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.xiaojihua.domain.Role;
import com.xiaojihua.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class C08FastJsonTest {


    @Test
    public void test1() throws IOException {
        User user = new User();
        user.setUserName("zhangsan");
        user.setPassword("123456");
        String s = JSON.toJSONString(user);
        System.out.println(s);
    }

    @Test
    public void test2(){
        HashMap hashMap = new HashMap();
        hashMap.put("username","cgx");
        hashMap.put("password","123456");
        String s = JSON.toJSONString(hashMap);
        System.out.println(s);
    }

    @Test
    public void testList(){
        User user = new User();
        user.setUserName("zhangsan");
        user.setPassword("123456");

        User user1 = new User();
        user1.setUserName("cgx");
        user1.setPassword("123456");

        ArrayList arrayList = new ArrayList();
        arrayList.add(user);
        arrayList.add(user1);

        //fastjson第一个坑，引用
        /**
         * {"$ref":"$[0]"}]
         *
         * 解决这个问题需要toJsonString的第二个参数
         */
        arrayList.add(user);
        //关闭循环引用
        String s = JSON.toJSONString(arrayList, SerializerFeature.DisableCircularReferenceDetect);
        System.out.println(s);
    }

    /**
     * fastjson的第二个坑
     * 如果像user和role这样的你中有我，我中有你的情况，那么还会出现
     * {"$ref":"$[0]"}]这样的字符串。这时及时关闭了循环饮用还是不行
     * 做法是声明一个SimplePropertyPreFilter，构造参数里面写上需要
     * 转换的属性名称，不在列表里的fastjson均不进行转换。
     * 如果出现重复属性没有办法识别，只能修改原来的属性值
     */
    @Test
    public void testList1(){
        HashSet<User> users = new HashSet();
        HashSet<Role> roles = new HashSet();

        User user = new User();
        user.setUserName("zhangsan");
        user.setPassword("123456");
        users.add(user);

        Role role = new Role();
        role.setName("测试人员");
        role.setUsers(users);
        roles.add(role);

        user.setRoles(roles);

        SimplePropertyPreFilter sp = new SimplePropertyPreFilter("password","roles","name","userName");

        String s = JSON.toJSONString(user,sp,SerializerFeature.DisableCircularReferenceDetect);
        System.out.println(s);
    }


}
