package uk.co.jacekk.bukkit.skylandsplus.generation;

import de.sirati97.sb.skylands.BiomesUtil;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class RedSandPopulator extends BlockPopulator {
    public RedSandPopulator() {
    }

    @SuppressWarnings("deprecation")
    public void populate(World world, Random random, Chunk chunk) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                Biome biome = chunk.getBlock(x, 64, z).getBiome();
                if (BiomesUtil.isMesa(biome)) {
                    for (int y = 4; y < 128; y++) {
                        Block block = chunk.getBlock(x, y, z);

                        if (block.getType().equals(Material.SAND)) {
                            block.setData((byte) 1);
                        }

                        if (block.getType().equals(Material.DEAD_BUSH) || block.getType().equals(Material.LONG_GRASS) || block.getType().equals(Material.CACTUS)) {
                            block.setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }
}
