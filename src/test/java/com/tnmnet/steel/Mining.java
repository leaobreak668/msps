package com.tnmnet.steel;

import java.util.List;

import com.tnmnet.steel.entity.Price;
import com.tnmnet.steel.repo.MongoDBTmp;
import com.tnmnet.steel.service.impl.StockMiningImpl;

public class Mining {

	public static void main(String[] args) {
		List<Price> lists = MongoDBTmp.getPriceList("PRICE600755");
		StockMiningImpl mining = new StockMiningImpl();
		mining.makeMoney(lists);
	}
}