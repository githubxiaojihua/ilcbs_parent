package com.xiaojihua.service;

import java.util.List;

public interface SqlService {

	//返回单值
	public String getSingleValue(String sql);
	
	public String getSingleValue(String sql, Object[] objs);
	
	public String[] toArray(String sql);
	
	public List executeSQL(String sql);
	
	public List executeSQL(String sql, Object[] objs);
	
	public List executeSQLForList(String sql, Object[] objs);
	
	public int updateSQL(String sql);
	
	public int updateSQL(String sql, Object[] objs);
	
	public int[] batchSQL(String[] sql);
	
}
