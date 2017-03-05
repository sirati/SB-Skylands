package uk.co.jacekk.bukkit.skylandsplus.generation;

import de.sirati97.sb.skylands.BiomesUtil;
import de.sirati97.sb.skylands.gen.multicore.Cord2d;
import de.sirati97.sb.skylands.gen.multicore.IAsyncGenerator;
import de.sirati97.sb.skylands.gen.multicore.TimedCord2d;
import de.sirati97.sb.skylands.gen.pop.BiomePopulator;
import de.sirati97.sb.skylands.gen.pop.SkyGlowstonePopulator;
import de.sirati97.sb.skylands.gen.pop.nms.WorldGenSecondaryCaves;
import de.sirati97.sb.skylands.gen.pop.nms.WorldGenSkyGlowstone;
import de.sirati97.sb.skylands.nms.BetterChunkSnapshot;
import de.sirati97.sb.skylands.util.Cleanable;
import de.sirati97.sb.skylands.util.CleanupRunnable;
import net.minecraft.server.v1_11_R1.Block;
import net.minecraft.server.v1_11_R1.BlockFlowers;
import net.minecraft.server.v1_11_R1.Blocks;
import net.minecraft.server.v1_11_R1.NoiseGeneratorOctaves;
import net.minecraft.server.v1_11_R1.WorldGenCanyon;
import net.minecraft.server.v1_11_R1.WorldGenCaves;
import net.minecraft.server.v1_11_R1.WorldGenCavesHell;
import net.minecraft.server.v1_11_R1.WorldGenFlowers;
import net.minecraft.server.v1_11_R1.WorldGenLargeFeature;
import net.minecraft.server.v1_11_R1.WorldGenMineshaft;
import net.minecraft.server.v1_11_R1.WorldGenNether;
import net.minecraft.server.v1_11_R1.WorldGenStronghold;
import net.minecraft.server.v1_11_R1.WorldGenVillage;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.util.CraftMagicNumbers;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SkylandsGenerator extends org.bukkit.generator.ChunkGenerator implements IAsyncGenerator, Cleanable {


    private final static WorldGenCaves caveGen = new WorldGenCaves();
    private final static WorldGenSecondaryCaves caveGen2 = new WorldGenSecondaryCaves();
    private final static WorldGenCavesHell caveHellGen = new WorldGenCavesHell();

    private final static WorldGenCanyon canyonGen = new WorldGenCanyon();
    private final static WorldGenStronghold strongholdGen = new WorldGenStronghold();
    private final static WorldGenMineshaft mineshaftGen = new WorldGenMineshaft();
    private final static WorldGenVillage villageGen = new WorldGenVillage();
    private final static WorldGenLargeFeature largeFeatureGen = new WorldGenLargeFeature();
    private final static WorldGenFlowers[] worldGenFlowers;
    public final static WorldGenSkyGlowstone worldGenSkyGlowstone = new WorldGenSkyGlowstone();

    private final static WorldGenCavesHell caveGenNether = new WorldGenCavesHell();
    private final static WorldGenNether genNetherFort = new WorldGenNether();

    private final Map<Long, TimedCord2d> latelyGenerated = new HashMap<>();
    private final BukkitTask cleanupTask;

    static {
        BlockFlowers.EnumFlowerVarient[] flowerVartients = BlockFlowers.EnumFlowerVarient.values();
        worldGenFlowers = new WorldGenFlowers[flowerVartients.length];
        for (int i = 0; i < flowerVartients.length; i++) {
            worldGenFlowers[i] = new WorldGenFlowers(flowerVartients[i].a().a(), flowerVartients[i]);
        }
    }

    
    private class GeneratorData{

        private double[] q;
        private double[] t = new double[256];

        double[] d;
        double[] e;
        double[] f;
        double[] g;
        double[] h;
        private Random random;

        private NoiseGeneratorOctaves k;
        private NoiseGeneratorOctaves l;
        private NoiseGeneratorOctaves m;
        private NoiseGeneratorOctaves o;
        private NoiseGeneratorOctaves a;
        private NoiseGeneratorOctaves b;

        int[][] i = new int[32][32];
    }

    private int offset;
    private int high = 128;
    private boolean only, stronghold, mineshaft;
    private boolean canyon = true;
    private boolean village = true;
    private boolean largeFeature = true;
    private boolean glowstone = true;
    private boolean decorated_caves = true;
    private boolean caves = true;
    private boolean no_plains, no_desert, no_forest, no_jungle, no_taiga, no_ice, no_mushroom, no_swampland, no_savanna;
    private boolean no_ocean = true;
    private Biome onlyBiome, plains, desert, forest, jungle, taiga, ice, mushroom, swampland, savanna;
    private Biome ocean = Biome.PLAINS;

    @SuppressWarnings("deprecation")
    public SkylandsGenerator(Plugin plugin, String[] tokens) {
        this(plugin);

        for (String token : tokens) {
            if (token.matches("offset=-?\\d{1,3}")) {
                offset = Integer.parseInt(token.substring(7));
            } else if (token.matches("high=\\d{1,3}")) {
                high = Integer.parseInt(token.substring(5));
            } else if (token.equalsIgnoreCase("high")) {
                high = 145;
            } else if (token.equalsIgnoreCase("very-high")) {
                high = 170;
            } else if (token.equalsIgnoreCase("no-decorated-caves")) {
                decorated_caves = false;
            } else if (token.equalsIgnoreCase("no-caves")) {
                caves = false;
            } else if (token.equalsIgnoreCase("no-glowstone")) {
                glowstone = false;
            } else if (token.equalsIgnoreCase("no-canyon")) {
                canyon = false;
            } else if (token.equalsIgnoreCase("stronghold")) {
                stronghold = true;
            } else if (token.equalsIgnoreCase("mineshaft")) {
                mineshaft = true;
            } else if (token.equalsIgnoreCase("no-village")) {
                village = false;
            } else if (token.equalsIgnoreCase("no-largeFeatures")) {
                largeFeature = false;
            } else if (token.equalsIgnoreCase("no-desert")) {
                no_desert = true;
                desert = Biome.PLAINS;
            } else if (token.equalsIgnoreCase("no-forest")) {
                no_forest = true;
                forest = Biome.PLAINS;
            } else if (token.equalsIgnoreCase("no-jungle")) {
                no_jungle = true;
                jungle = Biome.PLAINS;
            } else if (token.equalsIgnoreCase("no-taiga")) {
                no_taiga = true;
                taiga = Biome.PLAINS;
            } else if (token.equalsIgnoreCase("no-ice")) {
                no_ice = true;
                ice = Biome.PLAINS;
            } else if (token.equalsIgnoreCase("no-ocean")) {
                no_ocean = true;
                ocean = Biome.PLAINS;
            } else if (token.equalsIgnoreCase("no-mushroom")) {
                no_mushroom = true;
                mushroom = Biome.PLAINS;
            } else if (token.equalsIgnoreCase("no-swampland")) {
                no_swampland = true;
                swampland = Biome.PLAINS;
            } else if (token.equalsIgnoreCase("no-savanna")) {
                no_savanna = true;
                savanna = Biome.PLAINS;
            } else if (token.matches("(?i)only=[A-Z_]+")) {
                only = true;
                onlyBiome = Biome.valueOf(token.substring(5));
            } else if (token.matches("(?i)plains=[A-Z_]+")) {
                no_plains = true;
                plains = Biome.valueOf(token.substring(7));
            } else if (token.matches("(?i)desert=[A-Z_]+")) {
                no_desert = true;
                desert = Biome.valueOf(token.substring(7));
            } else if (token.matches("(?i)forest=[A-Z_]+")) {
                no_forest = true;
                forest = Biome.valueOf(token.substring(7));
            } else if (token.matches("(?i)jungle=[A-Z_]+")) {
                no_jungle = true;
                jungle = Biome.valueOf(token.substring(7));
            } else if (token.matches("(?i)taiga=[A-Z_]+")) {
                no_taiga = true;
                taiga = Biome.valueOf(token.substring(6));
            } else if (token.matches("(?i)ice=[A-Z_]+")) {
                no_ice = true;
                ice = Biome.valueOf(token.substring(4));
            } else if (token.matches("(?i)mushroom=[A-Z_]+")) {
                no_mushroom = true;
                mushroom = Biome.valueOf(token.substring(9));
            } else if (token.matches("(?i)swampland=[A-Z_]+")) {
                no_swampland = true;
                swampland = Biome.valueOf(token.substring(10));
            } else if (token.matches("(?i)savanna=[A-Z_]+")) {
                no_savanna = true;
                savanna = Biome.valueOf(token.substring(10));
            } else if (token.matches("(?i)ocean=[A-Z_]+")) {
                no_ocean = true;
                ocean = Biome.valueOf(token.substring(5));
            }
        }
    }


    public SkylandsGenerator(Plugin plugin, int offset, int high, boolean glowstone) {
        this(plugin);
        this.offset = offset;
        this.high = high;
        this.glowstone = glowstone;
    }

    private SkylandsGenerator(Plugin plugin) {
        CleanupRunnable cleanupRunnable = new CleanupRunnable(this);
        cleanupTask = Bukkit.getScheduler().runTaskTimer(plugin, cleanupRunnable, 600, 600);
        cleanupRunnable.setTask(cleanupTask);
    }


    public void cleanUp() {
        long time = System.currentTimeMillis()-30000;
        for (TimedCord2d cord:new HashSet<>(latelyGenerated.values())) {
            if (cord.timestamp<time) {
                latelyGenerated.remove(Cord2d.getId(cord));
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        cleanupTask.cancel();
        super.finalize();
    }

    public boolean wasShortlyLoaded(Chunk chunk) {
        return latelyGenerated.containsKey(Cord2d.getId(chunk.getX(),chunk.getZ()));
    }

    public List<BlockPopulator> getDefaultPopulators(World world) {
        ArrayList<BlockPopulator> populators = new ArrayList<>();

        switch (world.getEnvironment()) {
            case NORMAL:
                populators.add(new BiomePopulator());
                populators.add(new LakePopulator(world));
                populators.add(new PumpkinPopulator(world));
                populators.add(new MelonPopulator(world));
                populators.add(new OrePopulator(world, glowstone));
                populators.add(new CactusPopulator(world));
                populators.add(new RedSandPopulator());
                populators.add(new SnowPopulator());
                if (glowstone) {
                    populators.add(new SkyGlowstonePopulator());
                }
                break;

            case THE_END:
                populators.add(new EndTowerPopulator(world));
                break;

            case NETHER:
                populators.add(new NetherSoulSandPopulator(world));
                populators.add(new NetherFirePopulator(world));
                populators.add(new NetherGlowstonePopulator(world));
                populators.add(new NetherWartPopulator(world));
                populators.add(new OrePopulator(world, false));
                break;
        }

        return populators;
    }

    // anybody know what this does, let me know !
    private double[] a(double[] adouble, int i, int j, int k, int l, int i1, int j1, GeneratorData gData) {
        if (adouble == null) {
            adouble = new double[l * i1 * j1];
        }

        double d0 = 684.412D;
        double d1 = 684.412D;

        gData.g = gData.a.a(gData.g, i, k, l, j1, 1.121D, 1.121D, 0.5D);
        gData.h = gData.b.a(gData.h, i, k, l, j1, 200.0D, 200.0D, 0.5D);

        d0 *= 2.0D;

        gData.d = gData.m.a(gData.d, i, j, k, l, i1, j1, d0 / 80.0D, d1 / 160.0D, d0 / 80.0D);
        gData.e = gData.k.a(gData.e, i, j, k, l, i1, j1, d0, d1, d0);
        gData.f = gData.l.a(gData.f, i, j, k, l, i1, j1, d0, d1, d0);

        int k1 = 0;
        int l1 = 0;

        for (int i2 = 0; i2 < l; ++i2) {
            for (int j2 = 0; j2 < j1; ++j2) {
                double d2 = (gData.g[l1] + 256.0D) / 512.0D;

                if (d2 > 1.0D)
                    d2 = 1.0D;

                double d3 = gData.h[l1] / 8000.0D;

                if (d3 < 0.0D)
                    d3 = -d3 * 0.3D;

                d3 = d3 * 3.0D - 2.0D;

                if (d3 > 1.0D)
                    d3 = 1.0D;

                d3 /= 8.0D;
                d3 = 0.0D;

                if (d2 < 0.0D)
                    d2 = 0.0D;

                d2 += 0.5D;
                d3 = d3 * i1 / 16.0D;

                ++l1;

                double d4 = i1 / 2.0D;

                for (int k2 = 0; k2 < i1; ++k2) {
                    double d5 = 0.0D;
                    double d6 = (k2 - d4) * 8.0D / d2;

                    if (d6 < 0.0D)
                        d6 *= -1.0D;

                    double d7 = gData.e[k1] / 512.0D;
                    double d8 = gData.f[k1] / 512.0D;
                    double d9 = (gData.d[k1] / 10.0D + 1.0D) / 2.0D;

                    if (d9 < 0.0D) {
                        d5 = d7;
                    } else if (d9 > 1.0D) {
                        d5 = d8;
                    } else {
                        d5 = d7 + (d8 - d7) * d9;
                    }

                    d5 -= 8.0D;

                    byte b0 = 32;
                    double d10;

                    if (k2 > i1 - b0) {
                        d10 = (k2 - (i1 - b0)) / (b0 - 1.0F);
                        d5 = d5 * (1.0D - d10) + -30.0D * d10;
                    }

                    b0 = 8;

                    if (k2 < b0) {
                        d10 = (b0 - k2) / (b0 - 1.0F);
                        d5 = d5 * (1.0D - d10) + -30.0D * d10;
                    }

                    adouble[k1] = d5;
                    ++k1;
                }
            }
        }
        return adouble;
    }

    private void shapeLand(World world, int chunkX, int chunkZ, BetterChunkSnapshot chunkSnapshot, GeneratorData gData) {
        byte b0 = 2;
        int k = b0 + 1;

        int l = high / 4 + 1; //128->high
        int i1 = b0 + 1;

        gData.q = this.a(gData.q, chunkX * b0, 0, chunkZ * b0, k, l, i1, gData);

        Block blockType;

        switch (world.getEnvironment()) {
            case NETHER:
                blockType = Blocks.NETHERRACK;
                break;

            case THE_END:
                blockType = Blocks.END_STONE;
                break;

            default:
                blockType = Blocks.STONE;
                break;
        }

        for (int j1 = 0; j1 < b0; ++j1) {
            int k1 = 0;

            while (k1 < b0) {
                int l1 = 0;

                while (true) {
                    if (l1 >= high / 4) {//128->high
                        ++k1;
                        break;
                    }

                    double d0 = 0.25D;
                    double d1 = gData.q[((j1 + 0) * i1 + (k1 + 0)) * l + l1 + 0];
                    double d2 = gData.q[((j1 + 0) * i1 + (k1 + 1)) * l + l1 + 0];
                    double d3 = gData.q[((j1 + 1) * i1 + (k1 + 0)) * l + l1 + 0];
                    double d4 = gData.q[((j1 + 1) * i1 + (k1 + 1)) * l + l1 + 0];
                    double d5 = (gData.q[((j1 + 0) * i1 + (k1 + 0)) * l + l1 + 1] - d1) * d0;
                    double d6 = (gData.q[((j1 + 0) * i1 + (k1 + 1)) * l + l1 + 1] - d2) * d0;
                    double d7 = (gData.q[((j1 + 1) * i1 + (k1 + 0)) * l + l1 + 1] - d3) * d0;
                    double d8 = (gData.q[((j1 + 1) * i1 + (k1 + 1)) * l + l1 + 1] - d4) * d0;

                    for (int i2 = 0; i2 < 4; ++i2) {
                        double d9 = 0.125D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        for (int j2 = 0; j2 < 8; ++j2) {
//                            int i3 = j2 + j1 * 8 << 11 | 0 + k1 * 8 << 7 | l1 * 4 + i2;
                            int i3 = j2 + j1 * 8 << 12 | k1 * 8 << 8 | l1 * 4 + i2;


                            int j3 = 1 << 8; //7->8
                            double d14 = 0.125D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;

                            for (int k3 = 0; k3 < 8; ++k3) {
                                if (d15 > 0.0D) {
                                    chunkSnapshot.set(i3, blockType.getBlockData());
                                }

                                i3 += j3;
                                d15 += d16;
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }

                    ++l1;
                }
            }
        }
    }

    private void decorateLand(int chunkX, int chunkZ, BetterChunkSnapshot chunkSnapshot, BiomeGrid biomeGrid, GeneratorData gData) {
        double d0 = 0.03125D;
        gData.t = gData.o.a(gData.t, chunkX * 16, chunkZ * 16, 0, 16, 16, 1, d0 * 2.0D, d0 * 2.0D, d0 * 2.0D);

        for (int z = 0; z < 16; ++z) {
            for (int x = 0; x < 16; ++x) {
                int i1 = (int) (gData.t[z + x * 16] / 3.0D + 3.0D + gData.random.nextDouble() * 0.25D);
                int j1 = -1;

                Biome biome = biomeGrid.getBiome(x, z);

                Block b1, b2;

                if (only) {
                    biomeGrid.setBiome(x, z, onlyBiome);
                    biome = onlyBiome;
                } else {
                    if (no_plains) {
                        if (BiomesUtil.isPlains(biome)) {
                            biomeGrid.setBiome(x, z, plains);
                            biome = plains;
                        }
                    }
                    if (no_desert) {
                        if (BiomesUtil.isDesert(biome)) {
                            biomeGrid.setBiome(x, z, desert);
                            biome = desert;
                        }
                    }
                    if (no_forest) {
                        if (BiomesUtil.isForest(biome)) {
                            biomeGrid.setBiome(x, z, forest);
                            biome = forest;
                        }
                    }
                    if (no_jungle) {
                        if (BiomesUtil.isJungle(biome)) {
                            biomeGrid.setBiome(x, z, jungle);
                            biome = jungle;
                        }
                    }
                    if (no_taiga) {
                        if (BiomesUtil.isTaiga(biome, false)) {
                            biomeGrid.setBiome(x, z, taiga);
                            biome = taiga;
                        }
                    }
                    if (no_ice) {
                        if (BiomesUtil.isIcy(biome)) {
                            biomeGrid.setBiome(x, z, ice);
                            biome = ice;
                        }
                    }
                    if (no_mushroom) {
                        if (BiomesUtil.isMushroom(biome)) {
                            biomeGrid.setBiome(x, z, mushroom);
                            biome = mushroom;
                        }
                    }
                    if (no_swampland) {
                        if (BiomesUtil.isSwampland(biome)) {
                            biomeGrid.setBiome(x, z, swampland);
                            biome = swampland;
                        }
                    }
                    if (no_savanna) {
                        if (BiomesUtil.isSavanna(biome)) {
                            biomeGrid.setBiome(x, z, savanna);
                            biome = savanna;
                        }
                    }
                    if (no_ocean) {
                        if (BiomesUtil.isOcean(biome)) {
                            biomeGrid.setBiome(x, z, ocean);
                            biome = ocean;
                        }
                    }
                }

                if (BiomesUtil.isDesert(biome)) {
                    b1 = Blocks.SAND;
                    b2 = Blocks.SAND;
                } else if (BiomesUtil.isMesa(biome)) {
                    b1 = Blocks.SAND;
                    b2 = Blocks.HARDENED_CLAY;
                } else if (BiomesUtil.isHell(biome)) {
                    b1 = Blocks.NETHERRACK;
                    b2 = Blocks.NETHERRACK;
                } else if (BiomesUtil.isMushroom(biome)) {
                    b1 = Blocks.MYCELIUM;
                    b2 = Blocks.DIRT;
                } else {
                    b1 = Blocks.GRASS;
                    b2 = Blocks.DIRT;
                }

                for (int y = 255; y >= 0; --y) { //127->255

                    Block b3 = chunkSnapshot.a(x, y, z).getBlock();

                    if (b3 == Blocks.AIR) {
                        j1 = -1;
                    } else if (b3 == Blocks.STONE) {
                        if (j1 == -1) {
                            j1 = i1;
                            chunkSnapshot.a(x, y, z, b1.getBlockData());
                        } else if (j1 > 0) {
                            --j1;
                            chunkSnapshot.a(x, y, z, b2.getBlockData());

                            if (j1 == 0 && b2 == Blocks.SAND) {
                                j1 = gData.random.nextInt(4);
                                b2 = Blocks.SANDSTONE;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
        return toChunkData(world, random, chunkX, chunkZ, biomeGrid, generateChunkDataAsync(world, random, chunkX, chunkZ, biomeGrid));
    }

    @Override
    public ChunkData toChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid, BetterChunkSnapshot chunkSnapshot) {
        ChunkData chunkData = createChunkData(world);

        int cut_top = 0;
        int cut_bottom = 0;

        if (offset > 128) {
            cut_top = offset - 128;
        } else if (offset < 0) {
            cut_bottom = -offset;
        }

        // TODO: Do this in a nice way.
        for (int x = 0; x < 16; ++x) {
            for (int y = cut_bottom; y < 256 - cut_top; ++y) {
                for (int z = 0; z < 16; ++z) {
                    chunkData.setBlock(x, y + offset, z, CraftMagicNumbers.getMaterial(chunkSnapshot.get(x, y, z).getBlock()));
                }
            }
        }

        TimedCord2d timedCord = new TimedCord2d(chunkX,chunkZ);
        latelyGenerated.put(Cord2d.getId(timedCord), timedCord);

        return chunkData;
    }


    @Override
    public BetterChunkSnapshot generateChunkDataAsync(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
        Environment environment = world.getEnvironment();
        GeneratorData gData = new GeneratorData();
        
        if (gData.random == null) {
            gData.random = new Random(world.getSeed());

            gData.k = new NoiseGeneratorOctaves(gData.random, 16);
            gData.l = new NoiseGeneratorOctaves(gData.random, 16);
            gData.m = new NoiseGeneratorOctaves(gData.random, 8);
            gData.o = new NoiseGeneratorOctaves(gData.random, 4);
            gData.a = new NoiseGeneratorOctaves(gData.random, 10);
            gData.b = new NoiseGeneratorOctaves(gData.random, 16);
        }

        net.minecraft.server.v1_11_R1.World mcWorld = ((CraftWorld) world).getHandle();

        //Block[] blocks = new Block[65536];
        BetterChunkSnapshot chunkSnapshot = new BetterChunkSnapshot();

        gData.random.setSeed(chunkX * 341873128712L + chunkZ * 132897987541L);

        this.shapeLand(world, chunkX, chunkZ, chunkSnapshot, gData);

        if (environment == Environment.NORMAL) {
            if (decorated_caves) {
                caveGen.a(mcWorld, chunkX, chunkZ, chunkSnapshot); //one time before decoration
            }
        }

        this.decorateLand(chunkX, chunkZ, chunkSnapshot, biomeGrid, gData);

        if (environment == Environment.NORMAL) {
            if (caves) {
                caveGen2.a(mcWorld, chunkX, chunkZ, chunkSnapshot);
            }

            if (canyon) {
                canyonGen.a(mcWorld, chunkX, chunkZ, chunkSnapshot);
            }
            if (stronghold) {
                strongholdGen.a(mcWorld, chunkX, chunkZ, chunkSnapshot);
            }
            if (mineshaft) {
                mineshaftGen.a(mcWorld, chunkX, chunkZ, chunkSnapshot);
            }
            if (village) {
                villageGen.a(mcWorld, chunkX, chunkZ, chunkSnapshot);
            }
            if (largeFeature) {
                largeFeatureGen.a(mcWorld, chunkX, chunkZ, chunkSnapshot);
            }
        } else if (environment == Environment.NETHER) {
            caveGenNether.a(mcWorld, chunkX, chunkZ, chunkSnapshot);
            genNetherFort.a(mcWorld, chunkX, chunkZ, chunkSnapshot);
        }

        return chunkSnapshot;
    }

}