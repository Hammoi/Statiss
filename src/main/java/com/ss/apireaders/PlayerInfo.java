package com.ss.apireaders;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ss.info.PlayerAPI;

import net.hypixel.api.util.ILeveling;

public class PlayerInfo {

	private Integer networkExp;

	public Integer getNetworkExp() {

		if(networkExp == null) {
			if(PlayerAPI.getJsonAPI().has("networkExp")) {
				setNetworkExp(PlayerAPI.getJsonAPI().get("networkExp").getAsInt());
			} else {
				setNetworkExp(0);
			}
		}

		return networkExp;
	}

	public void setNetworkExp(Integer networkExp) {
		this.networkExp = networkExp;
	}

	private Integer networkLevel;

	public Integer getNetworkLevel() {

		if(networkLevel == null) {
			setNetworkLevel((int) ILeveling.getLevel(getNetworkExp()));
		}

		return networkLevel;
	}
	public void setNetworkLevel(Integer networkLevel) {
		this.networkLevel = networkLevel;
	}


	private Integer exptoNextLevel;

	public Integer getExptoNextLevel() {

		if(exptoNextLevel == null) {
			setExptoNextLevel((int) (ILeveling.getExpFromLevelToNext(getNetworkLevel()) - (getNetworkExp() - ILeveling.getTotalExpToLevel(getNetworkLevel()))));
		}

		return exptoNextLevel;
	}
	public void setExptoNextLevel(Integer exptoNextLevel) {
		this.exptoNextLevel = exptoNextLevel;
	}


	private Integer achievementPoints;

	public Integer getAchievementPoints() {

		if(achievementPoints == null) {
			if(PlayerAPI.getJsonAPI().has("achievementPoints")) {
				setAchievementPoints(PlayerAPI.getJsonAPI().get("achievementPoints").getAsInt());
			} else {
				setAchievementPoints(0);
			}
		}

		return achievementPoints;
	}
	public void setAchievementPoints(Integer achievementPoints) {
		this.achievementPoints = achievementPoints;
	}

	private Integer karma;

	public Integer getKarma() {

		if(karma == null) {
			if(PlayerAPI.getJsonAPI().has("karma")) {
				setKarma(PlayerAPI.getJsonAPI().get("karma").getAsInt());
			} else setKarma(0);
		}

		return karma;
	}
	public void setKarma(Integer karma) {
		this.karma = karma;
	}

	private Integer questsCompleted;

	public Integer getQuestsCompleted() {

		if(questsCompleted == null) {
			setQuestsCompleted(0);
			if(PlayerAPI.getJsonAPI().has("quests")) {
				JsonObject quests = PlayerAPI.getJsonAPI().getAsJsonObject("quests");
				Iterator<Entry<String, JsonElement>> questNames = quests.entrySet().iterator();

				while(questNames.hasNext()) {
					Entry<String, JsonElement> quest = questNames.next();


					String questName = quest.getKey();

					if(quests.getAsJsonObject(questName).has("completions")) {
						setQuestsCompleted(questsCompleted += quests.getAsJsonObject(questName).
								getAsJsonArray("completions").size());
					}
				}
			} else setQuestsCompleted(0);

		}

		return questsCompleted;
	}

	public void setQuestsCompleted(Integer questsCompleted) {
		this.questsCompleted = questsCompleted;
	}

}
