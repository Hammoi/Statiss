package com.ss.info;

import java.text.DecimalFormat;

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
	
	public static int[] milliToMinute(int milliseconds) {
		
		int minute = (int) Math.floor(milliseconds/60000);
		return new int[] {minute, (int) Math.floor((((double) milliseconds)/60000 - minute) * 60)};
	}
	
	public static final int[] dungeonLevelReqs = {50, 75, 110, 160, 230, 330, 470, 670, 950, 1340, 1890, 2665, 3760, 5260, 7380, 10300, 14400, 20000, 27600, 38000, 52500, 71500, 97000, 132000, 180000, 243000, 328000, 445000, 600000, 800000, 1065000, 1410000, 1900000, 2500000, 3300000, 4300000, 5600000, 7200000, 9200000, 12000000, 15000000, 19000000, 24000000, 30000000, 38000000, 48000000, 60000000, 75000000, 93000000, 116250000};
	public static final int[] normalSkillReqs = {50, 125, 200, 300, 500, 750, 1000, 1500, 2000, 3500, 5000, 7500, 10000, 15000, 20000, 30000, 50000, 75000, 100000, 200000, 300000, 400000, 500000, 600000, 700000, 800000, 900000, 1000000, 1100000, 1200000, 1300000, 1400000, 1500000, 1600000, 1700000, 1800000, 1900000, 2000000, 2100000, 2200000, 2300000, 2400000, 2500000, 2600000, 2750000, 2900000, 3100000, 3400000, 3700000, 4000000};

	public static double[] getLevel(int[] levelReqs, Double xp) {


		double[] level = new double[] {0, 0, xp};

		for(int x = 0; x <50; x++) {

			if(level[0] == 50) {
				break;
			}

			if(xp >= levelReqs[x]) {
				xp -= levelReqs[x];
				level[0] += 1;
			} else {
				level[0] += xp / levelReqs[(int) level[0]];
				level[1] = xp;
				DecimalFormat f = new DecimalFormat("0.00");
				level[0] = Double.parseDouble(f.format(level[0]));
				break;
			}
		}
		return level;
	}
	
}
