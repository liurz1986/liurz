package cn.com.usercenter.repository;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.com.usercenter.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, String>{
	//jpa会自动查询，不用写sql。findBy后面接类的属性名---就会根据属性查询
	User findByUuid(String uuid);
	 
	void deleteByUuid(String uuid);
	
	//@Param 命名参数，sql中可以用  ：命名参数值
	@Query(value="select a from User a where a.name=:name and a.password=:password")
	User findUser(@Param("name") String name,@Param("password") String password);
   //声明操作查询   ?1表示第一个参数，？2表示第二个参数 以此类推
	@Query(value="select a from User a where datediff(endTime,updateTime)<=?1")
	List<User> findUsersByEndTime(int days);
	
	@Query(value="select name from User")
	List<String> allUserName();
	
	//jpa对分页支持,name的值模糊查询
	@Query(value="select a from User a where a.name like %?1% ")
	Page<User> findByName(String name,Pageable pageable);
	
	/**
	 * jpa对分页支持,createTime去查询
	 * ?1表示第一个参数，？2表示第二个参数
	 */
	@Query(value="select u from User u where createTime between ?1 and ?2 order by createTime desc")
	Page<User> findAllByCreateTime(Date startTime,Date endTime,Pageable pageable);
	
	/**
	 * %m---月  00-12
	 * %Y---年 比如---2018  四位
	 * %y---年 比如      ---18 两位
	 * 用对象接受返回其中几个会报错，必须全部字段返回
	 */
	@Query(value="select * from t_user a  where DATE_FORMAT(a.create_time,'%m')=?1 order by create_time desc limit ?2,?3",nativeQuery=true)
	List<User> findAllByMonth(Integer month,Integer page,Integer row);
	

   
}