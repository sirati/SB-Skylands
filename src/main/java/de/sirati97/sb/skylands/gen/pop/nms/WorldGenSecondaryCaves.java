package de.sirati97.sb.skylands.gen.pop.nms;

import com.google.common.base.Objects;
import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.Blocks;
import net.minecraft.server.v1_11_R1.ChunkSnapshot;
import net.minecraft.server.v1_11_R1.IBlockData;
import net.minecraft.server.v1_11_R1.Material;
import net.minecraft.server.v1_11_R1.MathHelper;
import net.minecraft.server.v1_11_R1.World;
import net.minecraft.server.v1_11_R1.WorldGenBase;

import java.util.Random;

/**
 * Created by sirati97 on 03.06.2016.
 */
public class WorldGenSecondaryCaves extends WorldGenBase {

    protected static final IBlockData a;
    protected static final IBlockData b;
    protected static final IBlockData c;
    protected static final IBlockData d;

    public WorldGenSecondaryCaves() {
    }

    protected void a(long var1, int var3, int var4, ChunkSnapshot var5, double var6, double var8, double var10) {
        this.a(var1, var3, var4, var5, var6, var8, var10, 1.0F + this.f.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    protected void a(long var1, int var3, int var4, ChunkSnapshot var5, double var6, double var8, double var10, float var12, float var13, float var14, int var15, int var16, double var17) {
        double var19 = (double)(var3 * 16 + 8);
        double var21 = (double)(var4 * 16 + 8);
        float var23 = 0.0F;
        float var24 = 0.0F;
        Random var25 = new Random(new Random(var1).nextLong());
        if(var16 <= 0) {
            int var26 = this.e * 16 - 16;
            var16 = var26 - var25.nextInt(var26 / 4);
        }

        boolean var65 = false;
        if(var15 == -1) {
            var15 = var16 / 2;
            var65 = true;
        }

        int var27 = var25.nextInt(var16 / 2) + var16 / 4;

        for(boolean var28 = var25.nextInt(6) == 0; var15 < var16; ++var15) {
            double var29 = 1.5D + (double)(MathHelper.sin((float)var15 * 3.1415927F / (float)var16) * var12);
            double var31 = var29 * var17;
            float var33 = MathHelper.cos(var14);
            float var34 = MathHelper.sin(var14);
            var6 += (double)(MathHelper.cos(var13) * var33);
            var8 += (double)var34;
            var10 += (double)(MathHelper.sin(var13) * var33);
            if(var28) {
                var14 *= 0.92F;
            } else {
                var14 *= 0.7F;
            }

            var14 += var24 * 0.1F;
            var13 += var23 * 0.1F;
            var24 *= 0.9F;
            var23 *= 0.75F;
            var24 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 2.0F;
            var23 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 4.0F;
            if(!var65 && var15 == var27 && var12 > 1.0F && var16 > 0) {
                this.a(var25.nextLong(), var3, var4, var5, var6, var8, var10, var25.nextFloat() * 0.5F + 0.5F, var13 - 1.5707964F, var14 / 3.0F, var15, var16, 1.0D);
                this.a(var25.nextLong(), var3, var4, var5, var6, var8, var10, var25.nextFloat() * 0.5F + 0.5F, var13 + 1.5707964F, var14 / 3.0F, var15, var16, 1.0D);
                return;
            }

            if(var65 || var25.nextInt(4) != 0) {
                double var35 = var6 - var19;
                double var37 = var10 - var21;
                double var39 = (double)(var16 - var15);
                double var41 = (double)(var12 + 2.0F + 16.0F);
                if(var35 * var35 + var37 * var37 - var39 * var39 > var41 * var41) {
                    return;
                }

                if(var6 >= var19 - 16.0D - var29 * 2.0D && var10 >= var21 - 16.0D - var29 * 2.0D && var6 <= var19 + 16.0D + var29 * 2.0D && var10 <= var21 + 16.0D + var29 * 2.0D) {
                    int var43 = MathHelper.floor(var6 - var29) - var3 * 16 - 1;
                    int var44 = MathHelper.floor(var6 + var29) - var3 * 16 + 1;
                    int var45 = MathHelper.floor(var8 - var31) - 1;
                    int var46 = MathHelper.floor(var8 + var31) + 1;
                    int var47 = MathHelper.floor(var10 - var29) - var4 * 16 - 1;
                    int var48 = MathHelper.floor(var10 + var29) - var4 * 16 + 1;
                    if(var43 < 0) {
                        var43 = 0;
                    }

                    if(var44 > 16) {
                        var44 = 16;
                    }

                    if(var45 < 1) {
                        var45 = 1;
                    }

                    if(var46 > 248) {
                        var46 = 248;
                    }

                    if(var47 < 0) {
                        var47 = 0;
                    }

                    if(var48 > 16) {
                        var48 = 16;
                    }

                    boolean var49 = false;

                    int var51;
                    for(int var50 = var43; !var49 && var50 < var44; ++var50) {
                        for(var51 = var47; !var49 && var51 < var48; ++var51) {
                            for(int var52 = var46 + 1; !var49 && var52 >= var45 - 1; --var52) {
                                if(var52 >= 0 && var52 < 256) {
                                    IBlockData var53 = var5.a(var50, var52, var51);
                                    if(var53.getBlock() == Blocks.FLOWING_WATER || var53.getBlock() == Blocks.WATER) {
                                        var49 = true;
                                    }

                                    if(var52 != var45 - 1 && var50 != var43 && var50 != var44 - 1 && var51 != var47 && var51 != var48 - 1) {
                                        var52 = var45;
                                    }
                                }
                            }
                        }
                    }

                    if(!var49) {
                        BlockPosition.MutableBlockPosition var66 = new BlockPosition.MutableBlockPosition();

                        for(var51 = var43; var51 < var44; ++var51) {
                            double var54 = ((double)(var51 + var3 * 16) + 0.5D - var6) / var29;

                            for(int var56 = var47; var56 < var48; ++var56) {
                                double var57 = ((double)(var56 + var4 * 16) + 0.5D - var10) / var29;
                                boolean var59 = false;
                                if(var54 * var54 + var57 * var57 < 1.0D) {
                                    for(int var60 = var46; var60 > var45; --var60) {
                                        double var61 = ((double)(var60 - 1) + 0.5D - var8) / var31;
                                        if(var61 > -0.7D && var54 * var54 + var61 * var61 + var57 * var57 < 1.0D) {
                                            IBlockData var63 = var5.a(var51, var60, var56);
                                            IBlockData var64 = (IBlockData)Objects.firstNonNull(var5.a(var51, var60 + 1, var56), b);
                                            if(var63.getBlock() == Blocks.GRASS || var63.getBlock() == Blocks.MYCELIUM) {
                                                var59 = true;
                                            }

                                            if(this.a(var63, var64)) {
                                                if(var60 - 1 < 10) {
                                                    var5.a(var51, var60, var56, a);
                                                } else {
                                                    var5.a(var51, var60, var56, b);
                                                    if(var59 && var5.a(var51, var60 - 1, var56).getBlock() == Blocks.DIRT) {
                                                        var66.c(var51 + var3 * 16, 0, var56 + var4 * 16);
                                                        var5.a(var51, var60 - 1, var56, this.g.getBiome(var66).r.getBlock().getBlockData());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if(var65) {
                            break;
                        }
                    }
                }
            }
        }

    }

    protected boolean a(IBlockData var1, IBlockData var2) {
        return var1.getBlock() == Blocks.STONE?true:(var1.getBlock() == Blocks.DIRT?true:(var1.getBlock() == Blocks.GRASS?true:(var1.getBlock() == Blocks.HARDENED_CLAY?true:(var1.getBlock() == Blocks.STAINED_HARDENED_CLAY?true:(var1.getBlock() == Blocks.SANDSTONE?true:(var1.getBlock() == Blocks.RED_SANDSTONE?true:(var1.getBlock() == Blocks.MYCELIUM?true:(var1.getBlock() == Blocks.SNOW_LAYER?true:(var1.getBlock() == Blocks.SAND || var1.getBlock() == Blocks.GRAVEL) && var2.getMaterial() != Material.WATER))))))));
    }

    protected void a(World var1, int var2, int var3, int var4, int var5, ChunkSnapshot var6) {
        int var7 = this.f.nextInt(this.f.nextInt(this.f.nextInt(15) + 1) + 1);
        if(this.f.nextInt(7) != 0) {
            var7 = 0;
        }

        for(int var8 = 0; var8 < var7; ++var8) {
            double var9 = (double)(var2 * 16 + this.f.nextInt(16));
            double var11 = (double)this.f.nextInt(this.f.nextInt(120) + 8);
            double var13 = (double)(var3 * 16 + this.f.nextInt(16));
            int var15 = 1;
            if(this.f.nextInt(4) == 0) {
                this.a(this.f.nextLong(), var4, var5, var6, var9, var11, var13);
                var15 += this.f.nextInt(4);
            }

            for(int var16 = 0; var16 < var15; ++var16) {
                float var17 = this.f.nextFloat() * 6.2831855F;
                float var18 = (this.f.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float var19 = this.f.nextFloat() * 2.0F + this.f.nextFloat();
                if(this.f.nextInt(10) == 0) {
                    var19 *= this.f.nextFloat() * this.f.nextFloat() * 3.0F + 1.0F;
                }

                this.a(this.f.nextLong(), var4, var5, var6, var9, var11, var13, var19, var17, var18, 0, 0, 1.0D);
            }
        }

    }

    static {
        a = Blocks.LAVA.getBlockData();
        b = Blocks.AIR.getBlockData();
        c = Blocks.SANDSTONE.getBlockData();
        d = Blocks.RED_SANDSTONE.getBlockData();
    }
}
