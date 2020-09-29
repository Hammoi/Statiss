package com.ss;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Config {

	private static JsonObject config;
	private static File configFile;
	private static FileReader fr;
	private static UUID apiKey;
	private static Boolean debugMode;

	public static void setUpConfig(File cFile) {

		configFile = cFile;
		try {
			if(configFile.createNewFile()) {
				System.out.println("SS: Config doesn't exist, writing new config.");
			} else {
				System.out.println("SS: Config exists.");
			}

			fr = new FileReader(configFile);
		} catch (IOException e) {
			System.out.println("SS: Something went wrong while setting up config.");
		}

		StringBuilder c = new StringBuilder();

		Scanner s = new Scanner(fr);
		while (s.hasNextLine()) {
			c.append(s.nextLine());
		}
		s.close();

		if(c.length() != 0) {
			try {
				config = new Gson().fromJson(c.toString(), JsonObject.class);
			} catch(JsonSyntaxException e) {
				config = new JsonObject();
				createConfig();
				System.out.println("SS: Corrupt config file. Writing new one.");
			}
		} else {
			config = new JsonObject();
			createConfig();
		}
	}

	public static void createConfig() {
		writeConfig("APIKey", "");
		writeConfig("Debug", false);
	}


	public static boolean writeConfig(String key, String value) {
		if(config.has(key)) {
			config.remove(key);

		}
		config.addProperty(key, value);
		try {
			FileWriter fw = new FileWriter(configFile);
			fw.write(config.toString());
			fw.flush();
			fw.close();
			return true;
		} catch (IOException e) {
			System.out.println("SS: Something went wrong while writing to config.");
			return false;
		}

	}

	public static boolean writeConfig(String key, Boolean value) {
		if(config.has(key)) {
			config.remove(key);

		}
		config.addProperty(key, value);
		
		try {
			FileWriter fw = new FileWriter(configFile);
			fw.write(config.toString());
			
			fw.flush();
			fw.close();
			return true;
		} catch (IOException e) {
			System.out.println("SS: Something went wrong while writing to config.");
			return false;
		}

	}

	public JsonObject getConfig() {

		return config;
	}

	private static boolean validKey = false;

	public static boolean isValidKey() {

		return validKey;
	}

	public static void setValidKeyStatus(boolean newStatus) {
		validKey = newStatus;
	}

	public static UUID getApiKey() {

		if(apiKey == null) {

			try {
				apiKey = UUID.fromString(config.get("APIKey").getAsString());
			} catch(NullPointerException e) {
				System.out.println("SS: No key inside config.");
				
				
				return null;
			} catch(IllegalArgumentException e) {
				System.out.println("SS: IAE.");
				return null;
			}
			setValidKeyStatus(true);

		}

		return apiKey;
	}
	
	public static Boolean getDebugMode() {
		if(debugMode == null) {
			try {
				debugMode = config.get("Debug").getAsBoolean();
			} catch(NullPointerException e) {
				System.out.println("SS: No debug mode. Setting to false.");
				toggleDebug(false);
				return getDebugMode();
			
			} catch(IllegalArgumentException e) {
				System.out.println("SS: IAE.");
				return null;
			}

		}
		return debugMode;
	}



	public static void setApiKey(UUID newKey) {
		if(writeConfig("APIKey", newKey.toString())) {
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§7Your API key has been changed to §6" + newKey + "§7."));
			apiKey = newKey;
		} else {
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§cSomething went wrong."));
		}
	}

	public static void toggleDebug(boolean mode) {
		if(writeConfig("Debug", mode)) {
			if(mode) {
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§7Debug mode is toggled §aon§7."));
			} else {
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§7Debug mode is toggled §coff§7."));
			}
			debugMode = mode;
		} else {
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§cSomething went wrong."));
		}
	}

}
