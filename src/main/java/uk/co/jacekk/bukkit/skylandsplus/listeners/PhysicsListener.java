package uk.co.jacekk.bukkit.skylandsplus.listeners;

import de.sirati97.sb.skylands.gen.multicore.MultiCoreGenerator;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.generator.ChunkGenerator;
import uk.co.jacekk.bukkit.skylandsplus.generation.SkylandsGenerator;

public class PhysicsListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPhysics(BlockPhysicsEvent event){
		Material changed = event.getChangedType();
		
		if (changed == Material.SAND || changed == Material.GRAVEL){
            ChunkGenerator generator = event.getBlock().getWorld().getGenerator();
			if (generator instanceof SkylandsGenerator || (generator instanceof MultiCoreGenerator && ((MultiCoreGenerator) generator).getParent() instanceof SkylandsGenerator)){
				event.setCancelled(true);
			}
		}
	}
	
}
