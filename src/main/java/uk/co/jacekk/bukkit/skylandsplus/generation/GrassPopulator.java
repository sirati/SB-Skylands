package uk.co.jacekk.bukkit.skylandsplus.generation;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class GrassPopulator
        extends BlockPopulator {
    private Random random;

    public GrassPopulator(World world) {
        this.random = new Random(world.getSeed());
    }

    @SuppressWarnings("deprecation")
    public void populate(World world, Random random, Chunk chunk) {
        //No longer in use. code for 1.8


//        List<Biome> iceBiomes = Arrays.asList(Biome.TAIGA, Biome.TAIGA_HILLS, Biome.ICE_PLAINS, Biome.ICE_MOUNTAINS, Biome.FROZEN_RIVER, Biome.FROZEN_OCEAN);
//
//        int worldChunkX = chunk.getX() * 16;
//        int worldChunkZ = chunk.getZ() * 16;
//        for (int x = 0; x < 16; x++) {
//            for (int z = 0; z < 16; z++) {
//                int y = world.getHighestBlockYAt(worldChunkX + x, worldChunkZ + z);
//                if (y > 5) {
//                    Block block = chunk.getBlock(x, y, z);
//                    Block ground = block.getRelative(BlockFace.DOWN);
//
//                    Biome biome = world.getBiome(worldChunkX + x, worldChunkZ + z);
//                    if ((ground.getType() == Material.GRASS)) {
//                        if (biome == Biome.PLAINS || biome == Biome.SUNFLOWER_PLAINS || biome == Biome.SAVANNA || biome == Biome.SAVANNA_MOUNTAINS || biome == Biome.SAVANNA_PLATEAU || biome == Biome.SAVANNA_PLATEAU_MOUNTAINS) {
//                            if (this.random.nextInt(100) < 35) {
//                                block.setType(Material.LONG_GRASS);
//                                block.setData((byte) 1);
//                            }
//                        } else if ((biome == Biome.TAIGA) || (biome == Biome.TAIGA_HILLS)) {
//                            if (this.random.nextInt(100) < 1) {
//                                block.setType(Material.LONG_GRASS);
//                                block.setData((byte) 2);
//                            }
//                        } else if (iceBiomes.contains(biome)) {
//                            if (this.random.nextInt(100) < 1) {
//                                block.setType(Material.LONG_GRASS);
//                                block.setData((byte) 1);
//                            }
//                        } else if (this.random.nextInt(100) < 14) {
//                            block.setType(Material.LONG_GRASS);
//                            block.setData((byte) 1);
//                            block.setData((byte) (this.random.nextInt(100) < 85 ? 1 : 2));
//                        }
//                    } else if (ground.getType() == Material.SAND) {
//                        if (biome == Biome.DESERT || biome == Biome.DESERT_HILLS || biome == Biome.DESERT_MOUNTAINS || biome == Biome.MESA || biome == Biome.MESA_BRYCE || biome == Biome.MESA_PLATEAU || biome == Biome.MESA_PLATEAU_FOREST || biome == Biome.MESA_PLATEAU_FOREST_MOUNTAINS || biome == Biome.MESA_PLATEAU_MOUNTAINS) {
//                            if (this.random.nextInt(100) < 3) {
//                                block.setType(Material.DEAD_BUSH);
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }
}
