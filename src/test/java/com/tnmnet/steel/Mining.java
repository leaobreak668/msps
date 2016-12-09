package com.tnmnet.steel;

import java.util.List;

import com.tnmnet.steel.entity.Price;
import com.tnmnet.steel.service.StockMining;
import com.tnmnet.steel.util.MongoDBTmp;

public class Mining {

	public static void main(String[] args) {
		List<Price> lists = MongoDBTmp.getPriceList("PRICE000783");
		StockMining mining = new StockMining();
		mining.makeMoney(lists);
	}
}