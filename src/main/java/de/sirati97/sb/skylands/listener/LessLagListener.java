package de.sirati97.sb.skylands.listener;

import de.sirati97.sb.skylands.gen.multicore.MultiCoreGenerator;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.generator.ChunkGenerator;
import uk.co.jacekk.bukkit.skylandsplus.generation.SkylandsGenerator;

/**
 * Created by sirati97 on 13.06.2016.
 */
public class LessLagListener implements Listener {


    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent event){
        Material type = event.getBlock().getType();

        if (type == Material.SAND || type == Material.GRAVEL){
            ChunkGenerator generatorBase = event.getBlock().getWorld().getGenerator();
            SkylandsGenerator generator = null;
            if (generatorBase instanceof SkylandsGenerator) {
                generator = (SkylandsGenerator) generatorBase;
            } else if (generatorBase instanceof MultiCoreGenerator && ((MultiCoreGenerator) generatorBase).getParent() instanceof SkylandsGenerator){
                generator = (SkylandsGenerator) ((MultiCoreGenerator) generatorBase).getParent();
            }

            if (generator != null) {
                if (generator.wasShortlyLoaded(event.getBlock().getChunk())) {
                    Block to = event.getBlock().getRelative(BlockFace.DOWN);
                    if (!to.getType().isSolid()) {
                        Block from = event.getBlock();
                        event.setCancelled(true);
                        moveBlocks(from, type, to);
                    }
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityChangeBlockEvent(EntitySpawnEvent event) {
        if (event.getEntityType() == EntityType.FALLING_BLOCK) {
            FallingBlock fallingBlock = (FallingBlock) event.getEntity();
            Material type = fallingBlock.getMaterial();

            if (type == Material.SAND || type == Material.GRAVEL) {
                ChunkGenerator generatorBase = fallingBlock.getWorld().getGenerator();
                SkylandsGenerator generator = null;
                if (generatorBase instanceof SkylandsGenerator) {
                    generator = (SkylandsGenerator) generatorBase;
                } else if (generatorBase instanceof MultiCoreGenerator && ((MultiCoreGenerator) generatorBase).getParent() instanceof SkylandsGenerator) {
                    generator = (SkylandsGenerator) ((MultiCoreGenerator) generatorBase).getParent();
                }

                if (generator != null) {
                    if (generator.wasShortlyLoaded(fallingBlock.getLocation().getChunk())) {
                        Block from = event.getLocation().getBlock();
                        Block to = from.getRelative(BlockFace.DOWN);
                        if (!to.getType().isSolid()) {
                            event.setCancelled(true);
                            moveBlocks(from, type, to);
                        }
                    }
                }
            }
        }
    }

    private void moveBlocks(Block from, Material fromMat, Block to) {
        while (!to.getType().isSolid() && to.getY() > 1) {
            to = to.getRelative(BlockFace.DOWN);
        }
        byte data = from.getData();
        Material type = fromMat;
        boolean place = to.getType().isSolid();

        do {
            to = to.getRelative(BlockFace.UP);
            from.setType(Material.AIR);
            if (place) {
                to.setType(type);
                to.setData(data);
            }
            from = from.getRelative(BlockFace.UP);
            type = from.getType();
            data = from.getData();
        } while (type == Material.SAND || type == Material.GRAVEL);
    }

}
