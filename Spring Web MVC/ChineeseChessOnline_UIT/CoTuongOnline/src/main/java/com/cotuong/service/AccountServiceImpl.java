package com.cotuong.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cotuong.model.Account;
import com.mongodb.MongoException;

public class AccountServiceImpl implements AccountService{
	
	
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private PasswordEncoder passwordEncoder;
	private Query query;
	private Update update;
	
	public AccountServiceImpl (MongoTemplate mongoTemplate){
		this.mongoTemplate=mongoTemplate;
	}
	
	@Override
	public void add(Account account) throws MongoException{
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		if(!mongoTemplate.collectionExists(Account.class)){
			mongoTemplate.createCollection(Account.class);
		}
		account.setPoint(10);
		account.setStatus(2);
		mongoTemplate.insert(account);
	}

	@Override
	public Account getAccount(String email)throws MongoException{
		query=new Query(Criteria.where("email").is(email));
		return mongoTemplate.findOne(query, Account.class);
	}

	@Override
	public void update(Account account) throws MongoException{
//		update=new Update();
		
	}

	@Override
	public void delete(Account account) throws MongoException{
		
	}
	
	/**
	 * 
	 */
	@Override
	public void activateAccount(String email)throws MongoException{
		query=new Query(Criteria.where("email").is(email));
		update=new Update();
		update.set("status",0);
		mongoTemplate.updateFirst(query, update, Account.class);
	}

	@Override
	public void updatePoint(String player_win, String player_lose) {
		query=new Query(Criteria.where("email").is(player_win));
		Account account=(Account)mongoTemplate.findOne(query, Account.class);
		update=new Update();
		update.set("point",account.getPoint()+10);
		mongoTemplate.updateFirst(query, update, Account.class);
		query=new Query(Criteria.where("email").is(player_lose));
		account=(Account)mongoTemplate.findOne(query, Account.class);
		update=new Update();
		update.set("point",account.getPoint()-10);
		mongoTemplate.updateFirst(query, update, Account.class);
	}

	@Override
	public void setStatusOnline(String email)throws MongoException{
		query=new Query(Criteria.where("email").is(email));
		update=new Update();
		update.set("status",1);
		mongoTemplate.updateFirst(query, update, Account.class);
	}

	@Override
	public void setStatusOffline(String email) throws  MongoException{
		query=new Query(Criteria.where("email").is(email));
		update=new Update();
		update.set("status",0);
		mongoTemplate.updateFirst(query, update, Account.class);
	}

	@Override
	public List<Account> getListOnline() throws MongoException{
		query=new Query(Criteria.where("status").is(1));
		query.fields().include("email").include("img_url").include("point").include("name");
		return mongoTemplate.find(query, Account.class);
	}
}
