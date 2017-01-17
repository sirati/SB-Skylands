package uk.co.jacekk.bukkit.skylandsplus.generation;

import de.sirati97.sb.skylands.BiomesUtil;
import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.Blocks;
import net.minecraft.server.v1_11_R1.WorldGenLakes;
import net.minecraft.server.v1_11_R1.WorldGenReed;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class LakePopulator
        extends BlockPopulator {
    private Random random;

    public LakePopulator(org.bukkit.World world) {
        this.random = new Random(world.getSeed());
    }

    public void populate(org.bukkit.World world, Random random, Chunk chunk) {
        net.minecraft.server.v1_11_R1.World mcWorld = ((CraftWorld) world).getHandle();

        int worldChunkX = chunk.getX() * 16;
        int worldChunkZ = chunk.getZ() * 16;
        if (this.random.nextInt(4) == 0) {
            int x = worldChunkX + this.random.nextInt(16) + 8;
            int z = worldChunkZ + this.random.nextInt(16) + 8;
            if (BiomesUtil.isJungle(world.getBiome(x, z))) {
                int y = world.getHighestBlockYAt(x, z) + 2;
                BlockPosition pos = new BlockPosition(x, y, z);
                if (this.random.nextInt(100) < 85) {
                    WorldGenLakes wgl = new WorldGenLakes(Blocks.WATER);
                    wgl.generate(mcWorld, this.random, pos);
                    WorldGenReed wgr = new WorldGenReed();
                    wgr.generate(mcWorld, this.random, pos);
                } else {
                    WorldGenLakes wgl = new WorldGenLakes(Blocks.LAVA);
                    wgl.generate(mcWorld, this.random, pos);
                }
            }
        }
    }
}
