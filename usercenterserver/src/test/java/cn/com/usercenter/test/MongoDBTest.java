package cn.com.usercenter.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.com.usercenter.mongodb.UserRepository;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class MongoDBTest {
	@Autowired
	@Qualifier("mongodbUser")
	private UserRepository userRepserositorys;

	@Test
	public void test1() {
		// userRepserositorys.deleteUserById("03");
	}
}
