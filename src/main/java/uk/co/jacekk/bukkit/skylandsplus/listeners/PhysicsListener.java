package uk.co.jacekk.bukkit.skylandsplus.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import uk.co.jacekk.bukkit.skylandsplus.generation.SkylandsGenerator;

public class PhysicsListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockPhysics(BlockPhysicsEvent event){
		Material changed = event.getChangedType();
		
		if (changed == Material.SAND || changed == Material.GRAVEL){
			if (event.getBlock().getWorld().getGenerator() instanceof SkylandsGenerator){
				event.setCancelled(true);
			}
		}
	}
	
}
