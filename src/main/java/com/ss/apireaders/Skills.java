package com.ss.apireaders;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ss.info.PlayerAPI;
import com.ss.info.Util;

public class Skills {

	private boolean apiEnabled = true;

	public boolean getApiStatus() {

		if((getMining() % 1) == 0 && (getCombat() % 1) == 0 && (getForaging() % 1) == 0 && (getFishing() % 1) == 0 && (getFarming() % 1) == 0 && (getAlchemy() % 1) == 0 && (getEnchanting() % 1) == 0 && (getTaming() % 1) == 0) {
			setApiStatus(false);
		}

		return apiEnabled;

	}

	public void setApiStatus(boolean newStatus) {
		this.apiEnabled = newStatus;
	}

	private Double mining;

	public double getMining() {

		if(mining == null) {

			try {
				setMining(Util.getLevel(Util.normalSkillReqs, Profiles.getActiveProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get("experience_skill_mining").getAsDouble())[0]);
			} catch(NullPointerException e) {
				setApiStatus(false);
				try {
					setMining(Double.valueOf(PlayerAPI.getJsonAPI().getAsJsonObject("achievements").get("skyblock_excavator").getAsInt()));
				} catch(NullPointerException npe) {
					setMining(0);
				}
			}

		}
		return mining;
	}

	public void setMining(double newMining) {
		this.mining = newMining;
	}

	private Double combat;

	public double getCombat() {
		if(combat == null) {
			try {
				setCombat(Util.getLevel(Util.normalSkillReqs, Profiles.getActiveProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get("experience_skill_combat").getAsDouble())[0]);
			} catch(NullPointerException e) {
				setApiStatus(false);
				try {
					setCombat(Double.valueOf(PlayerAPI.getJsonAPI().getAsJsonObject("achievements").get("skyblock_combat").getAsInt()));
				} catch(NullPointerException npe) {
					setCombat(0);
				}
			}
		}
		return combat;
	}

	public void setCombat(double newCombat) {
		this.combat = newCombat;
	}

	private Double foraging;

	public double getForaging() {
		if(foraging == null) {
			try {
				setForaging(Util.getLevel(Util.normalSkillReqs, Profiles.getActiveProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get("experience_skill_foraging").getAsDouble())[0]);
			} catch(NullPointerException e) {
				setApiStatus(false);
				try {
					setForaging(Double.valueOf(PlayerAPI.getJsonAPI().getAsJsonObject("achievements").get("skyblock_gatherer").getAsInt()));
				} catch(NullPointerException npe) {
					setForaging(0);
				}
			}
		}
		return foraging;
	}

	public void setForaging(double newForaging) {
		this.foraging = newForaging;
	}
	private Double fishing;

	public double getFishing() {
		if(fishing == null) {
			try {
				setFishing(Util.getLevel(Util.normalSkillReqs, Profiles.getActiveProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get("experience_skill_fishing").getAsDouble())[0]);
			} catch(NullPointerException e) {
				setApiStatus(false);
				try {
					setFishing(Double.valueOf(PlayerAPI.getJsonAPI().getAsJsonObject("achievements").get("skyblock_angler").getAsInt()));
				} catch(NullPointerException npe) {
					setFishing(0);
				}
			}
		}
		return fishing;
	}

	public void setFishing(double newFishing) {
		this.fishing = newFishing;
	}

	private Double farming;

	public double getFarming() {
		if(farming == null) {
			try {
				setFarming(Util.getLevel(Util.normalSkillReqs, Profiles.getActiveProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get("experience_skill_farming").getAsDouble())[0]);
			} catch(NullPointerException e) {
				setApiStatus(false);
				try {
					setFarming(Double.valueOf(PlayerAPI.getJsonAPI().getAsJsonObject("achievements").get("skyblock_harvester").getAsInt()));
				} catch(NullPointerException npe) {
					setFarming(0);
				}
			}
		}
		return farming;
	}

	public void setFarming(double newFarming) {
		this.farming = newFarming;
	}

	private Double alchemy;

	public double getAlchemy() {
		if(alchemy == null) {
			try {
				setAlchemy(Util.getLevel(Util.normalSkillReqs, Profiles.getActiveProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get("experience_skill_alchemy").getAsDouble())[0]);
			} catch(NullPointerException e) {
				setApiStatus(false);
				try {
					setAlchemy(Double.valueOf(PlayerAPI.getJsonAPI().getAsJsonObject("achievements").get("skyblock_concoctor").getAsInt()));
				} catch(NullPointerException npe) {
					setAlchemy(0);
				}
			}
		}
		return alchemy;
	}

	public void setAlchemy(double newAlchemy) {
		this.alchemy = newAlchemy;
	}

	private Double enchanting;

	public double getEnchanting() {
		if(enchanting == null) {
			try {
				setEnchanting(Util.getLevel(Util.normalSkillReqs, Profiles.getActiveProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get("experience_skill_enchanting").getAsDouble())[0]);
			} catch(NullPointerException e) {
				setApiStatus(false);
				try {
					setEnchanting(Double.valueOf(PlayerAPI.getJsonAPI().getAsJsonObject("achievements").get("skyblock_augmentation").getAsInt()));
				} catch(NullPointerException npe) {
					setEnchanting(0);
				}
			}
		}
		return enchanting;
	}

	public void setEnchanting(double newEnchanting) {
		this.enchanting = newEnchanting;
	}

	private Double taming;

	public double getTaming() {
		if(taming == null) {
			try {
				setTaming(Util.getLevel(Util.normalSkillReqs, Profiles.getActiveProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get("experience_skill_taming").getAsDouble())[0]);
			} catch(NullPointerException e) {
				setApiStatus(false);
				try {
					setTaming(Double.valueOf(PlayerAPI.getJsonAPI().getAsJsonObject("achievements").get("skyblock_domesticator").getAsInt()));
				} catch(NullPointerException npe) {
					setTaming(0);
				}
			}
		}

		return taming;
	}

	public void setTaming(double newTaming) {
		this.taming = newTaming;
	}

	private Double carpentry;

	public double getCarpentry() {
		if(carpentry == null) {
			try {
				setCarpentry(Util.getLevel(Util.normalSkillReqs, Profiles.getActiveProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get("experience_skill_carpentry").getAsDouble())[0]);
			} catch(NullPointerException e) {
				setApiStatus(false);
			}
		}

		return carpentry;
	}

	public void setCarpentry(double newCarpentry) {
		this.carpentry = newCarpentry;
	}

	private Double runescrafting;

	public double getRunescrafting() {
		if(runescrafting == null) {
			try {
				double xp = Profiles.getActiveProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get("experience_skill_runecrafting").getAsDouble();

				int[] skillReqs = {0, 50, 100, 125, 160, 200, 250, 315, 400, 500, 625, 785, 1000, 1250, 1600, 2000, 2465, 3125, 4000, 5000, 6200, 7800, 9800, 12200, 15300, 19050};
				double level = 0;
				for(int x = 0; x <25; x++) {

					if(level == 25) {
						break;
					}

					if(xp >= skillReqs[x]) {
						xp -= skillReqs[x];  
						level += 1;
					} else {
						level += xp / skillReqs[(int) level];
						DecimalFormat f = new DecimalFormat("0.00");
						level = Double.parseDouble(f.format(level));
						break;
					}
				}
				
				setRunescrafting(level);

			} catch(NullPointerException e) {
				setApiStatus(false);
			}
		}

		return runescrafting;
	}

	public void setRunescrafting(double newRunescrafting) {
		this.runescrafting = newRunescrafting;
	}








}
