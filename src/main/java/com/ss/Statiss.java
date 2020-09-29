package com.ss;

import java.io.File;
import java.io.FileNotFoundException;

import com.ss.commands.SStats;
import com.ss.commands.Sscontrols;
import com.ss.commands.Sshelp;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "statiss", name = "Statiss", version = "0.9.3")
public class Statiss {

	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	
    	Config.setUpConfig(event.getSuggestedConfigurationFile());
    	
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	FMLCommonHandler.instance().bus().register(this);
        ClientCommandHandler.instance.registerCommand(new SStats());
        ClientCommandHandler.instance.registerCommand(new Sscontrols());
        ClientCommandHandler.instance.registerCommand(new Sshelp());
    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	
    }
  
}

