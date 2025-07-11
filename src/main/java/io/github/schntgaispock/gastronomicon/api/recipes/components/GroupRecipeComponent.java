package io.github.schntgaispock.gastronomicon.api.recipes.components;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import lombok.Getter;
import net.kyori.adventure.text.Component;

/**
 * For recipe slots that have multiple acceptable items.
 * <hr>
 * Examples:
 * <ul>
 * <li>The wood in a bed
 * <li>The cobblestone in a stone sword
 * </ul>
 * 
 * @author SchnTgaiSpock
 */
@Getter
public class GroupRecipeComponent extends RecipeComponent<Set<ItemStack>> {

    private final @Getter NamespacedKey id;

    public GroupRecipeComponent(NamespacedKey id, Set<ItemStack> component) {
        super(component);

        this.id = id;
    }

    public GroupRecipeComponent(NamespacedKey id, ItemStack... component) {
        this(id, Set.of(component));
    }

    public GroupRecipeComponent(NamespacedKey id, Material... component) {
        this(id, Set.of(Arrays.stream(component).map(ItemStack::new).toArray(ItemStack[]::new)));
    }

    @Override
    public boolean matches(@Nullable ItemStack item) {
        if (item == null) {
            return false;
        } else {
            for (final ItemStack groupItem : component) {
                return item.isSimilar(groupItem);
            }
        }

        return false;
    }

    @Override
    public ItemStack getDisplayItem() {
        if (component.stream().findFirst().isPresent()) {
            final ItemStack displayItem = component.stream().findFirst().get().clone();
            final List<Component> lore = displayItem.lore();
            if (lore != null) {
                lore.add(Component.text(""));
                for (final ItemStack itemStack : component) {
                    lore.add(Component.text("§8‑ §f").append(Objects.requireNonNull(itemStack.getItemMeta().displayName())));
                }
                displayItem.lore(lore);
            }
            return displayItem;
        }
        return null;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
