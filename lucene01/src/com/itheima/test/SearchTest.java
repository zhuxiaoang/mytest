package com.itheima.test;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

public class SearchTest {

	@Test
	public void test() throws Exception {
		//1、创建分词器
		StandardAnalyzer analyzer = new StandardAnalyzer();
		//2、创建QueryParser解析器对象
		//两个参数：1、要查询默认的哪个域？2、指定查询的分词器
		QueryParser queryParser = new QueryParser("name", analyzer);
		//3、根据这个解析器对象创建查询条件对象
		//参数说明：自定义的条件查询
		//如果 在创建Query对象时候自定义查询语法那么会覆盖QueryParser里面的域名
		//如果在创建Query对象的时候不自定义语法那么会根据QueryParser里面定义的域来查询
		//4、设置查询条件的
		Query query = queryParser.parse("desc:java");
		//5、索引库在哪?
		Directory directory = FSDirectory.open(new File("F:\\lucene\\280\\"));
		//6、创建读取索引库的数据流
		IndexReader indexReader = DirectoryReader.open(directory);//索引库中所有数据：文档和索引
		//7、创建搜索对象：读取索引库的数据流
		IndexSearcher searcher = new IndexSearcher(indexReader);//索引库中所有数据：文档和索引
		//8、执行查询：需要搜索对象；返回结果集（坐标） : 根据索引来搜索
		//参数说明：1、查询的对象；2、返回结果集的记录数
		TopDocs topDocs = searcher.search(query, 2);//索引中的坐标
		
		//显示总记录数
		System.err.println("总记录数：" + topDocs.totalHits);
		//9、根据这个结果集获取数组坐标
		ScoreDoc[] docs = topDocs.scoreDocs;
		//10、需要遍历这个数组坐标
		for (ScoreDoc scoreDoc : docs) {
			//11、是获取每一个元素中的文档ID
			int docId = scoreDoc.doc;
			//12、再根据文档ID来查询文档
			Document doc = searcher.doc(docId);
			//13、打印 文档数据
			System.err.println("===id ==" + doc.get("id"));
			System.err.println("===name ==" + doc.get("name"));
			System.err.println("===price ==" + doc.get("price"));
			System.err.println("===pic ==" + doc.get("pic"));
		}
		//14、释放资源
		indexReader.close();
	}

}
