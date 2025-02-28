package com.github.rotlug;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "boss_keep_inventory.json");

    // Default values
    public static List<String> bossMobs = List.of();
    public static boolean enabled = true;
    public static boolean disableMobSpawning = true;
    public static boolean debug = false;

    public static void loadConfig() {
        if (!CONFIG_FILE.exists()) {
            saveDefaultConfig(); // Create a default config if it doesn't exist
        }

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            if (json.has("bossMobs")) {
                bossMobs = Arrays.asList(GSON.fromJson(json.get("bossMobs"), String[].class));
                enabled = GSON.fromJson(json.get("enabled"), Boolean.class);
                disableMobSpawning = GSON.fromJson(json.get("disableMobSpawning"), Boolean.class);
                debug = GSON.fromJson(json.get("debug"), Boolean.class);
            }
        } catch (Exception e) {
            BossKeepInventory.LOGGER.error("Failed to read config file!");
        }
    }

    private static void saveDefaultConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            JsonObject json = new JsonObject();
            json.add("bossMobs", GSON.toJsonTree(bossMobs));
            json.add("enabled", GSON.toJsonTree(enabled));
            json.add("disableMobSpawning", GSON.toJsonTree(disableMobSpawning));
            json.add("debug", GSON.toJsonTree(debug));
            writer.write(GSON.toJson(json));
        } catch (Exception e) {
            BossKeepInventory.LOGGER.error("Failed to write config file!");
        }
    }
}