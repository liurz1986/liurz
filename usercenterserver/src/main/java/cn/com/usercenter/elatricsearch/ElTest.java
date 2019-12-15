package cn.com.usercenter.elatricsearch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * EL操作
 * 
 * @ClassName: ElTest
 * @Description: TODO
 * @author lwx393577：
 * @date 2019年11月30日 下午10:58:40
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ElTest {
	// @Autowired
	// ElasticsearchTemplate elasticsearchTemplate;
	@Autowired
	LibraryRepository libraryRepository;

	@Test
	public void test02() {

		Library libary = libraryRepository.findById((long) 4).get();
		System.out.println(libary.getBook_name());
	}

	/**
	 * 插入数据
	 */
	@Test
	public void testInsert() {
		libraryRepository.save(new Library(12, "A00042", "明史简述", 59, "吴晗", "吴晗背景uniworsity厉害"));
	}

	/**
	 * 批量插入数据
	 */
	@Test
	public void testInserts() {
		List<Library> librarys = new ArrayList<Library>();
		Library library1 = new Library(12, "A00042", "明史简述", 59, "吴晗", "吴晗背景uniworsity厉害");
		Library library2 = new Library(13, "A00043", "傅雷家书", 99, "傅聪", "都是NB，class大家u");
		Library library3 = new Library(14, "A00942", "时间简史", 169, "霍金", "教授宇宙大爆发的59年历史");
		Library library4 = new Library(15, "A00925", "我的前半生", 39, "方舟89子", "都是生活，每晚9点");
		Library library5 = new Library(16, "A00029", "围9城", 139, "钱钟书", "你想出城？不存在的");

		librarys.add(library1);
		librarys.add(library2);
		librarys.add(library3);
		librarys.add(library4);
		librarys.add(library5);
		libraryRepository.saveAll(librarys);
	}

	// 全字段查询,不分页
	@Test
	public void testSearch() {
		try {
			String searchStr = "A00042";
			QueryStringQueryBuilder builder = new QueryStringQueryBuilder(searchStr);
			Iterable<Library> search = libraryRepository.search(builder);
			Iterator<Library> iterator = search.iterator();
			while (iterator.hasNext()) {
				System.out.println("--> 数据：" + iterator.next());
			}
		} catch (Exception e) {
			System.out.println("---> 异常信息： " + e);
		}
	}

	// 全字段查询, 已经分页
	@Test
	public void testSearchByPage() {
		try {
			String searchStr = "三西阿";
			QueryStringQueryBuilder builder = new QueryStringQueryBuilder(searchStr);
			Iterable<Library> search = libraryRepository.search(builder, PageRequest.of(0, 2));
			Iterator<Library> iterator = search.iterator();
			while (iterator.hasNext()) {
				System.out.println("--> 数据：" + iterator.next());
			}

		} catch (Exception e) {
			System.out.println("---> 异常信息： " + e);
		}
	}
}
