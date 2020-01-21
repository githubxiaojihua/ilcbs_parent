package com.xiaojihua.service;

import com.xiaojihua.domain.Dept;
import com.xiaojihua.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;

//这里的page和pageable是spring data jpa 的，并不是自己写的

public interface UserService {
    //查询所有，带条件查询
    public List<User> find(Specification<User> spec);
    //获取一条记录
    public User get(String id);
    //分页查询，将数据封装到一个page分页工具类对象
    public Page<User> findPage(Specification<User> spec, Pageable pageable);

    //新增和修改保存
    public  void saveOrUpdate(User entity);
    //批量新增和修改保存
    public  void saveOrUpdateAll(Collection<User> entitys);

    //单条删除，按id
    public  void deleteById(String id);
    //批量删除
    public  void delete(String[] ids);

}
