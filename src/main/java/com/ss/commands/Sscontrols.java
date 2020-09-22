package com.ss.commands;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.zip.GZIPInputStream;


import com.google.common.io.BaseEncoding.DecodingException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ss.Config;
import com.ss.info.PlayerAPI;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.GuildReply.Guild;
import net.hypixel.api.util.ILeveling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraft.nbt.CompressedStreamTools;
import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import java.util.Arrays;
import scala.util.parsing.json.JSONObject;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class Sscontrols implements ICommand {

	@Override
	public int compareTo(ICommand o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return "sscontrol";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "SkyStats Control Panel.";
	}

	@Override
	public List<String> getCommandAliases() {
		List<String> alias = new ArrayList();
		alias.add("ssc");
		return alias;
	}

	private void sendMessage(EntityPlayer player, String string) {
		player.addChatMessage(new ChatComponentTranslation(string));
	}

	private void sendMessages(EntityPlayer player, String[] strings) {
		for(int x = 0; x<strings.length; x++) {
			player.addChatMessage(new ChatComponentTranslation(strings[x]));
		}

	}

	private String stringTest = new String();

	public String getString() {
		return stringTest;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		EntityPlayer player = (EntityPlayer) sender;
		String[] help = {"§e/sscontrol restartapi §8- §7Restarts the API Reader.",
				"§e/sscontrol setApiKey [key] §8- §7Sets the Hypixel API key.",
		"§e/sscontrol getApiKey §8- §7Returns your current API key.",
		"§e/sscontrol toggleDebug §8- §7Toggles debug mode."};
		if(args.length == 0) {
			sendMessages(player, help);
		} else {
			switch(args[0].toLowerCase()) {
			case "restartapi":
				try {
					PlayerAPI.shutdownAPI();
					sendMessage(player, "§aAPI successfully restarted.");
				} catch(NullPointerException e) {
					sendMessage(player, "§cAPI is not started.");
				}
				break;
			case "setapikey":
				if(args.length >= 2) {
					try {
						Config.setApiKey(UUID.fromString(args[1]));
					} catch(IllegalArgumentException e) {
						sendMessage(player, "§cInvalid API Key. To generate a new one, use /api new while on Hypixel.");
					}
				} else {
					sendMessage(player, "§c/ssc setApiKey [key]");
				}
				break;
			case "getapikey":
				if(Config.getApiKey() != null) {
					
					IChatComponent componentText = new ChatComponentText("§7Your active API key is: §6" + Config.getApiKey());

					ChatStyle style = new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Config.getApiKey().toString()))
							.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentTranslation("§7Click this to put your active API key into your chat bar.")));;
					componentText.setChatStyle(style);

					player.addChatMessage(componentText);
				} else {
					sendMessage(player, "§cYou have not set an API key. Set one using /ssc setApiKey [key].");
				}
				break;
			case "toggledebug":
				System.out.println(Config.getDebugMode());
				if(Config.getDebugMode()) {
					Config.toggleDebug(false);
				} else {
					Config.toggleDebug(true);
				}
				
				
				break;
			default:
				sendMessages(player, help);
				break;
			}


		}


	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {

		if(args.length <= 1) {
			String firstArg = args[0];

			ArrayList<String> commands = new ArrayList<>(Arrays.asList(new String[] {"restartApi", "setApiKey", "getApiKey", "toggleDebug"}));
			if(!firstArg.isEmpty()) {
				ArrayList<String> validCommands = new ArrayList<>();

				commands.forEach((command) -> {
					for(int character = 0; character < firstArg.toCharArray().length ; character++) {
						if(!(command.toString().toLowerCase().charAt(character) == firstArg.toLowerCase().toCharArray()[character])) {
							break;
						}
						if(character == firstArg.toCharArray().length - 1) {
							validCommands.add(command.toString());
						}
					}
				});
				return validCommands;
			} else {
				return commands;
			}

		} else return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		// TODO Auto-generated method stub
		return false;
	}

}
