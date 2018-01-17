package com.itheima.dao;

import java.util.List;

import com.itheima.pojo.Book;

public interface BookDao {

	/**
	 * 查询Book列表
	 * @return
	 */
	public List<Book> queryBookList();
	
}
