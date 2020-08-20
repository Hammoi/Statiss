package com.ss.info;

import com.google.gson.JsonArray;

public class Util {

	public static String arrayToString(JsonArray array) {
		StringBuilder string = new StringBuilder();
		for(int l = 0; l < array.size(); l++) {
			string.append(array.get(l).getAsString());

			if(l < array.size() - 1) {
				string.append("\n");
			}

		}		
		return string.toString();
	}
	
	public static String arrayToString(JsonArray array, Integer anvilUses) {
		StringBuilder string = new StringBuilder();
		for(int l = 0; l < array.size(); l++) {
			string.append(array.get(l).getAsString());

			if(l < array.size() - 1) {
				string.append("\n");
			}

			if(l == array.size() - 2) {
				if(anvilUses != null) {
				string.append("§7Anvil Uses: §c" + anvilUses + "\n");
				}
			}

		}
		return string.toString();
	}
	
}
