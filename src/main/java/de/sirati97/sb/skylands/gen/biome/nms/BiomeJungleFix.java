package de.sirati97.sb.skylands.gen.biome.nms;

import net.minecraft.server.v1_11_R1.BiomeBase;
import net.minecraft.server.v1_11_R1.BiomeJungle;
import net.minecraft.server.v1_11_R1.BlockFlowers;
import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.ChunkSnapshot;
import net.minecraft.server.v1_11_R1.EnumCreatureType;
import net.minecraft.server.v1_11_R1.World;
import net.minecraft.server.v1_11_R1.WorldGenMelon;
import net.minecraft.server.v1_11_R1.WorldGenTreeAbstract;
import net.minecraft.server.v1_11_R1.WorldGenVines;
import net.minecraft.server.v1_11_R1.WorldGenerator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BiomeJungleFix extends BiomeBase {
    private final static Map<BiomeJungle, BiomeJungleFix> fixMap = new HashMap<>();
    //    private final static Method aBiomeDecoratorMethod;
    private final static Field[] fields;
    private final static a a = new BiomeBase.a("Jungle");
    private final BiomeJungle biomeJungle;

    static {
        Class biomeBase = BiomeBase.class;
        Field[] decFields = biomeBase.getDeclaredFields();
        List<Field> fieldsList = new ArrayList<>();
        for (int i = 0; i < decFields.length; i++) {
            int modifier = decFields[i].getModifiers();
            if (Modifier.isStatic(modifier) || Modifier.isFinal(modifier)) {
                continue;
            }
            decFields[i].setAccessible(true);
            fieldsList.add(decFields[i]);
        }
        fields = fieldsList.toArray(new Field[fieldsList.size()]);
    }

    public static BiomeJungleFix getFix(BiomeJungle biomeJungle) {
        if (fixMap.containsKey(biomeJungle)) {
            return fixMap.get(biomeJungle);
        } else {
            BiomeJungleFix fix = new BiomeJungleFix(biomeJungle);
            fixMap.put(biomeJungle, fix);
            return fix;
        }
    }


    public BiomeJungleFix(BiomeJungle biomeJungle) {
        super(a);
        try {
            for (Field field : fields) {
                field.set(this, field.get(biomeJungle));
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        this.biomeJungle = biomeJungle;
    }


    //override all methods as wrapper
    @Override
    public WorldGenTreeAbstract a(Random var1) {
        return biomeJungle.a(var1);
    }

    @Override
    public WorldGenerator b(Random var1) {
        return biomeJungle.b(var1);
    }
//
//    @Override
//    protected BiomeDecorator a() {
//        try {
//            return (BiomeDecorator) aBiomeDecoratorMethod.invoke(biomeJungle);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            throw new IllegalStateException(e);
//        }
//    }

    @Override
    public BlockFlowers.EnumFlowerVarient a(Random random, BlockPosition blockPosition) {
        return biomeJungle.a(random, blockPosition);
    }

    @Override
    public void a(World world, Random random, ChunkSnapshot chunkSnapshot, int i, int i1, double v) {
        biomeJungle.a(world, random, chunkSnapshot, i, i1, v);
    }

    @Override
    public boolean b() {
        return biomeJungle.b();
    }

    @Override
    public boolean c() {
        return biomeJungle.c();
    }

    @Override
    public boolean d() {
        return biomeJungle.d();
    }

    @Override
    public boolean e() {
        return biomeJungle.e();
    }

    @Override
    public float f() {
        return biomeJungle.f();
    }

    @Override
    public boolean i() {
        return biomeJungle.i();
    }

    @Override
    public Class<? extends BiomeBase> g() {
        return biomeJungle.g();
    }

    @Override
    public List<BiomeMeta> getMobs(EnumCreatureType enumCreatureType) {
        return biomeJungle.getMobs(enumCreatureType);
    }

    @Override
    public EnumTemperature h() {
        return biomeJungle.h();
    }
    //end


    public void a(World var1, Random var2, BlockPosition var3) {
//        super.a(var1, var2, var3);
//        int var4 = var2.nextInt(16) + 8;
//        int var5 = var2.nextInt(16) + 8;
//        int highest = var1.getHighestBlockYAt(var3.a(var4, 0, var5)).getY() * 2;
//        if (highest > 0) {
//            int var6 = var2.nextInt(highest);
//            (new WorldGenMelon()).generate(var1, var2, var3.a(var4, var6, var5));
//            WorldGenVines var9 = new WorldGenVines();
//
//            for(var5 = 0; var5 < 50; ++var5) {
//                var6 = var2.nextInt(16) + 8;
//                int var8 = var2.nextInt(16) + 8;
//                var9.generate(var1, var2, var3.a(var6, 128, var8));
//            }
//        }
        super.a(var1, var2, var3);
        int var4 = var2.nextInt(16) + 8;
        int var5 = var2.nextInt(16) + 8;
        int highest = var1.getHighestBlockYAt(var3.a(var4, 0, var5)).getY() * 2;
        int var6 = highest>0?var2.nextInt(highest):0;
        (new WorldGenMelon()).generate(var1, var2, var3.a(var4, var6, var5));
        WorldGenVines var9 = new WorldGenVines();

        for (var5 = 0; var5 < 50; ++var5) {
            var6 = var2.nextInt(16) + 8;
            int var8 = var2.nextInt(16) + 8;
            var9.generate(var1, var2, var3.a(var6, 128, var8));
        }
    }
}
