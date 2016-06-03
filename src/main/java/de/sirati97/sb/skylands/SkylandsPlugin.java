package de.sirati97.sb.skylands;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.jacekk.bukkit.skylandsplus.generation.SkylandsGenerator;
import uk.co.jacekk.bukkit.skylandsplus.listeners.MobSpawnListener;
import uk.co.jacekk.bukkit.skylandsplus.listeners.PhysicsListener;

import java.io.File;
import java.io.IOException;

/**
 * Created by sirati97 on 31.05.2016.
 */
public class SkylandsPlugin extends JavaPlugin implements Listener {
    private YamlConfiguration config;
    private File configFile;


    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            config = new YamlConfiguration();
            config.set("prevent-sand-falling", true);
            config.set("restrict-mob-spawning", true);
            try {
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            config = YamlConfiguration.loadConfiguration(configFile);
        }


        Configuration conf = getConfig();
		if (conf.getBoolean("prevent-sand-falling", true)) {
			Bukkit.getPluginManager().registerEvents(new PhysicsListener(), this);
		}

		if (conf.getBoolean("restrict-mob-spawning", true)) {
            Bukkit.getPluginManager().registerEvents(new MobSpawnListener(), this);
		}
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        if(id == null || id.isEmpty()){
            return new SkylandsGenerator(20, 145, true);
        } else {
            return new SkylandsGenerator(id);
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginLoad(PluginEnableEvent enableEvent) {
        if (enableEvent.getPlugin().getName().equals("SB-Plots")) {
            if(config.contains("sb-plots")) {
                String worldName = config.getString("sb-plots.name");
                long seed = config.getLong("sb-plots.seed");
                if (Strings.isNullOrEmpty(worldName)) {
                    throw new IllegalStateException("Invalid world name!");
                } else {
                    PlotsIntegration integration = new PlotsIntegration();
                    integration.registerWorld(this, enableEvent.getPlugin(), worldName, seed);
                }

            }


        }
    }
}
