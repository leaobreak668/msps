package com.tnmnet.steel;

import java.util.List;

import com.tnmnet.steel.entity.Price;
import com.tnmnet.steel.repo.MongoDBTmp;
import com.tnmnet.steel.service.StockMining;

public class Mining {

	public static void main(String[] args) {
		List<Price> lists = MongoDBTmp.getPriceList("PRICE600755");
		StockMining mining = new StockMining();
		mining.makeMoney(lists);
	}
}