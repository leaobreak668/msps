package com.tnmnet.steel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.tnmnet.steel.entity.Price;
import com.tnmnet.steel.repo.PriceRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Service
public class PriceService {

	@Autowired
	private PriceRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	public Price createPrice(Price price) {
		repository.save(price);
		return price;
	}

	public List<Price> findAllByCode(String code) {
		DBObject queryObject = new BasicDBObject();
		queryObject.put("code", code);
		Query query = new BasicQuery(queryObject);
		// 排序
		query.with(new Sort(Direction.ASC, "id"));
		//
		List<Price> datas = (List<Price>) mongoTemplate.find(query, Price.class);
		return datas;
	}
}
