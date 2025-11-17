package io.github.schntgaispock.gastronomicon.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.URI;
import java.util.UUID;

public class Util {
    private Util() {}

    public static ItemStack fromBase64Hash(String hash) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID(), null);
        PlayerTextures textures = profile.getTextures();

        try {
            textures.setSkin(URI.create("http://textures.minecraft.net/texture/" + hash).toURL());
        } catch (Exception ignored) {
            // nothing
        }
        profile.setTextures(textures);

        meta.setOwnerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }
}
