package com.tnmnet.steel;

import com.tnmnet.steel.service.BFloat;

public class MBF {

	public static void main(String[] args) {
		BFloat bf = new BFloat(10);
		System.out.println(bf.divide(new BFloat(3)));
	}

}
