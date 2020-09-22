package com.ss.apireaders;

import java.text.DecimalFormat;

import org.apache.commons.lang3.text.WordUtils;

import com.google.gson.JsonObject;
import com.ss.info.PlayerAPI;

public class Dungeons {

	private JsonObject dungeonStats = Profiles.getActiveProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get("dungeons").getAsJsonObject();
	private JsonObject classStats = dungeonStats.getAsJsonObject("player_classes");
	
	private Double catacombsLevel;
	
	public Double getCatacombsLevel() {
		
		if(catacombsLevel == null) {
			try {
			setCatacombsLevel(getLevel(dungeonStats.getAsJsonObject("dungeon_types").getAsJsonObject("catacombs").get("experience").getAsDouble()));
			} catch(NullPointerException e) {
				setCatacombsLevel(0.0);
			}
		}
		
		return catacombsLevel;
	}


	public void setCatacombsLevel(Double catacombsLevel) {
		this.catacombsLevel = catacombsLevel;
	}
	
	private String activeClass;
	
	public String getActiveClass() {
		
		if(activeClass == null) {
			try {
			setActiveClass(WordUtils.capitalizeFully(dungeonStats.get("selected_dungeon_class").getAsString()));
			} catch(NullPointerException e) {
				setActiveClass("");
			}
		}
		
		return activeClass;
	}
	
	public void setActiveClass(String newClass) {
		this.activeClass = newClass;
	}
	
	
	private Double archerLevel;
	
	public Double getArcherLevel() {
		
		if(archerLevel == null) {
			try {
			setArcherLevel(getLevel(classStats.getAsJsonObject("archer").get("experience").getAsDouble()));
			} catch(NullPointerException e) {
				setArcherLevel(0.0);
			}
		}
		
		return archerLevel;
	}


	public void setArcherLevel(Double archerLevel) {
		this.archerLevel = archerLevel;
	}
	
	private Double berserkLevel;
	
	public Double getBerserkLevel() {
		
		if(berserkLevel == null) {
			try {
			setBerserkLevel(getLevel(classStats.getAsJsonObject("berserk").get("experience").getAsDouble()));
			} catch(NullPointerException e) {
				setBerserkLevel(0.0);
			}
		}
		
		return berserkLevel;
	}


	public void setBerserkLevel(Double berserkLevel) {
		this.berserkLevel = berserkLevel;
	}
	
	private Double mageLevel;
	
	public Double getMageLevel() {
		
		if(mageLevel == null) {
			try {
			setMageLevel(getLevel(classStats.getAsJsonObject("mage").get("experience").getAsDouble()));
			} catch(NullPointerException e) {
				setMageLevel(0.0);
			}
		}
		
		return mageLevel;
	}


	public void setMageLevel(Double mageLevel) {
		this.mageLevel = mageLevel;
	}
	
	private Double tankLevel;

	public Double getTankLevel() {
		
		if(tankLevel == null) {
			try {
			setTankLevel(getLevel(classStats.getAsJsonObject("tank").get("experience").getAsDouble()));
			} catch(NullPointerException e) {
				setTankLevel(0.0);
			}
		}
		
		return tankLevel;
	}


	public void setTankLevel(Double tankLevel) {
		this.tankLevel = tankLevel;
	}
	
	private Double healerLevel;

	public Double getHealerLevel() {
		
		if(healerLevel == null) {
			try {
			setHealerLevel(getLevel(classStats.getAsJsonObject("healer").get("experience").getAsDouble()));
			} catch(NullPointerException e) {
				setHealerLevel(0.0);
			}
		}
		
		return healerLevel;
	}


	public void setHealerLevel(Double healerLevel) {
		this.healerLevel = healerLevel;
	}
	
	
	private double getLevel(Double xp) {
		int[] dungeonLevelReqs = {50, 75, 110, 160, 230, 330, 470, 670, 950, 1340, 1890, 2665, 3760, 5260, 7380, 10300, 14400, 20000, 27600, 38000, 52500, 71500, 97000, 132000, 180000, 243000, 328000, 445000, 600000, 800000, 1065000, 1410000, 1900000, 2500000, 3300000, 4300000, 5600000, 7200000, 9200000, 12000000, 15000000, 19000000, 24000000, 30000000, 38000000, 48000000, 60000000, 75000000, 93000000, 116250000};


		double level = 0;
		for(int x = 0; x <50; x++) {

			if(level == 50) {
				break;
			}

			if(xp >= dungeonLevelReqs[x]) {
				xp -= dungeonLevelReqs[x];
				level += 1;
			} else {
				level += xp / dungeonLevelReqs[(int) level];
				DecimalFormat f = new DecimalFormat("0.00");
				level = Double.parseDouble(f.format(level));
				break;
			}
		}
		return level;
	}


	
}
