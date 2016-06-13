package de.sirati97.sb.skylands;

import de.sirati97.sb.skylands.gen.multicore.MultiCoreGenerator;
import de.sirati97.sb.skylands.listener.LessLagListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.entity.Player;
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
    public static boolean serverStarted = false;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new LessLagListener(), this);
        Bukkit.getScheduler().runTask(this, new Runnable() {
            @Override
            public void run() {
                serverStarted = true;
            }
        });

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
            return new SkylandsGenerator(this, 20, 128, true);
        } else {
            String[] args = id.split("[,]");
            boolean multiCore = false;
            for(String arg:args) {
                if (arg.equalsIgnoreCase("multicore")) {
                    multiCore = true;
                    break;
                }
            }
            SkylandsGenerator skylandsGenerator = new SkylandsGenerator(this, args);
            return multiCore ? new MultiCoreGenerator(skylandsGenerator, this) : skylandsGenerator;
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("debug-multicore-generation")) {
            World w;
            if (args.length > 0) {
                w = Bukkit.getWorld(args[0]);
                if (w == null) {
                    sender.sendMessage("There is no world with the name " +args[0]);
                    return true;
                }
            } else if(sender instanceof Player) {
                w = ((Player) sender).getWorld();
            } else {
                sender.sendMessage("You need to specify a world!");
                return true;
            }
            ChunkGenerator generator = w.getGenerator();
            if (generator instanceof MultiCoreGenerator) {
                ((MultiCoreGenerator) generator).sendDebug(sender);
            } else {
                sender.sendMessage("The world " + w.getName() + " does not use the multicore chunk generator.");
            }
            return true;
        }

        return super.onCommand(sender, command, label, args);
    }
}
