package io.project.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;

import io.project.domain.Task;
import io.project.domain.User;

//Custom Repository Implementation 
//the Name must be the same as MAIN repo class that extends MonongoRepo but with IMPL
//or else you will cry and it wont work
public class UsersTaskRepositoryImpl implements UsersTaskRepositoryCustom 
{			 

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Override
	public int addTaskToList(String username, Task task) 
	{
		//criteria - query based on username
		Criteria criteria = Criteria.where("username").is(username);
	
		//add task to end of list
		Update update = new Update();
		update.push("tasks", task);

		//update document
		mongoTemplate.updateFirst(Query.query(criteria), update, User.class);
	
		
		
		
		return 0;
		
	}

	@Override
	public int updateTask(String username, String taskName, String status) {
	//	( { name : "ash", "tasks.name":"milk"}, 
	//			{ $set: {"tasks.$.status": "in progress"} })
		Criteria criteria = Criteria.where("username").is(username).and("tasks.name").is(taskName);
		Update update = new Update();
		update.set("tasks.$.status", status);
		mongoTemplate.updateFirst(Query.query(criteria), update, User.class);
		
		return 0;
	}
	
	@Override
	public int deleteTask(String username, String taskName) {

		//db.task.update({username: "tom"}, {$pull: {tasks: { name: "123" } } } );
		mongoTemplate.updateFirst(new BasicQuery("{username: \"" + username + "\"}"), 
								  new BasicUpdate("{$pull: {tasks: { name: \"" + taskName + "\"} } }" ), User.class);
		
		return 0;
	}

}
