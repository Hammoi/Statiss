package com.ss.commands;

import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import java.util.Arrays;

public class Sshelp implements ICommand {

	@Override
	public int compareTo(ICommand o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender arg0, String[] arg1, BlockPos arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public List<String> getCommandAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList(new String[] {"ssh","sshelp"});
	}

	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return "skystatshelp";
	}

	@Override
	public String getCommandUsage(ICommandSender arg0) {
		// TODO Auto-generated method stub
		return "Shows all SkyStats commands and usage.";
	}

	@Override
	public boolean isUsernameIndex(String[] arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void processCommand(ICommandSender arg0, String[] arg1) throws CommandException {
		EntityPlayer player = (EntityPlayer) arg0;
		player.addChatMessage(new ChatComponentTranslation("§8--------------------------------\n"
				+ "§6SkyStats Commands\n"
				+ "§e   /skystats [player name] §7- §8Lookup Skyblock stats of player.\n"
				+ "§e      §7Alias: §e/ss\n"
				+ "§e   /skystatscontrols §7- §8SkyStats control panel.\n"
				+ "§e      §7Alias: §e/ssc\n"
				+ "§8--------------------------------"));

	}

}
