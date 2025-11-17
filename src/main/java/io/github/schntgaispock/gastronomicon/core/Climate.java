package io.github.schntgaispock.gastronomicon.core;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.block.Biome.*;

@RequiredArgsConstructor
public enum Climate {
    DRY(new SlimefunItemStack("DRY", Material.SAND, "&eDry").item()),
    TEMPERATE(new SlimefunItemStack("TEMPERATE", Material.GRASS_BLOCK, "&eTemperate").item()),
    COLD(new SlimefunItemStack("COLD", Material.ICE, "&eCold").item()),
    SNOWY(new SlimefunItemStack("SNOWY", Material.SNOW, "&eSnowy").item()),
    NETHER(new SlimefunItemStack("NETHER", Material.CRIMSON_NYLIUM, "&eNether").item()),
    END(new SlimefunItemStack("END", Material.END_STONE, "&eEnd").item());

    private final @Getter ItemStack displayItem;

    public static Climate of(Biome b) {
        if (b == BADLANDS || b == WOODED_BADLANDS || b == ERODED_BADLANDS ||
                b == DESERT || b == SAVANNA || b == WINDSWEPT_SAVANNA ||
                b == SAVANNA_PLATEAU) {
            return Climate.DRY;

        } else if (b == DEEP_FROZEN_OCEAN || b == OLD_GROWTH_PINE_TAIGA ||
                b == TAIGA || b == OLD_GROWTH_SPRUCE_TAIGA ||
                b == WINDSWEPT_HILLS || b == WINDSWEPT_FOREST ||
                b == WINDSWEPT_GRAVELLY_HILLS || b == STONY_SHORE) {
            return Climate.COLD;

        } else if (b == SNOWY_BEACH || b == SNOWY_PLAINS || b == ICE_SPIKES ||
                b == FROZEN_RIVER || b == FROZEN_OCEAN || b == GROVE ||
                b == SNOWY_SLOPES || b == SNOWY_TAIGA || b == JAGGED_PEAKS ||
                b == FROZEN_PEAKS) {
            return Climate.SNOWY;

        } else if (b == NETHER_WASTES || b == CRIMSON_FOREST || b == WARPED_FOREST ||
                b == SOUL_SAND_VALLEY || b == BASALT_DELTAS) {
            return Climate.NETHER;

        } else if (b == THE_END || b == SMALL_END_ISLANDS || b == END_BARRENS ||
                b == END_MIDLANDS || b == END_HIGHLANDS || b == THE_VOID) {
            return Climate.END;

        } else {
            return Climate.TEMPERATE;
        }
    }

    public static Climate of(Block b) {
        return of(b.getBiome());
    }

    public static Climate of(Location l) {
        return of(l.getBlock().getBiome());
    }
}