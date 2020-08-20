package com.ss.apireaders;

import com.ss.info.PlayerAPI;

public class Damage {
	private Integer highestCrit;

	public Integer getHighestCrit() {

		if(highestCrit == null) {
			try {
				setHighestCrit((int) Profiles.getActiveProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).getAsJsonObject("stats").get("highest_critical_damage").getAsDouble());
			} catch(NullPointerException e) {
				setHighestCrit(0);
			}
		}

		return highestCrit;
	}

	public void setHighestCrit(Integer highestCrit) {
		this.highestCrit = highestCrit;
	}


}
