package cn.com.usercenter.mongodb;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDBUtil {
	//连接MongoDB数据库
	MongoClient mongoClient = new MongoClient("localhost", 27017);
	
	//获取具体数据库（数据库名为liurz）
	@SuppressWarnings("deprecation")
	DB db = mongoClient.getDB("liurz");
	
	//获取数据表
	public DBCollection getTable(String tableName){

		return db.getCollection(tableName);
		
	}
	
	public void save(){
		DBCollection tableUser=getTable("user");
		BasicDBObject document = new BasicDBObject();
		document.put("name", "mkyong");
		document.put("age", 30);
		document.put("createdDate", new Date());
		tableUser.insert(document);
	}
	
	public Object find(){
		DBCollection tableUser=getTable("user");
		BasicDBObject search=new BasicDBObject();
		search.put("name","mkyong");
		DBCursor dbCursor=tableUser.find(search);
		List<Object> data=new ArrayList<Object>();
		if(dbCursor.hasNext()){
			DBObject object=dbCursor.next();
			data.add(object);
		}
		return data;
	}
	
	public void delete(){
		DBCollection tableUser=getTable("user");
		BasicDBObject delete=new BasicDBObject();
		delete.put("id","02");
		tableUser.remove(delete);
	}
	public static void main(String[] args) {
		MongoDBUtil MongoDBUtil=new MongoDBUtil();
		MongoDBUtil.delete();
		
	}
}
