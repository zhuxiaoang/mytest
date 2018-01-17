package com.itheima.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.itheima.dao.BookDao;
import com.itheima.dao.BookDaoImpl;
import com.itheima.pojo.Book;

public class IndexTest {

	/**
	 * 演示写入索引库
	 * @throws Exception 
	 */
	@Test
	public void test() throws Exception {
		//1、获取数据
		//从数据库中来获取数据
		BookDao daoImpl = new BookDaoImpl();
		List<Book> list = daoImpl.queryBookList();
		
		//创建文档集合
		List<Document> docList = new ArrayList<>();
		for (Book book : list) {
			//2、创建文档
			Document document = new Document();
			//3、创建域Filed域
			//参数说明：1、指定域名；2、指定域值；3、是否存储！
			Field idField = new TextField("id", book.getId() + "", Store.YES);
			Field nameField = new TextField("name", book.getName() + "", Store.YES);
			Field priceField = new TextField("price", book.getPrice() + "", Store.YES);
			Field picField = new TextField("pic", book.getPic() + "", Store.YES);
			Field descField = new TextField("desc", book.getDesc() + "", Store.YES);
			//4、把数据放进域中，把域放在文档中
			document.add(idField);
			document.add(nameField);
			document.add(priceField);
			document.add(picField);
			document.add(descField);
			//把每一个文档放在集合中
			docList.add(document);
		}
		//分析：分词器
//		StandardAnalyzer analyzer = new StandardAnalyzer();//英文分词器
		//定义中文分词器
		IKAnalyzer analyzer = new IKAnalyzer();
		
		//5、创建FSDirectorty()指定索引库地址流 File System Directorty
		Directory directory = FSDirectory.open(new File("F:\\lucene\\280\\"));
		//6、创建索引库的配置对象IndexWriterConfig
		//参数说明：1、指定使用Lucene的版本；2、指定写入的分词器
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
		//7、创建这个写入索引库的写的对象IndexWriter(1、指定写入索引库地址流；2、指定写入索引库的配置对象)
		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
		//8、用IndexWriter把文档写入索引库
		for (Document document : docList) {
			indexWriter.addDocument(document);
		}
		//9、释放资源
		indexWriter.close();
	}

}
