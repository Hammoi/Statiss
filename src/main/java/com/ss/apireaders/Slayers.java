package com.ss.apireaders;

import java.util.ArrayList;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.ss.info.PlayerAPI;

public class Slayers {

	private JsonObject slayerJson;

	private JsonObject revJson;
	private JsonObject taranJson;
	private JsonObject svenJson;

	{
		try {
			slayerJson = Profiles.getActiveProfile().
					getAsJsonObject("members").
					getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).
					getAsJsonObject("slayer_bosses");
			revJson = slayerJson.getAsJsonObject("zombie");
			taranJson = slayerJson.getAsJsonObject("spider");
			svenJson = slayerJson.getAsJsonObject("wolf");
		} catch(NullPointerException e) {
			
		}
	}
	
	private int tiers = 4;

	private Integer revXp;

	public Integer getRevXp() {

		if(revXp == null) {
			try {
			setRevXp(revJson.get("xp").getAsInt());
			} catch(NullPointerException e) {
				setRevXp(0);
			}
		}

		return revXp;
	}

	public void setRevXp(Integer revXp) {
		this.revXp = revXp;
	}

	private ArrayList<Integer> revsSlain;


	public ArrayList<Integer> getRevsSlain() {

		if(revsSlain == null) {
			ArrayList<Integer> slain = new ArrayList<>();
			for(int t = 0; t < tiers; t++) {
				try {
					slain.add(revJson.get("boss_kills_tier_" + t).getAsInt());
				} catch(NullPointerException e) {
					slain.add(0);
				}
			}
			setRevsSlain(slain);
		}

		return revsSlain;
	}

	public void setRevsSlain(ArrayList<Integer> revsSlain) {
		this.revsSlain = revsSlain;
	}

	private Integer taranXp;

	public Integer getTaranXp() {

		if(taranXp == null) {
			try {
				setTaranXp(taranJson.get("xp").getAsInt());
			} catch(NullPointerException e) {
				setTaranXp(0);
			}
		}

		return taranXp;
	}

	public void setTaranXp(Integer taranXp) {
		this.taranXp = taranXp;
	}

	private ArrayList<Integer> taransSlain;

	public ArrayList<Integer> getTaransSlain() {

		if(taransSlain == null) {
			ArrayList<Integer> slain = new ArrayList<>();
			for(int t = 0; t < tiers; t++) {
				try {
					slain.add(taranJson.get("boss_kills_tier_" + t).getAsInt());
				} catch(NullPointerException e) {
					slain.add(0);
				}
			}
			setTaransSlain(slain);
		}

		return taransSlain;
	}

	public void setTaransSlain(ArrayList<Integer> taransSlain) {
		this.taransSlain = taransSlain;
	}


	private Integer svenXp;

	public Integer getSvenXp() {

		if(svenXp == null) {
			try {
			setSvenXp(svenJson.get("xp").getAsInt());
			} catch(NullPointerException e) {
				setSvenXp(0);
			}
		}

		return svenXp;
	}

	public void setSvenXp(Integer svenXp) {
		this.svenXp = svenXp;
	}

	private ArrayList<Integer> svensSlain;

	public ArrayList<Integer> getSvensSlain() {

		if(svensSlain == null) {
			ArrayList<Integer> slain = new ArrayList<>();
			for(int t = 0; t < tiers; t++) {
				try {
					slain.add(svenJson.get("boss_kills_tier_" + t).getAsInt());
				} catch(NullPointerException e) {
					slain.add(0);
				}
			}
			setSvensSlain(slain);
		}

		return svensSlain;
	}

	public void setSvensSlain(ArrayList<Integer> svensSlain) {
		this.svensSlain = svensSlain;
	}

	public static int getSlayerLevel(int xp) { //5 15 200 1000 5000 20000
		if(xp >= 1000000) {
			return 9;
		} else if(xp >= 400000) {
			return 8;
		} else if(xp >= 100000) {
			return 7;
		} else if(xp >= 20000) {
			return 6;
		} else if(xp >= 5000) {
			return 5;
		} else if(xp >= 1000) {
			return 4;
		} else if(xp >= 200) {
			return 3;
		} else if(xp >= 15) {
			return 2;
		} else if(xp >= 5) {
			return 1;
		} else if(xp >= 0) {
			return 0;
		} else {
			return -1;
		}
	}

	public static int getWolfSlayerLevel(int xp) {
		if(xp >= 1000000) {
			return 9;
		} else if(xp >= 400000) {
			return 8;
		} else if(xp >= 100000) {
			return 7;
		} else if(xp >= 20000) {
			return 6;
		} else if(xp >= 5000) {
			return 5;
		} else if(xp >= 1500) {
			return 4;
		} else if(xp >= 250) {
			return 3;
		} else if(xp >= 25) {
			return 2;
		} else if(xp >= 10) {
			return 1;
		} else if(xp >= 0) {
			return 0;
		} else {
			return -1;
		}

	}



}
