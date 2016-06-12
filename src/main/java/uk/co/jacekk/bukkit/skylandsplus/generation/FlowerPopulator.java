package uk.co.jacekk.bukkit.skylandsplus.generation;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class FlowerPopulator extends BlockPopulator {
    private Random random;

    public FlowerPopulator(World world) {
        this.random = new Random(world.getSeed());
    }


    public void populate(World world, Random random, Chunk chunk) {
        //No longer in use. code for 1.8


//        List<Biome> iceBiomes = Arrays.asList(Biome.TAIGA, Biome.TAIGA_HILLS, Biome.ICE_PLAINS, Biome.ICE_MOUNTAINS, Biome.FROZEN_RIVER, Biome.FROZEN_OCEAN);
//        for (int x = 0; x < 16; x++) {
//            for (int z = 0; z < 16; z++) {
//                for (int y = 0; y < 128; y++) {
//                    if (y > 4) {
//                        Block block = chunk.getBlock(x, y, z);
//                        Block ground = block.getRelative(BlockFace.DOWN);
//                        Biome biome = block.getBiome();
//                        if (ground.getType() == Material.GRASS) {
//                            if (biome == Biome.PLAINS) {
//                                if (this.random.nextInt(100) < 7) {
//                                    block.setType(this.random.nextInt(100) < 75 ? Material.YELLOW_FLOWER : Material.RED_ROSE);
//                                }
//                            } else if (biome == Biome.FLOWER_FOREST) {
//                                if (this.random.nextInt(100) < 12) {
//                                    block.setType(this.random.nextInt(100) < 75 ? Material.YELLOW_FLOWER : Material.RED_ROSE);
//                                }
//                            } else if (biome == Biome.SUNFLOWER_PLAINS) {
//                                if (this.random.nextInt(100) < 12) {
//                                    block.setType(Material.DOUBLE_PLANT);
//                                    Block blockAbove = block.getRelative(BlockFace.UP);
//                                    blockAbove.setType(Material.DOUBLE_PLANT);
//                                    blockAbove.setData((byte) 8);
//                                }
//                            } else if ((biome != Biome.DESERT) && (!iceBiomes.contains(biome)) &&
//                                    (this.random.nextInt(100) < 2)) {
//                                block.setType(this.random.nextInt(100) < 75 ? Material.YELLOW_FLOWER : Material.RED_ROSE);
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }
}
