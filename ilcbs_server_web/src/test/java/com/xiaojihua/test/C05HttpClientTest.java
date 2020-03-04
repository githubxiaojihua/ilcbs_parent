package com.xiaojihua.test;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class C05HttpClientTest {

    /**
     * 测试httpclient 发送get请求
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        //1、创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //2、创建httpGet请求
        HttpGet httpGet = new HttpGet("http://www.baidu.com");
        //3、httpclient执行请求获得相应
        CloseableHttpResponse response = client.execute(httpGet);
        //4、获得响应实体
        HttpEntity entity = response.getEntity();
        //5、获得响应状态
        System.out.println(response.getStatusLine());
        if(entity != null){
            //获取响应内容的长度
            System.out.println(entity.getContentLength());
            //获取响应内容
            System.out.println(EntityUtils.toString(entity));
        }
        response.close();
    }

    /**
     * 测试带参数的get请求
     * 请求本项目的登录
     */
    @Test
    public void test2() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        List<BasicNameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("username","cgx"));
        list.add(new BasicNameValuePair("password","123456"));
        String param = EntityUtils.toString(new UrlEncodedFormEntity(list, Consts.UTF_8));
        System.out.println(param);

        HttpGet httpGet = new HttpGet("http://localhost:8080/ilcbs/loginAction_login?" + param);
        CloseableHttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
    }

    /**
     * 发送post请求带有参数的，第一种方式
     * @throws IOException
     */
    @Test
    public void test3() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        List<BasicNameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("username","cgx"));
        list.add(new BasicNameValuePair("password","123456"));

        UrlEncodedFormEntity paramEntity = new UrlEncodedFormEntity(list,Consts.UTF_8);
        HttpPost post = new HttpPost("http://localhost:8080/ilcbs/loginAction_login");
        post.setEntity(paramEntity);

        CloseableHttpResponse response = client.execute(post);
        System.out.println(EntityUtils.toString(response.getEntity()));
        response.close();
    }

    /**
     * POST请求，第二种方式，使用的是org.apache.commons.httpclient.HttpClient
     * @throws IOException
     */
    @Test
    public void test4() throws IOException {
        //1、创建httpclient对象
        org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
        //2、创建get或者是post请求
        PostMethod postMethod = new PostMethod("http://localhost:8080/ilcbs/loginAction_login");
        //3、设置编码
        client.getParams().setContentCharset("UTF-8");
        //4、设置请求消息头，为表单提交方式
        postMethod.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        //5、设置提交参数
        postMethod.setParameter("username","cgx");
        postMethod.setParameter("password","123456");
        //6、执行Post请求
        client.executeMethod(postMethod);
        //7、获得响应状态码以及响应内容
        System.out.println(postMethod.getStatusLine());
        System.out.println(postMethod.getResponseBodyAsString());



    }

}
