package com.github.rotlug;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

public class TickTimer {
    public static int COOLDOWN_SECONDS = 10; // Number of seconds before player is no longer considered in combat
    private static int timer = COOLDOWN_SECONDS * 20;
    private static boolean isRunning = false;

    public static void register() {
        ServerTickEvents.START_SERVER_TICK.register(TickTimer::onServerTick);
    }

    private static void onServerTick(MinecraftServer server) {
        if (isRunning) {
            if (timer > 0) timer--;
            else {
                BossKeepInventory.setKeepInventory(false);
                stop();
            }
        }
    }

    public static void resetTimer() {
        timer = COOLDOWN_SECONDS * 20;
    }

    public static void start() {
        isRunning = true;
    }

    public static void stop() {
        isRunning = false;
    }
}