package uk.co.jacekk.bukkit.skylandsplus.generation;

import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;


public class BiomePopulator extends BlockPopulator {
	
	@Override
	public void populate(World world, Random random, Chunk chunk) {
		net.minecraft.server.v1_8_R3.World nmsWorld = ((CraftWorld)world).getHandle();
		BlockPosition position = new BlockPosition(chunk.getX() * 16, 80, chunk.getZ() * 16);
		BiomeBase biomeBase = nmsWorld.getBiome(position);
		try {
			biomeBase.a(nmsWorld, random, position);
		}catch (RuntimeException e){
			e.printStackTrace();
			// Decorator was already called on this chunk :/
		}
	}
	
}