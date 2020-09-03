package com.ss.commands;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.text.WordUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ss.apireaders.Profiles;
import com.ss.commands.SstatsThread;
import com.ss.info.PetLevels;
import com.ss.info.PlayerAPI;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.GuildReply.Guild;
import net.hypixel.api.util.ILeveling;
import net.hypixel.api.reply.skyblock.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NamedTag;
import java.util.Arrays;

public class SStats implements ICommand {

	@Override
	public int compareTo(ICommand o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return "skystats";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "Lookup stats of players in skyblock.";
	}

	@Override
	public List<String> getCommandAliases() {
		List<String> alias = new ArrayList();
		alias.add("ss");
		return alias;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {


		if(args.length == 0) {
			sender.addChatMessage(new ChatComponentTranslation("§e/ss [player] <profile> <page> §8- §7Lookup skyblock stats of player."));
		} else {
			switch(args.length) {
			case 1:
				sender.addChatMessage(new ChatComponentTranslation("§7Attempting to look up stats of §6" + args[0] + "§7..."));
				break;
			case 2:
				if(args[1].matches("-?(0|[1-9]\\d*)")) {
					if(args[1].equals("2") || args[1].equals("1")) {
						sender.addChatMessage(new ChatComponentTranslation("§7Attempting to load second page of §6" + args[0] + "§7's stats..."));
					} else {
						sender.addChatMessage(new ChatComponentTranslation("§cInvalid page."));
					}
				} else {
					sender.addChatMessage(new ChatComponentTranslation("§7Attempting to look up stats of §6" + args[0] + "§7's profile §d" + args[1] + " §7..."));
				}
				break;
			case 3:
				String profileName = null;
				String pageNumber = null;

				for(String arg : args) {
					if(!arg.equals(args[0])) {
						if(arg.matches("-?(0|[1-9]\\d*)")) pageNumber = arg;
						else profileName = arg;
					}
				}

				if(profileName != null && pageNumber != null) {
					if(pageNumber.equals("2") || pageNumber.equals("1")) {
						sender.addChatMessage(new ChatComponentTranslation("§7Attempting to load second page of §6" + args[0] + "§7's profile §d" + profileName + "§7's stats..."));
					} else {
						sender.addChatMessage(new ChatComponentTranslation("§cInvalid page."));
					}
				}
				break;
			}
			SstatsThread sbsThread = new SstatsThread(sender, args);
			sbsThread.start();
		}



	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {

		
		if(args.length == 1) {
		String firstArg = args[0];


		ArrayList<String> players = new ArrayList<>();
		Collection<NetworkPlayerInfo> playersC = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap();
		playersC.forEach((loadedPlayer) -> {
			if(!firstArg.isEmpty()) {
				for(int character = 0; character < firstArg.toCharArray().length ; character++) {
					if(!(loadedPlayer.getGameProfile().getName().toLowerCase().charAt(character) == firstArg.toLowerCase().toCharArray()[character])) {
						break;
					}
					if(character == firstArg.toCharArray().length - 1) {
						players.add(loadedPlayer.getGameProfile().getName());
					}
				}
			} else {
				players.add(loadedPlayer.getGameProfile().getName());
			}
		});
		return players;
		
		} else return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		// TODO Auto-generated method stub
		return false;
	}



}




