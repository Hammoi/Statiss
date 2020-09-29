package com.ss.apireaders;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.commons.lang3.text.WordUtils;

import com.google.gson.JsonObject;
import com.ss.info.PlayerAPI;
import com.ss.info.Util;

public class Dungeons {

	private JsonObject dungeonStats = Profiles.getActiveProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get("dungeons").getAsJsonObject();
	private JsonObject catacombStats = dungeonStats.getAsJsonObject("dungeon_types").getAsJsonObject("catacombs");
	private JsonObject classStats = dungeonStats.getAsJsonObject("player_classes");

	private double[] noLevel = new double[] {0,0};

	private double[] catacombsLevel;

	public double[] getCatacombsLevel() {

		if(catacombsLevel == null) {
			try {
				setCatacombsLevel(Util.getLevel(Util.dungeonLevelReqs, catacombStats.get("experience").getAsDouble()));
			} catch(NullPointerException e) {
				setCatacombsLevel(noLevel);
			}
		}

		return catacombsLevel;
	}


	public void setCatacombsLevel(double[] catacombsLevel) {
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

	public double[][] getAllClasses() {
		return new double[][] {getArcherLevel(), getBerserkLevel(), getMageLevel(), getTankLevel(), getHealerLevel()};
	}

	public void setActiveClass(String newClass) {
		this.activeClass = newClass;
	}


	private double[] archerLevel;

	public double[] getArcherLevel() {

		if(archerLevel == null) {
			try {
				setArcherLevel(Util.getLevel(Util.dungeonLevelReqs, classStats.getAsJsonObject("archer").get("experience").getAsDouble()));
			} catch(NullPointerException e) {
				setArcherLevel(noLevel);
			}
		}

		return archerLevel;
	}


	public void setArcherLevel(double[] archerLevel) {
		this.archerLevel = archerLevel;
	}

	private double[] berserkLevel;

	public double[] getBerserkLevel() {

		if(berserkLevel == null) {
			try {
				setBerserkLevel(Util.getLevel(Util.dungeonLevelReqs, classStats.getAsJsonObject("berserk").get("experience").getAsDouble()));
			} catch(NullPointerException e) {
				setBerserkLevel(noLevel);
			}
		}

		return berserkLevel;
	}


	public void setBerserkLevel(double[] berserkLevel) {
		this.berserkLevel = berserkLevel;
	}

	private double[] mageLevel;

	public double[] getMageLevel() {

		if(mageLevel == null) {
			try {
				setMageLevel(Util.getLevel(Util.dungeonLevelReqs, classStats.getAsJsonObject("mage").get("experience").getAsDouble()));
			} catch(NullPointerException e) {
				setMageLevel(noLevel);
			}
		}

		return mageLevel;
	}


	public void setMageLevel(double[] mageLevel) {
		this.mageLevel = mageLevel;
	}

	private double[] tankLevel;

	public double[] getTankLevel() {

		if(tankLevel == null) {
			try {
				setTankLevel(Util.getLevel(Util.dungeonLevelReqs, classStats.getAsJsonObject("tank").get("experience").getAsDouble()));
			} catch(NullPointerException e) {
				setTankLevel(noLevel);
			}
		}

		return tankLevel;
	}


	public void setTankLevel(double[] tankLevel) {
		this.tankLevel = tankLevel;
	}

	private double[] healerLevel;

	public double[] getHealerLevel() {

		if(healerLevel == null) {
			try {
				setHealerLevel(Util.getLevel(Util.dungeonLevelReqs, classStats.getAsJsonObject("healer").get("experience").getAsDouble()));
			} catch(NullPointerException e) {
				setHealerLevel(noLevel);
			}
		}

		return healerLevel;
	}


	public void setHealerLevel(double[] healerLevel) {
		this.healerLevel = healerLevel;
	}


	

	private int[] floorsCompleted;


	public int[] getFloorsCompleted() {

		if(floorsCompleted == null) {

			setFloorsCompleted(getData("tier_completions"));


		}
		return floorsCompleted;
	}


	public void setFloorsCompleted(int[] floorsCompleted) {
		this.floorsCompleted = floorsCompleted;
	}

	private int[] floorsStarted;

	public int[] getFloorsStarted() {


		if(floorsStarted == null) {
				setFloorsStarted(getData("times_played"));

		}

		return floorsStarted;
	}


	public void setFloorsStarted(int[] floorsStarted) {
		this.floorsStarted = floorsStarted;
	}

	private int[] watcherKills;
	private int[] fastestTime;
	private int[] fastestTimeS;
	private int[] fastestTimeSPlus;
	private int[] bestScore;
	private int[] archerMostDamage;
	private int[] berserkMostDamage;
	private int[] healerMostDamage;
	private int[] mageMostDamage;
	private int[] tankMostDamage;
	private int[] mostHealing;
	private int[] totalMobsKilled;
	private int[] mostMobsKilled;

	private Integer secretsFound;

	public Integer getSecretsFound() {

		if(secretsFound == null) {
			setSecretsFound(PlayerAPI.getJsonAPI().getAsJsonObject("achievements").get("skyblock_treasure_hunter").getAsInt());
		}

		return secretsFound;
	}

	public void setSecretsFound(Integer newAmount) {
		this.secretsFound = newAmount;
	}


	private int[] getData(String key) {
		int[] list = new int[] {-1,-1,-1,-1,-1,-1};

		try {

			catacombStats.getAsJsonObject(key).entrySet().forEach((value) -> {
				list[Integer.parseInt(value.getKey())] = value.getValue().getAsInt();
			});


		} catch(NullPointerException e) {
			return list;
		}

		return list;
	}

	public int[] getWatcherKills() {

		if(watcherKills == null) {
			setWatcherKills(getData("watcher_kills"));
		}

		return watcherKills;
	}


	public void setWatcherKills(int[] watcherKills) {
		this.watcherKills = watcherKills;
	}


	public int[] getFastestTime() {

		if(fastestTime == null) {
			setFastestTime(getData("fastest_time"));
		}

		return fastestTime;
	}


	public void setFastestTime(int[] fastestTime) {
		this.fastestTime = fastestTime;
	}


	public int[] getFastestTimeS() {



		if(fastestTimeS == null) {
			setFastestTimeS(getData("fastest_time_s"));
		}

		return fastestTimeS;
	}


	public void setFastestTimeS(int[] fastestTimeS) {
		this.fastestTimeS = fastestTimeS;
	}


	public int[] getFastestTimeSPlus() {

		if(fastestTimeSPlus == null) {
			setFastestTimeSPlus(getData("fastest_time_s_plus"));
		}

		return fastestTimeSPlus;
	}


	public void setFastestTimeSPlus(int[] fastestTimeSPlus) {
		this.fastestTimeSPlus = fastestTimeSPlus;
	}


	public int[] getBestScore() {

		if(bestScore == null) {
			setBestScore(getData("best_score"));
		}

		return bestScore;
	}


	public void setBestScore(int[] bestScore) {
		this.bestScore = bestScore;
	}


	public int[] getArcherMostDamage() {

		if(archerMostDamage == null) {
			setArcherMostDamage(getData("most_damage_archer"));
		}

		return archerMostDamage;
	}


	public void setArcherMostDamage(int[] archerMostDamage) {
		this.archerMostDamage = archerMostDamage;
	}


	public int[] getBerserkMostDamage() {

		if(berserkMostDamage == null) {
			setBerserkMostDamage(getData("most_damage_berserk"));
		}

		return berserkMostDamage;
	}


	public void setBerserkMostDamage(int[] berserkMostDamage) {
		this.berserkMostDamage = berserkMostDamage;
	}


	public int[] getHealerMostDamage() {

		if(healerMostDamage == null) {
			setHealerMostDamage(getData("most_damage_healer"));
		}

		return healerMostDamage;
	}


	public void setHealerMostDamage(int[] healerMostDamage) {
		this.healerMostDamage = healerMostDamage;
	}


	public int[] getMageMostDamage() {

		if(mageMostDamage == null) {
			setMageMostDamage(getData("most_damage_mage"));
		}

		return mageMostDamage;
	}


	public void setMageMostDamage(int[] mageMostDamage) {
		this.mageMostDamage = mageMostDamage;
	}


	public int[] getTankMostDamage() {

		if(tankMostDamage == null) {
			setTankMostDamage(getData("most_damage_tank"));
		}

		return tankMostDamage;
	}


	public void setTankMostDamage(int[] tankMostDamage) {
		this.tankMostDamage = tankMostDamage;
	}


	public int[] getMostHealing() {

		if(mostHealing == null) {
			setMostHealing(getData("most_healing"));
		}

		return mostHealing;
	}


	public void setMostHealing(int[] mostHealing) {
		this.mostHealing = mostHealing;
	}


	public int[] getTotalMobsKilled() {

		if(totalMobsKilled == null) {
			setTotalMobsKilled(getData("mobs_killed"));
		}

		return totalMobsKilled;
	}


	public void setTotalMobsKilled(int[] totalMobsKilled) {
		this.totalMobsKilled = totalMobsKilled;
	}


	public int[] getMostMobsKilled() {

		if(mostMobsKilled == null) {
			setMostMobsKilled(getData("most_mobs_killed"));
		}

		return mostMobsKilled;
	}


	public void setMostMobsKilled(int[] mostMobsKilled) {
		this.mostMobsKilled = mostMobsKilled;
	}






}
