package com.tnmnet.steel.repo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tnmnet.steel.entity.Price;

import net.sf.json.JSONObject;

public class MongoDBTmp {
	/**
	 * URI
	 */
	private static final String URI = "mongodb://172.16.9.65:27017/nodedb";

	/**
	 * 
	 * @param collectionName
	 * @return
	 */
	public static List<Price> getPriceList(String collectionName) {
		MongoClientURI uri = new MongoClientURI(URI, MongoClientOptions.builder().cursorFinalizerEnabled(false));
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase db = mongoClient.getDatabase("nodedb");
		MongoCollection<org.bson.Document> collection = db.getCollection(collectionName);
		FindIterable<Document> doc = collection.find();
		//
		List<Price> list = new ArrayList<Price>();
		doc.forEach(new Block<Document>() {

			@Override
			public void apply(Document arg0) {
				Price price = new Price();
				JSONObject jsonObject = JSONObject.fromObject(arg0.toJson());
				price.setTimes(jsonObject.getString("times"));
				price.setPrice(new BigDecimal(jsonObject.getString("price")));
				list.add(price);
			}
		});
		mongoClient.close();
		return list;
	}

	public static void main(String args[]) {
		MongoDBTmp.getPriceList("PRICE000783");
	}
}