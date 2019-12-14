package cn.com.usercenter.mongodb;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import com.mongodb.WriteResult;
@Component("mongodbUser")
public class UserRepository {
    @Autowired
	private MongoTemplate mongoTemplate;
	
	public void save(UserVO userVO){
		mongoTemplate.save(userVO);
	}
	
	public UserVO findById(int id){
		Query query=new Query(Criteria.where("id").is(id));
		return mongoTemplate.findById(query, UserVO.class);
	}
	
	public List<UserVO> findAll(){
		Query query=new Query();
		return mongoTemplate.findAllAndRemove(query, UserVO.class);
	}
	
	public void updateUser(UserVO userVO){
		Query query=new Query(Criteria.where("id").is(userVO.getId()));
		 Update update= new Update().set("name", userVO.getName()).set("passWord", userVO.getPassword())
				 .set("telephone", userVO.getTelephone()).set("email", userVO.getEmail());
		 WriteResult result =mongoTemplate.updateFirst(query, update, UserVO.class);
	}
	public void deleteUserById(String id) {
        Query query=new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query,UserVO.class);
    }
	
}
