package de.sirati97.sb.skylands;

import org.bukkit.block.Biome;

/**
 * Created by sirati97 on 12.06.2016.
 */
public final class BiomesUtil {
    private BiomesUtil() {
        throw new UnsupportedOperationException();
    }

    public static boolean isPlains(Biome biome) {
        return Biome.PLAINS.equals(biome) || Biome.MUTATED_PLAINS.equals(biome);
    }

    public static boolean isDesert(Biome biome) {
        return Biome.DESERT.equals(biome) || Biome.DESERT_HILLS.equals(biome) || Biome.MUTATED_DESERT.equals(biome);
    }

    public static boolean isTaiga(Biome biome, boolean snowy) {
        return (Biome.TAIGA.equals(biome) || Biome.TAIGA_HILLS.equals(biome) || Biome.MUTATED_TAIGA.equals(biome) ||
                Biome.REDWOOD_TAIGA.equals(biome) || Biome.REDWOOD_TAIGA_HILLS.equals(biome) || Biome.MUTATED_REDWOOD_TAIGA.equals(biome) || Biome.MUTATED_REDWOOD_TAIGA_HILLS.equals(biome)) ||
                (snowy && (
                            Biome.TAIGA_COLD.equals(biome) || Biome.TAIGA_COLD_HILLS.equals(biome) || Biome.MUTATED_TAIGA_COLD.equals(biome)
                        ));
    }

    public static boolean isForest(Biome biome) {
        return Biome.FOREST.equals(biome) || Biome.FOREST_HILLS.equals(biome) || Biome.MUTATED_FOREST.equals(biome) ||
                Biome.BIRCH_FOREST.equals(biome) || Biome.MUTATED_BIRCH_FOREST.equals(biome) || Biome.MUTATED_BIRCH_FOREST_HILLS.equals(biome) ||
                Biome.ROOFED_FOREST.equals(biome) || Biome.MUTATED_ROOFED_FOREST.equals(biome);
    }

    public static boolean isMushroom(Biome biome) {
        return Biome.MUSHROOM_ISLAND.equals(biome) || Biome.MUSHROOM_ISLAND_SHORE.equals(biome);
    }

    public static boolean isMesa(Biome biome) {
        return Biome.MESA.equals(biome) || Biome.MESA_CLEAR_ROCK.equals(biome) || Biome.MESA_ROCK.equals(biome) || Biome.MUTATED_MESA.equals(biome) || Biome.MUTATED_MESA_CLEAR_ROCK.equals(biome) || Biome.MUTATED_MESA_ROCK.equals(biome);
    }

    public static boolean isOcean(Biome biome) {
        return Biome.OCEAN.equals(biome) || Biome.DEEP_OCEAN.equals(biome) || Biome.FROZEN_OCEAN.equals(biome) || Biome.BEACHES.equals(biome) || Biome.STONE_BEACH.equals(biome) || Biome.COLD_BEACH.equals(biome);
    }

    public static boolean isSwampland(Biome biome) {
        return Biome.SWAMPLAND.equals(biome) || Biome.MUTATED_SWAMPLAND.equals(biome);
    }

    public static boolean isSavanna(Biome biome) {
        return Biome.SAVANNA.equals(biome) || Biome.SAVANNA_ROCK.equals(biome) || Biome.MUTATED_SAVANNA.equals(biome) || Biome.MUTATED_SAVANNA_ROCK.equals(biome);
    }

    public static boolean isExtremeHills(Biome biome) {
        return Biome.EXTREME_HILLS.equals(biome) || Biome.EXTREME_HILLS_WITH_TREES.equals(biome) || Biome.MUTATED_EXTREME_HILLS.equals(biome) || Biome.SMALLER_EXTREME_HILLS.equals(biome) || Biome.MUTATED_EXTREME_HILLS_WITH_TREES.equals(biome);
    }

    public static boolean isHell(Biome biome) {
        return Biome.HELL.equals(biome);
    }

    public static boolean isTheEnd(Biome biome) {
        return Biome.SKY.equals(biome);
    }

    public static boolean isIcy(Biome biome) {
        return Biome.ICE_FLATS.equals(biome) || Biome.ICE_MOUNTAINS.equals(biome) || Biome.MUTATED_ICE_FLATS.equals(biome) ||
                Biome.FROZEN_OCEAN.equals(biome) || Biome.FROZEN_RIVER.equals(biome) || Biome.COLD_BEACH.equals(biome) ||
                Biome.TAIGA_COLD.equals(biome) || Biome.TAIGA_COLD_HILLS.equals(biome) || Biome.MUTATED_TAIGA_COLD.equals(biome);
    }

    public static boolean isJungle(Biome biome) {
        return Biome.JUNGLE.equals(biome) || Biome.JUNGLE_EDGE.equals(biome) || Biome.JUNGLE_HILLS.equals(biome) || Biome.JUNGLE_EDGE.equals(biome) || Biome.MUTATED_JUNGLE.equals(biome) || Biome.MUTATED_JUNGLE_EDGE.equals(biome);
    }
}
