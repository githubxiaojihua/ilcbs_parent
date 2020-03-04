package com.xiaojihua.service;

import java.util.Collection;
import java.util.List;

import com.xiaojihua.domain.ExportProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


public interface ExportProductService {
	//查询所有，带条件查询
	public List<ExportProduct> find(Specification<ExportProduct> spec);
	//获取一条记录
	public ExportProduct get(String id);
	//分页查询，将数据封装到一个page分页工具类对象
	public  Page<ExportProduct> findPage(Specification<ExportProduct> spec, Pageable pageable);
	
	//新增和修改保存
	public  void saveOrUpdate(ExportProduct entity);
	//批量新增和修改保存
	public  void saveOrUpdateAll(Collection<ExportProduct> entitys);
	
	//单条删除，按id
	public  void deleteById(String id);
	//批量删除
	public  void delete(String[] ids);
}
