package com.tnmnet.steel.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tnmnet.steel.entity.Price;

public interface PriceRepository extends MongoRepository<Price, String> {

}
