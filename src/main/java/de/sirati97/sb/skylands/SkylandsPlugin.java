package de.sirati97.sb.skylands;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.jacekk.bukkit.skylandsplus.listeners.MobSpawnListener;
import uk.co.jacekk.bukkit.skylandsplus.listeners.PhysicsListener;

/**
 * Created by sirati97 on 31.05.2016.
 */
public class SkylandsPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        Configuration conf = getConfig();
		if (conf.getBoolean("prevent-sand-falling")) {
			Bukkit.getPluginManager().registerEvents(new PhysicsListener(), this);
		}

		if (conf.getBoolean("restrict-mob-spawning")) {
            Bukkit.getPluginManager().registerEvents(new MobSpawnListener(), this);
		}
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        if(id == null || id.isEmpty()){
            id = "offset=0";
        }

        return new uk.co.jacekk.bukkit.skylandsplus.generation.ChunkGenerator(id);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginLoad(PluginEnableEvent enableEvent) {
        if (enableEvent.getPlugin().getName().equals("SB-Plots")) {
            PlotsIntegration integration = new PlotsIntegration();
            integration.registerWorld(this, enableEvent.getPlugin(), "invokedskylands", 1435, null);
        }
    }
}
