package de.sirati97.sb.skylands;

import de.sirati97.sb.plots.PlotPlugin;
import de.sirati97.sb.plots.portals.PortalWorld;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;
import uk.co.jacekk.bukkit.skylandsplus.generation.ChunkGenerator;

/**
 * Created by sirati97 on 31.05.2016.
 */
public class PlotsIntegration {

    public void registerWorld(Plugin plugin, Plugin plotPluginUncasted, String name, long seed, String settings) {
        if(settings == null || settings.isEmpty()){
            settings = "offset=0";
        }
        WorldCreator worldCreator = new WorldCreator(name);
        worldCreator.seed(seed);
        worldCreator.generator(new ChunkGenerator(settings));

        World skylands = Bukkit.createWorld(worldCreator);
        PlotPlugin plotPlugin = (PlotPlugin)plotPluginUncasted;
        plotPlugin.getPortalManager().registerPortal(new PortalWorld("FarmSkylands", Material.GLOWSTONE, PortalWorld.GROUND_BLOCKS_OVERWORLD, skylands, false));

    }
}
