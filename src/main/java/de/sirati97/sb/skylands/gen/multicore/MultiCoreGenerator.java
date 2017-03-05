package de.sirati97.sb.skylands.gen.multicore;

import de.sirati97.sb.skylands.SkylandsPlugin;
import de.sirati97.sb.skylands.util.Cleanable;
import de.sirati97.sb.skylands.util.CleanupRunnable;
import net.minecraft.server.v1_11_R1.BiomeBase;
import net.minecraft.server.v1_11_R1.ChunkProviderServer;
import net.minecraft.server.v1_11_R1.ChunkRegionLoader;
import net.minecraft.server.v1_11_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.block.CraftBlock;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

/**
 * Created by sirati97 on 12.06.2016 for sb-skylands.
 */
public class MultiCoreGenerator extends ChunkGenerator implements Cleanable {
    private final IAsyncGenerator parent;
    private final Plugin plugin;
    private final Map<Long, MultiCoreChunkData> dataMap = new HashMap<>();
    private final Queue<MultiCoreChunkData> queue = new LinkedList<>();
    private final AsyncHelper asyncHelper = new AsyncHelper();
    private final Set<TimedCord2d> generatedCords = new HashSet<>();
    private volatile MultiCoreChunkData mostImportant = null;
    private int debug_async1 = 0;
    private int debug_async2 = 0;
    private int debug_all = 1;
    private int lastChunkX = 0;
    private int lastChunkZ = 0;
    private final BukkitTask cleanupTask;


    public MultiCoreGenerator(IAsyncGenerator parent, Plugin plugin) {
        this.parent = parent;
        this.plugin = plugin;
        CleanupRunnable cleanupRunnable = new CleanupRunnable(this);
        cleanupTask = Bukkit.getScheduler().runTaskTimer(plugin, cleanupRunnable, 1200, 1200);
        cleanupRunnable.setTask(cleanupTask);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return parent.getDefaultPopulators(world);
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return parent.getFixedSpawnLocation(world, random);
    }

    @Override
    protected void finalize() throws Throwable {
        cleanupTask.cancel();
        super.finalize();
    }

    public void cleanUp() {
        long time = System.currentTimeMillis()-30000;
        synchronized (generatedCords) {
            for (TimedCord2d cord:new HashSet<>(generatedCords)) {
                if (cord.timestamp<time) {
                    generatedCords.remove(cord);
                }
            }
        }
        synchronized (dataMap) {
            for (MultiCoreChunkData data:new HashSet<>(dataMap.values())) {
                if (data.timestamp<time) {
                    dataMap.remove(data.id);
                }
            }
        }
        synchronized (queue) {
            for (MultiCoreChunkData data:new HashSet<>(queue)) {
                if (data.timestamp<time) {
                    queue.remove(data);
                }
            }
        }
    }

    @Override
    public synchronized ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid) {

        if (SkylandsPlugin.serverStarted) {

            long id = Cord2d.getId(chunkX, chunkZ);

            try {
                MultiCoreChunkData data = null;
                synchronized (this.dataMap) {
                    if (this.dataMap.containsKey(id)) {
                        data = this.dataMap.get(id);
                        mostImportant = data;
                        data.awaitChunk = true;
                    }
                }

                if (data != null && data.invokedChunkLoad) {
                    debug_async1++;
                }
                if (data == null || !data.finished) {
                    Set<Cord2d> cords = new HashSet<>();
                    for (int x = -2; x <= 2; x++) {
                        for (int z = -2; z <= 2; z++) {
                            cords.add(new Cord2d(x, z)); //
                        }
                    }

                    int relX = lastChunkX - chunkX;
                    int relZ = lastChunkZ - chunkZ;

                    double length = Math.sqrt(relX*relX+relZ*relZ);
                    if (length < 4 && length > 1) {
                        double normX = relX / length;
                        double normZ = relZ / length;
//                        System.out.println("found relation ship normX="+normX+", normZ="+normZ+", length="+length);
                        for (int i = 1; i < 4; i++) {
                            int xBase = (int)(normX*i);
                            int zBase = (int)(normZ*i);
                            for (int x = -1; x <= 1; x++) {
                                for (int z = -1; z <= 1; z++) {
                                    cords.add(new Cord2d(xBase + x, zBase + z));
                                }
                            }
                        }
                    }
                    cords.remove(new Cord2d(0,0));

//                    synchronized (this.dataMap) {
//                        System.out.println("Generated new chunk at x=" + chunkX + ", z=" +chunkZ + ", data=" + (data==null) + ", addedChunks="+cords.size());
//                    }
                    for (Cord2d cord:cords) {
                        generateChunkDataIfNeeded(world, random, chunkX + cord.x, chunkZ + cord.z);
                    }

                    lastChunkX = chunkX;
                    lastChunkZ = chunkZ;
                }
                debug_all++;

                if (data != null && data.finished) {
                    //System.out.println("requested chunk was already generated! x="+chunkX+", z="+chunkZ);
                    return parent.toChunkData(world, random, chunkX, chunkZ, biomeGrid, data.chunkSnapshot);
                }


                if (data == null) {
                    return parent.generateChunkData(world, random, chunkX, chunkZ, biomeGrid);
                } else {
                    //noinspection SynchronizationOnLocalVariableOrMethodParameter
                    synchronized (data) {
                        if (!data.finished) {
                            try {
                                data.wait(1000);
                            } catch (InterruptedException ignored) {
                            }
                        }
                    }
                    if (!data.finished) {
                        data.awaitChunk = false;
                        throw new IllegalStateException("Chunk generation took over one second");
                    }
                    return parent.toChunkData(world, random, chunkX, chunkZ, biomeGrid, data.chunkSnapshot);
                }
            } finally {
                synchronized (dataMap) {
                    dataMap.remove(id);
                }
            }

        } else {
            return parent.generateChunkData(world, random, chunkX, chunkZ, biomeGrid);
        }

    }


    private synchronized void generateChunkDataIfNeeded(World world, Random random, int chunkX, int chunkZ) {
        if (!isChunkGenerated(world, chunkX, chunkZ)) {
            long id = Cord2d.getId(chunkX, chunkZ);
            MultiCoreChunkData data;
            synchronized (this.dataMap) {
                if (this.dataMap.containsKey(id)) {
                    return;
                } else {
                    //System.out.println("will gen async x="+chunkX+", z="+chunkZ);
                    data = new MultiCoreChunkData(world, random, chunkX, chunkZ, id, getBiomeGrid(world, chunkX, chunkZ));
                    dataMap.put(id, data);
                }
            }
            synchronized (queue) {
                queue.add(data);
            }
            asyncHelper.doTask(this);

        }
    }

    private boolean isChunkGenerated(World world, int chunkX, int chunkZ) {
        try {
            WorldServer nmsWorld = ((CraftWorld)world).getHandle();
            ChunkProviderServer provider = nmsWorld.getChunkProviderServer();
            Field field = ChunkProviderServer.class.getDeclaredField("chunkLoader");
            field.setAccessible(true);
            ChunkRegionLoader chunkLoader = (ChunkRegionLoader) field.get(provider);
            return chunkLoader.chunkExists(nmsWorld, chunkX, chunkZ);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private BiomeGrid getBiomeGrid(World world, int chunkX, int chunkZ) {
        WorldServer nmsWorld = ((CraftWorld)world).getHandle();
        CustomBiomeGrid biomeGrid = new CustomBiomeGrid();
        biomeGrid.biome = new BiomeBase[256];
        nmsWorld.getWorldChunkManager().getBiomeBlock(biomeGrid.biome, chunkX << 4, chunkZ << 4, 16, 16);
        return biomeGrid;
    }

    private static class CustomBiomeGrid implements BiomeGrid {
        BiomeBase[] biome;

        @Override
        public Biome getBiome(int x, int z) {
            return CraftBlock.biomeBaseToBiome(biome[(z << 4) | x]);
        }

        @Override
        public void setBiome(int x, int z, Biome bio) {
            biome[(z << 4) | x] = CraftBlock.biomeToBiomeBase(bio);
        }
    }

    void generateAsync() {
        while (true) {
            final MultiCoreChunkData next;
            synchronized (queue) {
                if (queue.size() < 1) {
                    return;
                }
                if (mostImportant == null) {
                    next = queue.poll();
                } else {
                    next = mostImportant;
                    mostImportant = null;
                    queue.remove(next);
                }
            }

            synchronized (generatedCords) { //stops us from doing the same chunk twice
                TimedCord2d timedCord = new TimedCord2d(next);
                if (generatedCords.contains(timedCord)) {
                    synchronized (dataMap) {
                        dataMap.remove(next.id);
                    }
                    continue;
                } else {
                    generatedCords.add(timedCord);
                }
            }

            debug_async2++;
            next.chunkSnapshot = parent.generateChunkDataAsync(next.world, next.random, next.x, next.z, next.biomeGrid);
            next.finished = true;
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (next) {
                if (next.awaitChunk) {
                    dataMap.remove(next.id);
                } else {
                    Bukkit.getScheduler().runTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            if (!next.awaitChunk) {
                                next.invokedChunkLoad = true;
                                next.world.loadChunk(next.x, next.z);
                            }

                            synchronized (dataMap) {
                                dataMap.remove(next.id);
                            }
                        }
                    });
                }
                next.notifyAll();
            }

        }
    }

    public void sendDebug(CommandSender sender) {
        sender.sendMessage("async generated chunks="+ debug_async1);
        sender.sendMessage("sync generated chunks="+(debug_all - debug_async1 -1));
        sender.sendMessage("all generated chunks="+(debug_all -1));
        sender.sendMessage("async2="+ debug_async2);
        synchronized (queue) {
            sender.sendMessage("queue size="+queue.size());
        }
        synchronized (dataMap) {
            sender.sendMessage("pre-generated chuck map size="+dataMap.size());
        }
        synchronized (generatedCords) {
            sender.sendMessage("generated chunk cord cache size="+generatedCords.size());
        }

    }

    public IAsyncGenerator getParent() {
        return parent;
    }
}
