package com.xiaojihua.serviceImpl;

import com.xiaojihua.dao.DeptDao;
import com.xiaojihua.dao.UserDao;
import com.xiaojihua.domain.Dept;
import com.xiaojihua.domain.User;
import com.xiaojihua.service.DeptService;
import com.xiaojihua.service.UserService;
import com.xiaojihua.utils.Encrypt;
import com.xiaojihua.utils.MailUtil;
import com.xiaojihua.utils.SysConstant;
import com.xiaojihua.utils.UtilFuns;
import org.apache.ws.commons.schema.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao dao;

    @Autowired
    private SimpleMailMessage mailMessage;

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Override
    public List<User> find(Specification<User> spec) {
        return dao.findAll(spec);
    }

    @Override
    public User get(String id) {
        //find相当于get没有延时加载的问题
        return dao.findOne(id);
    }

    @Override
    public Page<User> findPage(Specification<User> spec, Pageable pageable) {
        //从这里也看出应该在dao中继承JpaSpecificationExecutor
        return dao.findAll(spec,pageable);
    }

    /**
     * 更新和插入都走dao.save方法。
     * 根据entity的id来判断是否是新增，如果id是新的那么就是新增，
     * 如果id已经在数据库中了那么就是修改。
     * @param entity
     */
    @Override
    public void saveOrUpdate(final User entity) {
        if(UtilFuns.isEmpty(entity.getId())){
            String id = UUID.randomUUID().toString();
            entity.setId(id);
            entity.getUserinfo().setId(id);

            //增加shiro框架以后，增加用户的时候需要设置密码（原来没有设置）
            //这样才能在新增用户后用于登录
            entity.setPassword(Encrypt.md5(SysConstant.DEFAULT_PASS,entity.getUserName()));

            //发送注册邮件

            //方式一：通过javaMail
            //未免发送邮件时间过长因此新启动一个线程来发，使用工具类发送
            /*Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        MailUtil.sendMsg(entity.getUserinfo().getEmail(),"新增人员提醒邮件","您的密码是" + SysConstant.DEFAULT_PASS);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
            th.start();*/

            //方式二：通过spring整合javaMail来发送邮件
            Thread th2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    mailMessage.setTo(entity.getUserinfo().getEmail());
                    mailMessage.setSubject("新增人员提醒邮件");
                    mailMessage.setText("您的密码是:" + SysConstant.DEFAULT_PASS);
                    mailSender.send(mailMessage);
                }
            });
            th2.start();
        }else{

        }
        dao.save(entity);
    }

    @Override
    public void saveOrUpdateAll(Collection<User> entitys) {
        dao.save(entitys);
    }

    @Override
    public void deleteById(String id) {
        dao.delete(id);
    }

    @Override
    public void delete(String[] ids) {
        for(String id : ids){
            dao.delete(id);
        }
    }

    @Override
    public List<Object[]> getLoginData() {
        // TODO Auto-generated method stub
        return dao.getLoginData();
    }
}
