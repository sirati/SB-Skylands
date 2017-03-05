package de.sirati97.sb.skylands.gen.pop;

import net.minecraft.server.v1_11_R1.BlockPosition;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_11_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.generator.BlockPopulator;
import uk.co.jacekk.bukkit.skylandsplus.generation.SkylandsGenerator;

import java.util.Random;

/**
 * Created by sirati97 on 02.06.2016.
 */
public class SkyGlowstonePopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        net.minecraft.server.v1_11_R1.World nmsWorld = ((CraftWorld)world).getHandle();
        for (int i = 0; i < random.nextInt(3); i++) {
            Block block = chunk.getBlock(random.nextInt(15), 0, random.nextInt(15));
            int yCave = random.nextInt(7)+(random.nextBoolean()?2:1);
            boolean lastAir = block.getType().equals(Material.AIR);
            while (block.getY()<150) {
                block = block.getRelative(BlockFace.UP);
                Material mat = block.getType();
                if (lastAir && checkMaterial(mat)) {
                    yCave--;
                    lastAir = false;
                    if (yCave == 0) {
                        break;
                    }
                } else {
                    lastAir = mat.equals(Material.AIR);
                }
            }
            if (yCave == 0) {
                SkylandsGenerator.worldGenSkyGlowstone.generate(nmsWorld, random, new BlockPosition(block.getX(), block.getY()-1, block.getZ()));
            }
        }
        net.minecraft.server.v1_11_R1.Chunk c = ((CraftChunk) chunk).getHandle();
        c.initLighting();
    }

    private boolean checkMaterial(Material mat) {
        return mat.equals(Material.STONE) ||mat.equals(Material.DIRT) ||mat.equals(Material.GRASS) ||mat.equals(Material.MYCEL) ||mat.equals(Material.SAND) ||mat.equals(Material.SANDSTONE) ||mat.equals(Material.GRAVEL) ||mat.equals(Material.COBBLESTONE);
    }
}
