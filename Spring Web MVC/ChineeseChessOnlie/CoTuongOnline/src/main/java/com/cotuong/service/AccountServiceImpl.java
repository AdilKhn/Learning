package com.cotuong.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cotuong.model.Account;

public class AccountServiceImpl implements AccountService{
	
	
	@Autowired
	private MongoTemplate mongoTemplate;
	private Query query;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public AccountServiceImpl (MongoTemplate mongoTemplate){
		this.mongoTemplate=mongoTemplate;
	}
	@Override
	public void add(Account account) {
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		if(!mongoTemplate.collectionExists(Account.class)){
			mongoTemplate.createCollection(Account.class);
		}
		account.setPoint(10);
		account.setStatus(2);
		mongoTemplate.insert(account);
	}

	@Override
	public Account getAccount(String email) {
		query=new Query(Criteria.where("email").is(email));
		return mongoTemplate.findOne(query, Account.class);
	}

	@Override
	public void update(Account account) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Account account) {
		// TODO Auto-generated method stub
		
	}

}
