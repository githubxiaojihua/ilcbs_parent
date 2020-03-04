package com.xiaojihua.service;

import com.xiaojihua.domain.Contract;
import com.xiaojihua.domain.Export;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;

public interface ExportService {
	//查询所有，带条件查询
	public List<Export> find(Specification<Export> spec);
	//获取一条记录
	public Export get(String id);
	//分页查询，将数据封装到一个page分页工具类对象
	public  Page<Export> findPage(Specification<Export> spec, Pageable pageable);
	
	//新增和修改保存
	public  void saveOrUpdate(Export entity);
	//批量新增和修改保存
	public  void saveOrUpdateAll(Collection<Export> entitys);
	
	//单条删除，按id
	public  void deleteById(String id);
	//批量删除
	public  void delete(String[] ids);
}
