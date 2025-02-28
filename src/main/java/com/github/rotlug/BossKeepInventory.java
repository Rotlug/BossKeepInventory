package com.github.rotlug;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BossKeepInventory implements ModInitializer {
	public static final String MOD_ID = "boss-keep-inventory";
	private static MinecraftServer server;

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ConfigManager.loadConfig();

		ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStart);
		TickTimer.register();
	}

	public static void setKeepInventory(boolean value) {
		if (!ConfigManager.enabled) return;

		if (server != null) {
			if (ConfigManager.debug) server.getPlayerManager().broadcast(Text.literal("Player Advantage " + (value ? "ON" : "OFF")), false);
			server.getGameRules().get(GameRules.KEEP_INVENTORY).set(value, server);
			server.getGameRules().get(GameRules.DO_MOB_SPAWNING).set(!value, server);
		}
	}

	private void onServerStart(MinecraftServer mcServer) {
		ConfigManager.loadConfig();
		server = mcServer;
		setKeepInventory(false);
	}
}