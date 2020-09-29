package com.ss.info;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ss.Config;
import com.ss.apireaders.Profiles;
import com.ss.commands.SstatsThread;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.StatusReply.Session;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;

public class PlayerAPI {

	private static UUID playerUUID;

	public static UUID getPlayerUUID() {

		return playerUUID;
	}

	public static void setPlayerUUID(UUID newUUID) {

		playerUUID = newUUID;
	}


	private static HypixelAPI api;

	public static HypixelAPI getAPI() {

		return api;
	}



	private static JsonObject playerJson;

	public static JsonObject getJsonAPI() {


		if(playerJson == null) {
			setJsonAPI(getPlayerUUID());
		}

		return playerJson;
	}

	public static void setAPI(HypixelAPI newAPI) {
		api = newAPI;
	}


	public static void setJsonAPI(UUID playerUUID) {

		try {
			if(playerUUID == null) {
				playerJson = null;
			} else {
				try {
					playerJson = getAPI().getPlayerByUuid(playerUUID).get().getPlayer();
					SstatsThread.setAPIStatus(true);

				} catch(ExecutionException e) {
					SstatsThread.setAPIStatus(false);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static boolean startAPI() {
		if(Config.getApiKey() != null) {
			setAPI(new HypixelAPI(Config.getApiKey()));
			return true;
		} else {
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§cNo active API key. Set one using /ssc setApiKey."));
			return false;
		}
	}

	public static void badAPI() {
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§cHypixel API is being bad. Please try again."));
		PlayerAPI.shutdownAPI();
	}


	public static void shutdownAPI() {
		setPlayerUUID(null);
		setJsonAPI(null);
		setPlaysSkyblock(null);
		Profiles.setActiveProfile(null);
		Profiles.setAllProfiles(null);

		getAPI().shutdown();
	}
	private static Boolean playsSkyblock;

	public static Boolean doesPlaySkyblock() {

		if(playsSkyblock == null) {
			try {
				if(PlayerAPI.getJsonAPI().getAsJsonObject("stats").getAsJsonObject("SkyBlock").getAsJsonObject("profiles").entrySet().size() != 0) {
					setPlaysSkyblock(true);
				} else {
					setPlaysSkyblock(false);
				}
			} catch(NullPointerException e) {

				try {
					if(PlayerAPI.getAPI().getSkyBlockProfile(PlayerAPI.getPlayerUUID().toString().replace("-", "")).get().getProfile().get("profile") != null) {
						setPlaysSkyblock(true);
					} else setPlaysSkyblock(false);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					badAPI();
					e1.printStackTrace();
				}  catch(NullPointerException e1) {
					setPlaysSkyblock(false);

				}



			}
		}

		return playsSkyblock;
	}

	public static void setPlaysSkyblock(Boolean status) {
		playsSkyblock = status;
	}

	public static Session getPlayerSession() {
		Session session = null;
		try {
			session = getAPI().getStatus(getPlayerUUID()).get().getSession();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
		} catch (ExecutionException e) {
			PlayerAPI.badAPI();

		} 
		return session;
	}

	public static String UUIDtoName(UUID uuid) {
		String sURL = "https://api.mojang.com/user/profiles/" + uuid.toString().replace("-", "") + "/names";

		try {
			URL url = new URL(sURL);
			URLConnection request = url.openConnection();
			request.connect();
			JsonParser jp = new JsonParser();
			JsonElement json = jp.parse(new InputStreamReader((InputStream) request.getContent()));
			String name = json.getAsJsonArray().get(json.getAsJsonArray().size()-1).getAsJsonObject().get("name").getAsString();
			return name;
		} catch(IOException e) {
			return null;
		}
	}


}
