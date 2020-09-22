package com.ss.apireaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ss.info.PlayerAPI;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;

public class Profiles {

	private static JsonObject activeProfile;
	public static JsonObject getActiveProfile() {


		if(activeProfile == null) {
			ArrayList<JsonObject> profiles = new ArrayList<JsonObject>();
			ArrayList<Long> lastLogins = new ArrayList<Long>();
			try {

				for(Entry<String, JsonElement> profile : PlayerAPI.getJsonAPI().getAsJsonObject("stats").getAsJsonObject("SkyBlock").getAsJsonObject("profiles").entrySet()) {
					profiles.add(PlayerAPI.getAPI().getSkyBlockProfile(profile.getKey()).get().getProfile());
					lastLogins.add(PlayerAPI.getAPI().getSkyBlockProfile(profile.getKey()).get().getProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get("last_save").getAsLong());

					setActiveProfile(profiles.get(lastLogins.indexOf(Collections.max(lastLogins))));

				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {

				try {
					setActiveProfile(PlayerAPI.getAPI().getSkyBlockProfile(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get().getProfile());
				} catch(NullPointerException npe) {
					PlayerAPI.setPlaysSkyblock(false);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					PlayerAPI.badAPI();
				}


			} catch (ExecutionException e) {

				PlayerAPI.badAPI();
			}
		}

		System.out.println(activeProfile);
		return activeProfile;
	}

	public static void setActiveProfile(JsonObject newProfile) {
		activeProfile = newProfile;
	}

	private static ArrayList<JsonObject> allProfiles;

	public static ArrayList<JsonObject> getAllProfiles() {

		if(allProfiles == null) {
			ArrayList<JsonObject> profiles = new ArrayList<JsonObject>();
			try {
				for(Entry<String, JsonElement> profile : PlayerAPI.getJsonAPI().getAsJsonObject("stats").getAsJsonObject("SkyBlock").getAsJsonObject("profiles").entrySet()) {
					try {
						profiles.add(PlayerAPI.getAPI().getSkyBlockProfile(profile.getKey()).get().getProfile());
						PlayerAPI.setPlaysSkyblock(true);
					} catch (InterruptedException e) {

					} catch (ExecutionException e) {

						PlayerAPI.badAPI();
					}


				}
			} catch (NullPointerException e) {

				try {
					setActiveProfile(PlayerAPI.getAPI().getSkyBlockProfile(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get().getProfile());
				} catch(NullPointerException npe) {
					PlayerAPI.setPlaysSkyblock(false);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					PlayerAPI.badAPI();
				}

				PlayerAPI.setPlaysSkyblock(false);
			}

			setAllProfiles(profiles);

		}

		return allProfiles;
	}

	public static void setAllProfiles(ArrayList<JsonObject> newProfiles) {
		allProfiles = newProfiles;
	}

	/*
	 * Coop members
	 * Date created
	 * Last played
	 */

	private ArrayList<String> profileMembers;

	public ArrayList<String> getProfileMembers() {

		if(profileMembers == null) {
			ArrayList<String> members = new ArrayList<String>();
			try {
				getActiveProfile().getAsJsonObject("members").entrySet().forEach((uuid) -> {

					members.add(PlayerAPI.UUIDtoName(UUID.fromString(new StringBuilder(uuid.getKey()).insert(8, "-").insert(13, "-").insert(18, "-").insert(23, "-").toString())));


				});
				setProfileMembers(members);
			}catch(NullPointerException e) {}

		}

		return profileMembers;
	}

	public void setProfileMembers(ArrayList<String> newMembers) {
		this.profileMembers = newMembers;
	}

	private String cuteName;

	public String getCuteName() {

		if(cuteName == null) {
			try {
				setCuteName(PlayerAPI.getJsonAPI().
						getAsJsonObject("stats").
						getAsJsonObject("SkyBlock").
						getAsJsonObject("profiles").
						getAsJsonObject(getActiveProfile().get("profile_id").getAsString()).
						get("cute_name").getAsString());
			} catch(NullPointerException e) {
				setCuteName("§cUnknown");
			}
		}


		return cuteName;
	}

	public void setCuteName(String newCuteName) {
		cuteName = newCuteName;
	}

	private Long dateJoined;

	public Long getDateJoined() {

		if(dateJoined == null) {
			try {
				setDateJoined(getActiveProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get("first_join").getAsLong());
			} catch(NullPointerException e) {
				setDateJoined((long) 0);
			}
		}

		return dateJoined;
	}

	public void setDateJoined(Long newDate) {
		this.dateJoined = newDate;
	}

	private Long lastPlayed;

	public Long getLastPlayed() {

		if(lastPlayed == null) {
			try {
				setLastPlayed(getActiveProfile().getAsJsonObject("members").getAsJsonObject(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get("last_save").getAsLong());
			} catch(NullPointerException e) {
				setLastPlayed((long) 0);
			}
		}

		return lastPlayed;
	}

	public void setLastPlayed(Long newDate) {
		this.lastPlayed = newDate;
	}

}

class Profile {

	ArrayList<String> members;
	String cuteName;
	Integer dateJoined;
	Integer lastPlayed;

	public Profile(ArrayList<String> members, String cuteName, Integer dateJoined, Integer lastPlayed) {
		this.members = members;
		this.cuteName = cuteName;
		this.dateJoined = dateJoined;
		this.lastPlayed = lastPlayed;
	}

	public ArrayList<String> getMembers() {
		return members;
	}

	public String getCuteName() {
		return cuteName;
	}

	public Integer getDateJoined() {
		return dateJoined;
	}

	public Integer getLastPlayed() {
		return lastPlayed;
	}



}
