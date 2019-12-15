package cn.com.usercenter.elatricsearch;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "local_library", type = "libaray_book")
public class Library implements Serializable {
	/**
	 * index：是否设置分词 analyzer：存储时使用的分词器 ik_max_word ik_word
	 * searchAnalyze：搜索时使用的分词器 store：是否存储 type: 数据类型
	 */
	/**
	 * @Description: @Id注解必须是springframework包下的
	 *               org.springframework.data.annotation.Id
	 * @Author: https://blog.csdn.net/chen_2890
	 */
	@Id
	private Integer book_id;
	private String book_code;
	private String book_name;
	private Integer book_price;
	private String book_author;
	private String book_desc;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((book_price == null) ? 0 : book_price.hashCode());
		return result;
	}

	public Library() {
		super();
	}

	public Library(Integer book_id, String book_code, String book_name, Integer book_price, String book_author,
			String book_desc) {
		super();
		this.book_id = book_id;
		this.book_code = book_code;
		this.book_name = book_name;
		this.book_price = book_price;
		this.book_author = book_author;
		this.book_desc = book_desc;
	}

	public Integer getBook_id() {
		return book_id;
	}

	public void setBook_id(Integer book_id) {
		this.book_id = book_id;
	}

	public String getBook_code() {
		return book_code;
	}

	public void setBook_code(String book_code) {
		this.book_code = book_code;
	}

	public String getBook_name() {
		return book_name;
	}

	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}

	public Integer getBook_price() {
		return book_price;
	}

	public void setBook_price(Integer book_price) {
		this.book_price = book_price;
	}

	public String getBook_author() {
		return book_author;
	}

	public void setBook_author(String book_author) {
		this.book_author = book_author;
	}

	public String getBook_desc() {
		return book_desc;
	}

	public void setBook_desc(String book_desc) {
		this.book_desc = book_desc;
	}

}
