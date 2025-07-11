package io.github.schntgaispock.gastronomicon.core.slimefun.items.workstations.manual;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.schntgaispock.gastronomicon.Gastronomicon;
import io.github.schntgaispock.gastronomicon.api.recipes.GastroRecipe;
import io.github.schntgaispock.gastronomicon.api.recipes.MultiStoveRecipe;
import io.github.schntgaispock.gastronomicon.core.slimefun.recipes.GastroRecipeType;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.jetbrains.annotations.NotNull;

@Getter
@SuppressWarnings("deprecation")
public class MultiStove extends GastroWorkstation implements EnergyNetComponent {

    @RequiredArgsConstructor
    public enum Temperature {
        LOW(TEMPERATURE_BUTTON_LOW), MEDIUM(TEMPERATURE_BUTTON_MEDIUM), HIGH(TEMPERATURE_BUTTON_HIGH);

        private final @Getter ItemStack item;

        public @Nullable Temperature next() {
            if (ordinal() == values().length - 1) {
                return null;
            }

            return values()[ordinal() + 1];
        }

        public @Nullable Temperature prev() {
            if (ordinal() == 0) {
                return null;
            }

            return values()[ordinal() - 1];
        }
    }

    public static final ItemStack TEMPERATURE_BUTTON_LOW = new SlimefunItemStack(
            "TEMPERATURE_BUTTON_LOW",
        Material.YELLOW_STAINED_GLASS_PANE,
        "&7Temperature: &eLOW",
        "",
        "&bLeft-click &7to increase").item();
    public static final ItemStack TEMPERATURE_BUTTON_MEDIUM = new SlimefunItemStack(
            "TEMPERATURE_BUTTON_MEDIUM",
        Material.ORANGE_STAINED_GLASS_PANE,
        "&7Temperature: &6MEDIUM",
        "",
        "&bLeft-click &7to increase",
        "&bRight-click &7to decrease").item();
    public static final ItemStack TEMPERATURE_BUTTON_HIGH = new SlimefunItemStack(
            "TEMPERATURE_BUTTON_HIGH",
        Material.RED_STAINED_GLASS_PANE,
        "&7Temperature: &cHIGH",
        "",
        "&bRight-click &7to decrease").item();
    public static final int TEMPERATURE_BUTTON_SLOT = 52;
    public static final String TEMPERATURE_KEY = "gastronomicon:multi_stove/temperature";

    private final int capacity;
    private final int energyPerUse;

    public MultiStove(SlimefunItemStack item, ItemStack[] recipe, int capacity, int energyPerUse) {
        super(item, recipe);

        this.capacity = capacity;
        this.energyPerUse = energyPerUse;
    }

    @Override
    protected void setup(@NotNull BlockMenuPreset preset) {
        super.setup(preset);

        preset.drawBackground(TEMPERATURE_BUTTON_LOW, new int[] { TEMPERATURE_BUTTON_SLOT });
    }

    @Override
    protected void onNewInstance(@NotNull BlockMenu menu, @NotNull Block b) {
        super.onNewInstance(menu, b);

        menu.addMenuOpeningHandler(player -> {
            final String temp = BlockStorage.getLocationInfo(menu.getLocation(), TEMPERATURE_KEY);
            menu.replaceExistingItem(TEMPERATURE_BUTTON_SLOT,
                temp == null ? TEMPERATURE_BUTTON_LOW : Temperature.valueOf(temp).getItem(), false);
        });

        menu.addMenuClickHandler(TEMPERATURE_BUTTON_SLOT, (player, slot, item, action) -> {
            final Temperature t = Temperature.valueOf(item.getItemMeta().getDisplayName().substring(17));
            changeTemperature(menu, action.isRightClicked() ? t.prev() : t.next());
            return false;
        });
    }

    public static void changeTemperature(@Nonnull BlockMenu menu, @Nullable Temperature t) {
        if (t == null) {
            return;
        }
        menu.replaceExistingItem(TEMPERATURE_BUTTON_SLOT, t.getItem());
        BlockStorage.addBlockInfo(menu.getLocation(), TEMPERATURE_KEY, t.name());
    }

    @Override
    public GastroRecipeType getGastroRecipeType() {
        return GastroRecipeType.MULTI_STOVE;
    }

    @Override
    public @NotNull EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    @Nullable
    protected GastroRecipe findRecipe(ItemStack @NotNull [] ingredients, @NotNull List<ItemStack> containers, @NotNull List<ItemStack> tools,
                                      @NotNull Player player, @NotNull BlockMenu menu) {
        final GastroRecipe recipe = super.findRecipe(ingredients, containers, tools, player, menu);
        if (recipe instanceof final MultiStoveRecipe msRecipe) {
            if (msRecipe.getTemperature().getItem().isSimilar(menu.getItemInSlot(TEMPERATURE_BUTTON_SLOT))) {
                return msRecipe;
            } else {
                return null;
            }
        } else {
            return recipe;
        }
    }

    @Override
    protected int getOtherHash(Player player, BlockMenu menu) {
        return menu.getItemInSlot(TEMPERATURE_BUTTON_SLOT).getType().ordinal(); // Doesn't have to be a hash, just
                                                                                // unique
    }

    @Override
    protected boolean canCraft(BlockMenu menu, Block b, Player p, boolean sendMessage) {
        final int charge = getCharge(b.getLocation());
        if (charge < getEnergyPerUse()) {
            Gastronomicon.sendMessage(p, "&eNot enough energy!");
            return false;
        }

        return true;
    }

    @Override
    protected void onSuccessfulCraft(Block b) {
        final int charge = getCharge(b.getLocation());
        setCharge(b.getLocation(), charge - getEnergyPerUse());
    }

}
