package io.github.schntgaispock.gastronomicon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.github.schntgaispock.gastronomicon.core.GastroConfig;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import io.github.schntgaispock.gastronomicon.api.trees.TreeStructure;
import io.github.schntgaispock.gastronomicon.core.setup.CommandSetup;
import io.github.schntgaispock.gastronomicon.core.setup.ListenerSetup;
import io.github.schntgaispock.gastronomicon.core.setup.ResearchSetup;
import io.github.schntgaispock.gastronomicon.core.setup.ItemSetup;
import io.github.schntgaispock.gastronomicon.integration.DynaTechSetup;
import io.github.schntgaispock.gastronomicon.integration.SlimeHUDSetup;
import io.github.schntgaispock.gastronomicon.util.StringUtil;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

@Getter
public class Gastronomicon extends JavaPlugin implements SlimefunAddon {

    private static @Getter Gastronomicon instance;

    private GastroConfig playerData;
    private GastroConfig customFood;

    @Override
    @SuppressWarnings("deprecation")
    public void onEnable() {
        instance = this;

        info("#======================================#");
        info("#    Gastronomicon by SchnTgaiSpock    #");
        info("#======================================#");

        ItemSetup.setup();
        ResearchSetup.setup();
        ListenerSetup.setup();
        CommandSetup.setup();

        if (isPluginEnabled("SlimeHUD")) {
            try {
                info("SlimeHUD was found on this server!");
                info("Setting up Gastronomicon for SlimeHUD...");
                SlimeHUDSetup.setup();
            } catch (NoClassDefFoundError e) {
                warn("This server is using an incompatitable version of SlimeHUD");
                warn("Please update SlimeHUD to version 1.2.0 or higher!");
            }
        }
        
        // If disable-exotic-garden-recipes is true "!" will change it to false and the rest of the code won't run.
        // If disable-exotic-garden-recipes is false "!" will change it to true and the rest of the code will run checking for ExoticGarden.
        
        if (!getConfig().getBoolean("disable-exotic-garden-recipes") && !isPluginEnabled("ExoticGarden")) {
            warn("ExoticGarden was not found on this server!");
            warn("Recipes that require ExoticGarden items will be hidden.");
        }

        if (isPluginEnabled("DynaTech") && !getConfig().getBoolean("disable-dynatech-integration")) {
            try {
                info("DynaTech was found on this server!");
                info("Registering Gastronomicon crops with DynaTech...");
                DynaTechSetup.setup();
            } catch (NoClassDefFoundError e) {
                warn("This server is using an incompatitable version of DynaTech");
                warn("Please keep Gastronomicon and DynaTech up to date!");
            }
        }

        playerData = new GastroConfig("player.yml");
        customFood = new GastroConfig("custom-food.yml");

        TreeStructure.loadTrees();
    }

    @Override
    public void onDisable() {
        instance = null;
        getPlayerData().save();
    }

    public static NamespacedKey key(@Nonnull String name) {
        return new NamespacedKey(Gastronomicon.getInstance(), name.toUpperCase());
    }

    public static boolean isPluginEnabled(String name) {
        return getInstance().getServer().getPluginManager().isPluginEnabled(name);
    }

    public static int scheduleSyncDelayedTask(Runnable runnable, long delay) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), runnable, delay);
    }

    public static BukkitTask scheduleSyncRepeatingTask(Runnable runnable, long delay, long interval) {
        return Bukkit.getScheduler().runTaskTimer(getInstance(), runnable, delay, interval);
    }

    public static boolean checkPermission(Player player, @Nonnull String permissionNode, @Nullable String message) {
        if (player.hasPermission(permissionNode)) {
            return true;
        }

        if (message != null)
            Gastronomicon.sendMessage(player, message);
        return false;

    }

    public static void info(String message) {
        getInstance().getLogger().info(message);
    }

    public static void warn(String message) {
        getInstance().getLogger().warning(message);
    }

    public static void error(String message) {
        getInstance().getLogger().severe(message);
    }

    public static void sendMessage(Player player, String message) {
        player.sendMessage(/* ChatColor.of("#c91df4") + "§lGastronomicon§7§l> §7" + */ StringUtil.formatColors(message));
    }

    public static void sendMessage(Player player, Component message) {
        final Component text = Component.text()
            .content("Gastronomicon")
            .color(TextColor.color(0xc9, 0x1d, 0xf4))
            .decorate(TextDecoration.BOLD)
            .append(Component.text()
                .content(">")
                .color(TextColor.color(0xaa, 0xaa, 0xaa))
                .decorate(TextDecoration.BOLD)
                .appendSpace()
                .asComponent())
            .asComponent();
        player.sendMessage(text);
    }

    @Override
    public @NotNull JavaPlugin getJavaPlugin() {
        return instance;
    }

    @Override
    public @org.jetbrains.annotations.Nullable String getBugTrackerURL() {
        return "";
    }

    /**
     * Creates a NameSpacedKey from the given string
     */
    @Nonnull
    public NamespacedKey createKey(String s) {
        return new NamespacedKey(getInstance(), s);
    }
}
