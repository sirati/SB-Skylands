package de.sirati97.sb.skylands.gen.pop.nms;

import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.Blocks;
import net.minecraft.server.v1_11_R1.EnumDirection;
import net.minecraft.server.v1_11_R1.Material;
import net.minecraft.server.v1_11_R1.World;
import net.minecraft.server.v1_11_R1.WorldGenerator;

import java.util.Random;

/**
 * Created by sirati97 on 02.06.2016.
 */
public class WorldGenSkyGlowstone extends WorldGenerator {

    public boolean generate(World var1, Random var2, BlockPosition var3) {
        if(!var1.isEmpty(var3)) {
            return false;
        } else {
            var1.setTypeAndData(var3, Blocks.GLOWSTONE.getBlockData(), 2);

            for(int var4 = 0; var4 < 1300; ++var4) {
                BlockPosition var5 = var3.a(var2.nextInt(8) - var2.nextInt(8), -var2.nextInt(12), var2.nextInt(8) - var2.nextInt(8));
                if(var1.getType(var5).getMaterial() == Material.AIR) {
                    int var6 = 0;
                    EnumDirection[] var7 = EnumDirection.values();
                    int var8 = var7.length;

                    for(int var9 = 0; var9 < var8; ++var9) {
                        EnumDirection var10 = var7[var9];
                        if(var1.getType(var5.shift(var10)).getBlock() == Blocks.GLOWSTONE) {
                            ++var6;
                        }

                        if(var6 > 1) {
                            break;
                        }
                    }

                    if(var6 == 1) {
                        var1.setTypeAndData(var5, Blocks.GLOWSTONE.getBlockData(), 2);
                    }
                }
            }

            return true;
        }
    }
}
