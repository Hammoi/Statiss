package com.ss.info;

public class ColorCode {
	public static String getColorCode(String colorName) {
		switch(colorName) {
		case "BLACK":
			return "§0";

		case "DARK_BLUE":
			return "§1";

		case "DARK_GREEN":
			return "§2";

		case "DARK_AQUA":
			return "§3";

		case "DARK_RED":
			return "§4";

		case "DARK_PURPLE":
			return "§5";

		case "GOLD":
			return "§6";

		case "GRAY":
			return "§7";

		case "DARK_GRAY":
			return "§8";

		case "BLUE":
			return "§9";

		case "GREEN":
			return "§a";

		case "AQUA":
			return "§b";

		case "RED":
			return "§c";

		case "LIGHT_PURPLE":
			return "§d";

		case "YELLOW":
			return "§e";

		case "WHITE":
			return "§f";

		default:
			return null;
		}

	}

	public static String skillColor(int level) {
		if(level < 10) {
			return "§4" + level;
		} else if(level < 20) {
			return "§c" + level;
		} else if(level < 30) {
			return "§e" + level;
		} else if(level < 40) {
			return "§a" + level;
		} else if(level < 50) {
			return "§2" + level;
		} else if(level == 50) {
			return "§2§l" + level;
		} else return "";
	}

	public static String skillColor(double level) {
		if(level < 10) {
			return "§4" + level;
		} else if(level < 20) {
			return "§c" + level;
		} else if(level < 30) {
			return "§e" + level;
		} else if(level < 40) {
			return "§a" + level;
		} else if(level < 50) {
			return "§2" + level;
		} else if(level == 50) {
			return "§2§l" + level;
		} else return "";
	}
	
	public static String runescraftingColor(double level) {
		if(level < 5) {
			return "§4" + level;
		} else if(level < 10) {
			return "§c" + level;
		} else if(level < 15) {
			return "§e" + level;
		} else if(level < 20) {
			return "§a" + level;
		} else if(level < 25) {
			return "§2" + level;
		} else if(level == 25) {
			return "§2§l" + level;
		} else return "";
	}

	public static String slayerColor(int level) {
		switch(level) {
		case 0:
			return "§4";
		case 1:
			return "§4";
		case 2:
			return "§c";
		case 3:
			return "§c";
		case 4:
			return "§e";
		case 5:
			return "§e";
		case 6:
			return "§a";
		case 7:
			return "§2";
		case 8:
			return "§2";
		case 9:
			return "§2§l";
		default:
			return "";

		}
	}


	public static String itemColor(int rarity) {
		switch(rarity) {
		case 0:
			return "§f";
		case 1:
			return "§a";
		case 2:
			return "§9";
		case 3:
			return "§5";
		case 4:
			return "§6";
		case 5:
			return "§d";
		case 6:
			return "§c";
		case 7:
			return "§c";
		default:
			return "";
		}
	}
	
	public static String removeColor(String s) {
		StringBuilder string = new StringBuilder(s);
		while(string.indexOf("§") != -1) {
			int i = string.indexOf("§");
			string.delete(i, i+2);
		}
		
		if(string.charAt(0) == ' ') string.deleteCharAt(0);
		return string.toString();
	}
}
