package de.sirati97.sb.skylands;

import de.sirati97.sb.plots.PlotPlugin;
import de.sirati97.sb.plots.portals.PortalWorld;
import de.sirati97.sb.skylands.gen.multicore.MultiCoreGenerator;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import uk.co.jacekk.bukkit.skylandsplus.generation.SkylandsGenerator;

/**
 * Created by sirati97 on 31.05.2016.
 */
public class PlotsIntegration {

    public void registerWorld(Plugin plugin, Plugin plotPluginUncasted, String name, long seed) {
        PlotPlugin plotPlugin = (PlotPlugin)plotPluginUncasted;

        ChunkGenerator generator = new MultiCoreGenerator(new SkylandsGenerator(plugin, 20, 145, true), plugin);

        plotPlugin.getWorldManager().loadWorld(name, "FarmSkylands", Material.GLOWSTONE, PortalWorld.GROUND_BLOCKS_OVERWORLD, seed, generator, plugin.getName(), "offset=20,high=145,multicore", World.Environment.NORMAL);


    }
}
