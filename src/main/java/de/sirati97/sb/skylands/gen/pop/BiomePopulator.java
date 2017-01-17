package de.sirati97.sb.skylands.gen.pop;

import de.sirati97.sb.skylands.gen.biome.nms.BiomeJungleFix;
import net.minecraft.server.v1_11_R1.BiomeBase;
import net.minecraft.server.v1_11_R1.BiomeJungle;
import net.minecraft.server.v1_11_R1.BlockPosition;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;


public class BiomePopulator extends BlockPopulator {
	
	@Override
	public void populate(World world, Random random, Chunk chunk) {
		net.minecraft.server.v1_11_R1.World nmsWorld = ((CraftWorld)world).getHandle();
		BlockPosition position = new BlockPosition(chunk.getX() * 16, 80, chunk.getZ() * 16);
		BiomeBase biomeBase = nmsWorld.getBiome(position);
        biomeBase = biomeBase instanceof BiomeJungle? BiomeJungleFix.getFix((BiomeJungle) biomeBase):biomeBase;
		try {
			biomeBase.a(nmsWorld, random, position);
		}catch (RuntimeException e){
			e.printStackTrace();
			// Decorator was already called on this chunk :/
		}
	}
	
}