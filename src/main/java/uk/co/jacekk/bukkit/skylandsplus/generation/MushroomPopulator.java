package uk.co.jacekk.bukkit.skylandsplus.generation;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class MushroomPopulator
        extends BlockPopulator {
    private Random random;

    public MushroomPopulator(World world) {
        this.random = new Random(world.getSeed());
    }

    public void populate(World world, Random random, Chunk chunk) {
        //No longer in use. code for 1.8


//        int worldChunkX = chunk.getX() * 16;
//        int worldChunkZ = chunk.getZ() * 16;
//        for (int x = 0; x < 16; x++) {
//            for (int z = 0; z < 16; z++) {
//                if (world.getBiome(worldChunkX + x, worldChunkZ + z) == Biome.SWAMPLAND) {
//                    for (int y = 128; y > 0; y--) {
//                        Block block = chunk.getBlock(x, y, z);
//                        Block ground = block.getRelative(BlockFace.DOWN);
//                        if ((block.getType() == Material.AIR) && (ground.getType() == Material.GRASS) && (block.getLightLevel() <= 8) && (this.random.nextInt(100) < 5) &&
//                                (world.getHighestBlockYAt(x + worldChunkX, z + worldChunkZ) > y)) {
//                            block.setType(this.random.nextInt(100) < 20 ? Material.RED_MUSHROOM : Material.BROWN_MUSHROOM);
//                        }
//                    }
//                }
//            }
//        }
    }
}
