package com.xiaojihua.service;

import com.xiaojihua.domain.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;

//这里的page和pageable是spring data jpa 的，并不是自己写的

public interface ModuleService {
    //查询所有，带条件查询
    public List<Module> find(Specification<Module> spec);
    //获取一条记录
    public Module get(String id);
    //分页查询，将数据封装到一个page分页工具类对象
    public Page<Module> findPage(Specification<Module> spec, Pageable pageable);

    //新增和修改保存
    public  void saveOrUpdate(Module entity);
    //批量新增和修改保存
    public  void saveOrUpdateAll(Collection<Module> entitys);

    //单条删除，按id
    public  void deleteById(String id);
    //批量删除
    public  void delete(String[] ids);

}
