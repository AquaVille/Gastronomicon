package io.github.schntgaispock.gastronomicon.core.group;

import io.github.schntgaispock.gastronomicon.Gastronomicon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A category that is hidden from the main guide page
 *
 * @author Mooy1
 */
@ParametersAreNonnullByDefault
public final class SubGroup extends ItemGroup {

    public SubGroup(String key, ItemStack item) {
        this(key, item, 3);
    }

    public SubGroup(String key, ItemStack item, int tier) {
        super(Gastronomicon.getInstance().createKey(key), item, tier);
    }

    @Override
    public boolean isHidden(Player p) {
        return true;
    }

}