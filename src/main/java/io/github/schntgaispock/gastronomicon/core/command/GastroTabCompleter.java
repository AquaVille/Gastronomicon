package io.github.schntgaispock.gastronomicon.core.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import io.github.schntgaispock.gastronomicon.core.slimefun.items.food.GastroFood;
import org.jetbrains.annotations.NotNull;

/**
 * Tab completion for the '/gastronomicon' command
 */
public class GastroTabCompleter implements TabCompleter {

    public static final List<String> nums = Arrays.asList("1", "2", "5", "10", "20", "50", "100", "200");

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {

        if (!(sender instanceof final Player player)) {
            return null;
        }

        switch (args.length) {

            case 1:
                return filter(args[0], "help", "profile", "proficiency", "credits");

            case 2:
                switch (args[0]) {
                    case "skills", "profile":
                        return Bukkit.getServer().getOnlinePlayers().stream().map((Player p) -> {
                            return p.getName();
                        }).sorted(String::compareTo).toList();

                    case "proficiency":
                        return filter(args[1], Arrays.asList("get", "set", "add", "remove"));

                    default:
                        break;
                }

            case 3:
                if (args[0].equals("proficiency")) {
                    switch (args[1]) {
                        case "set", "modify", "remove", "get":
                            return GastroFood.getGastroFoodIds().stream()
                                    .filter(id -> !id.startsWith("GN_PERFECT") && id.contains(args[2]))
                                    .toList();

                        default:
                            break;
                    }
                }
                break;

            case 4:
                if (args[0].equals("proficiency")) {
                    switch (args[1]) {
                        case "set", "add", "remove":
                            return filter(args[3], nums);

                        case "get":
                            return filter(args[3], Bukkit.getOnlinePlayers().stream().map(p -> p.getName()));

                        default:
                            break;
                    }
                }
                break;
            
            case 5:
                if (args[0].equals("proficiency")) {
                    switch (args[1]) {
                        case "set", "modify", "remove":
                            return filter(args[4], Bukkit.getOnlinePlayers().stream().map(p -> p.getName()));

                        default:
                            break;
                    }
                }
            break;
            default:
                break;

        }

        return null;
    }

    private List<String> filter(String filter, String... strings) {
        return Arrays.stream(strings).filter(string -> string.startsWith(filter)).toList();
    }

    private List<String> filter(String filter, Collection<String> strings) {
        return strings.stream().filter(string -> string.startsWith(filter)).toList();
    }

    private List<String> filter(String filter, Stream<String> strings) {
        return strings.filter(string -> string.startsWith(filter)).toList();
    }

}
