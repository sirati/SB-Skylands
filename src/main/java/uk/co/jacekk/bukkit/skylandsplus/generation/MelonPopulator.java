package uk.co.jacekk.bukkit.skylandsplus.generation;

import de.sirati97.sb.skylands.BiomesUtil;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class MelonPopulator
  extends BlockPopulator
{
  private Random random;
  
  public MelonPopulator(World world)
  {
    this.random = new Random(world.getSeed());
  }
  
  @SuppressWarnings("deprecation")
public void populate(World world, Random random, Chunk chunk)
  {
    int worldChunkX = chunk.getX() * 16;
    int worldChunkZ = chunk.getZ() * 16;
    Biome b = world.getBiome(worldChunkX, worldChunkZ);
    
    
    if (this.random.nextInt(100) == 0 && BiomesUtil.isJungle(b))
    {
      int x = worldChunkX + this.random.nextInt(16) + 8;
      int z = worldChunkZ + this.random.nextInt(16) + 8;
      int y = world.getHighestBlockYAt(x, z);
      for (int i = 0; i < 64; i++)
      {
        int j = x + this.random.nextInt(8) - this.random.nextInt(8);
        int k = y + this.random.nextInt(4) - this.random.nextInt(4);
        int m = z + this.random.nextInt(8) - this.random.nextInt(8);
        
        Block block = world.getBlockAt(j, k, m);
        Block below = world.getBlockAt(j, k - 1, m);
        if ((block.getType() == Material.AIR) && (below.getType() == Material.GRASS)) {
          block.setTypeIdAndData(Material.MELON_BLOCK.getId(), (byte)this.random.nextInt(4), false);
        }
      }
    }
  }
}
