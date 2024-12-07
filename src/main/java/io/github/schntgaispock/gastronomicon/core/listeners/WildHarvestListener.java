package io.github.schntgaispock.gastronomicon.core.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import io.github.schntgaispock.gastronomicon.Gastronomicon;
import io.github.schntgaispock.gastronomicon.api.loot.LootTable;
import io.github.schntgaispock.gastronomicon.core.Climate;
import io.github.schntgaispock.gastronomicon.core.slimefun.GastroStacks;
import io.github.schntgaispock.gastronomicon.util.NumberUtil;
import io.github.schntgaispock.gastronomicon.util.StringUtil;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;

public class WildHarvestListener implements Listener {

    private static final Map<Climate, Map<Material, LootTable<ItemStack>>> dropsByClimateByBlock = new HashMap<>();
    private static final Map<Material, LootTable<ItemStack>> dropsByBlock = new HashMap<>();
    private static final Map<EntityType, LootTable<ItemStack>> dropsByMob = new HashMap<>();

    private static final List<String> worldsDisabledIn = new ArrayList<>();
    private static final Map<Material, Double> blockDropChanceCache = new HashMap<>();
    private static final Map<EntityType, Double> mobDropChanceCache = new HashMap<>();

    public static void registerBlockDrops(Material material, LootTable<ItemStack> table, Climate climate) {
        Map<Material, LootTable<ItemStack>> climateDrops = dropsByClimateByBlock.get(climate);
        if (climateDrops == null) {
            climateDrops = new HashMap<>();
        }
        climateDrops.put(material, table);
        dropsByClimateByBlock.put(climate, climateDrops);
    }

    public static void registerBlockDrops(Material material, LootTable<ItemStack> table) {
        dropsByBlock.put(material, table);
    }

    public static void registerMobDrops(EntityType entityType, LootTable<ItemStack> table) {
        dropsByMob.put(entityType, table);
    }

    private double getDropChance(Material mat) {
        if (blockDropChanceCache.containsKey(mat)) {
            return blockDropChanceCache.get(mat);
        }

        final double chance = Gastronomicon.getInstance()
            .getConfig()
            .getDouble("drops.block-break-chances." + StringUtil.kebabCase(mat.name()), 0.15);
        blockDropChanceCache.put(mat, chance);
        return chance;
    }

    private double getDropChance(EntityType entity) {
        if (mobDropChanceCache.containsKey(entity)) {
            return mobDropChanceCache.get(entity);
        }

        final double chance = Gastronomicon.getInstance()
            .getConfig()
            .getDouble("drops.mob-kill-chances." + StringUtil.kebabCase(entity.name()), 0.35);
        mobDropChanceCache.put(entity, chance);
        return chance;
    }

    @Nullable
    @ParametersAreNonnullByDefault
    public static LootTable<ItemStack> getDrops(Material dropFrom, Climate climate) {
        if (dropsByClimateByBlock.containsKey(climate) && dropsByClimateByBlock.get(climate).containsKey(dropFrom)) {
            return dropsByClimateByBlock.get(climate).get(dropFrom);
        } else {
            return dropsByBlock.getOrDefault(dropFrom, null);
        }
    }

    @Nullable
    public static LootTable<ItemStack> getDrops(@Nonnull EntityType dropFrom) {
        return dropsByMob.getOrDefault(dropFrom, null);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        final Player p = e.getPlayer();
        if (p.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        if (worldsDisabledIn.contains(e.getBlock().getWorld().getName())) {
            return;
        }

        final Block b = e.getBlock();
        if (b == null)
            return;

        final LootTable<ItemStack> drops = getDrops(b.getType(), Climate.of(b.getBiome()));
        if (drops == null)
            return;

        if (!Slimefun.getProtectionManager().hasPermission(p, e.getBlock(), Interaction.BREAK_BLOCK))
            return;

        final ItemStack weapon = p.getInventory().getItemInMainHand();
        final int fortune = weapon == null ? 0
            : weapon.getEnchantmentLevel(Enchantment.LOOTING);
        if (NumberUtil.flip(getDropChance(b.getType()) * (1 + fortune * 0.5))) {
            final ItemStack drop = drops.generate();
            if (drop == null) {
                return;
            }
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), drop.clone());
        }
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent e) {
        if (worldsDisabledIn.contains(e.getEntity().getWorld().getName())) {
            return;
        }

        final LootTable<ItemStack> drops = getDrops(e.getEntityType());
        if (drops == null)
            return;

        final Player killer = e.getEntity().getKiller();
        final int looting = (killer == null || killer.getInventory().getItemInMainHand() == null) ? 0
            : killer.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOTING);
        if (NumberUtil.flip(getDropChance(e.getEntityType()) * (1 + looting * 0.5))) {
            final ItemStack drop = drops.generate();
            if (drop == null) {
                return;
            }
            e.getDrops().add(drop.clone());
        }
    }

    public static void setup() {
        Bukkit.getPluginManager().registerEvents(new WildHarvestListener(), Gastronomicon.getInstance());

        worldsDisabledIn.addAll(Gastronomicon.getInstance().getConfig().getStringList("drops.disabled-in"));

        final LootTable<ItemStack> DRY_CLIMATE_GRASS_DROPS = LootTable.builder()
            .addItems(GastroStacks.CASSAVA.item(),
                GastroStacks.LENTILS.item(),
                GastroStacks.CUMIN_SEEDS.item(),
                GastroStacks.HONEYDEW_MELON_SEEDS.item(),
                GastroStacks.SORGHUM_SEEDS.item())
            .build();
        final LootTable<ItemStack> TEMPERATE_CLIMATE_GRASS_DROPS = LootTable.builder()
            .addItems(6,
                GastroStacks.RICE.item(),
                GastroStacks.OATS.item(),
                GastroStacks.SOYBEANS.item(),
                GastroStacks.BARLEY_SEEDS.item(),
                GastroStacks.RYE_SEEDS.item(),
                GastroStacks.SORGHUM_SEEDS.item())
            .addItems(4,
                GastroStacks.TURNIP_SEEDS.item(),
                GastroStacks.RED_BEANS.item(),
                GastroStacks.CANTALOUPE_SEEDS.item(),
                GastroStacks.HONEYDEW_MELON_SEEDS.item(),
                GastroStacks.SESAME_SEEDS.item(),
                GastroStacks.PEANUTS.item(),
                GastroStacks.BEANS.item(),
                GastroStacks.PEAS.item())
            .addItems(3,
                GastroStacks.BOK_CHOY_SEEDS.item(),
                GastroStacks.CUCUMBER_SEEDS.item(),
                GastroStacks.BASIL_SEEDS.item(),
                GastroStacks.SPINACH_SEEDS.item(),
                GastroStacks.MINT_SEEDS.item(),
                GastroStacks.CHILI_PEPPER_SEEDS.item(),
                GastroStacks.PARSLEY_SEEDS.item(),
                GastroStacks.CASSAVA.item(),
                GastroStacks.LENTILS.item(),
                GastroStacks.ASPARAGUS_SEEDS.item(),
                GastroStacks.GREEN_ONION_SEEDS.item(),
                GastroStacks.CAULIFLOWER_SEEDS.item(),
                GastroStacks.AVOCADO_PIT.item(),
                GastroStacks.TURMERIC.item(),
                GastroStacks.CUMIN_SEEDS.item(),
                GastroStacks.VANILLA_PLANT.item())
            .addItems(2,
                GastroStacks.LYCHEE_SAPLING.item(),
                GastroStacks.BANANA_SAPLING.item())
            .build();
        final LootTable<ItemStack> COLD_CLIMATE_GRASS_DROPS = LootTable.builder()
            .addItems(6,
                GastroStacks.QUINOA.item(),
                GastroStacks.OATS.item(),
                GastroStacks.RYE_SEEDS.item())
            .addItems(4,
                GastroStacks.TURNIP_SEEDS.item(),
                GastroStacks.SQUASH_SEEDS.item(),
                GastroStacks.PEAS.item())
            .addItems(3,
                GastroStacks.CELERY.item(),
                GastroStacks.BROCCOLI_SEEDS.item(),
                GastroStacks.BRUSSLES_SPROUTS.item())
            .addItems(2,
                GastroStacks.LYCHEE_SAPLING.item())
            .build();
        final LootTable<ItemStack> SNOWY_CLIMATE_GRASS_DROPS = LootTable.builder()
            .addItems(GastroStacks.RYE_SEEDS.item())
            .build();
        final LootTable<ItemStack> FERN_DROPS = LootTable.builder()
            .addItems(GastroStacks.FIDDLEHEADS.item())
            .build();
        final LootTable<ItemStack> DIRT_DROPS = LootTable.builder()
            .addItems(GastroStacks.ENOKI_MUSHROOMS.item(),
                GastroStacks.KING_OYSTER_MUSHROOM.item(),
                GastroStacks.BUTTON_MUSHROOM.item())
            .build();
        final LootTable<ItemStack> PODZOL_DROPS = LootTable.builder()
            .addItems(4,
                GastroStacks.ENOKI_MUSHROOMS.item(),
                GastroStacks.KING_OYSTER_MUSHROOM.item(),
                GastroStacks.BUTTON_MUSHROOM.item())
            .addItems(GastroStacks.TRUFFLE.item())
            .build();
        final LootTable<ItemStack> SEAGRASS_DROPS = LootTable.builder()
            .addItems(GastroStacks.CLAM.item())
            .build();

        final LootTable<ItemStack> SQUID_DROPS = LootTable.builder()
            .addItems(GastroStacks.RAW_SQUID.item())
            .build();
        final LootTable<ItemStack> GUARDIAN_DROPS = LootTable.builder()
            .addItems(GastroStacks.GUARDIAN_FIN.item())
            .build();
        final LootTable<ItemStack> GOAT_DROPS = LootTable.builder()
            .addItems(GastroStacks.RAW_CHEVON.item())
            .build();
        final LootTable<ItemStack> SALMON_DROPS = LootTable.builder()
            .addItems(GastroStacks.SALMON_ROE.item())
            .build();

        WildHarvestListener.registerBlockDrops(Material.SHORT_GRASS, DRY_CLIMATE_GRASS_DROPS, Climate.DRY);
        WildHarvestListener.registerBlockDrops(Material.TALL_GRASS, DRY_CLIMATE_GRASS_DROPS, Climate.DRY);
        WildHarvestListener.registerBlockDrops(Material.SHORT_GRASS, TEMPERATE_CLIMATE_GRASS_DROPS, Climate.TEMPERATE);
        WildHarvestListener.registerBlockDrops(Material.TALL_GRASS, TEMPERATE_CLIMATE_GRASS_DROPS, Climate.TEMPERATE);
        WildHarvestListener.registerBlockDrops(Material.SHORT_GRASS, COLD_CLIMATE_GRASS_DROPS, Climate.COLD);
        WildHarvestListener.registerBlockDrops(Material.TALL_GRASS, COLD_CLIMATE_GRASS_DROPS, Climate.COLD);
        WildHarvestListener.registerBlockDrops(Material.SHORT_GRASS, SNOWY_CLIMATE_GRASS_DROPS, Climate.SNOWY);
        WildHarvestListener.registerBlockDrops(Material.TALL_GRASS, SNOWY_CLIMATE_GRASS_DROPS, Climate.SNOWY);
        WildHarvestListener.registerBlockDrops(Material.FERN, FERN_DROPS);
        WildHarvestListener.registerBlockDrops(Material.LARGE_FERN, FERN_DROPS);
        WildHarvestListener.registerBlockDrops(Material.PODZOL, PODZOL_DROPS);
        WildHarvestListener.registerBlockDrops(Material.DIRT, DIRT_DROPS);
        WildHarvestListener.registerBlockDrops(Material.GRASS_BLOCK, DIRT_DROPS);
        WildHarvestListener.registerBlockDrops(Material.ROOTED_DIRT, DIRT_DROPS);
        WildHarvestListener.registerBlockDrops(Material.MYCELIUM, DIRT_DROPS);
        WildHarvestListener.registerBlockDrops(Material.SEAGRASS, SEAGRASS_DROPS);
        WildHarvestListener.registerMobDrops(EntityType.SQUID, SQUID_DROPS);
        WildHarvestListener.registerMobDrops(EntityType.GUARDIAN, GUARDIAN_DROPS);
        WildHarvestListener.registerMobDrops(EntityType.GOAT, GOAT_DROPS);
        WildHarvestListener.registerMobDrops(EntityType.SALMON, SALMON_DROPS);
    }

}
