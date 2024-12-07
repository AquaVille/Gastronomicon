package io.github.schntgaispock.gastronomicon.core.setup;

import io.github.schntgaispock.gastronomicon.core.slimefun.GastroResearch;
import io.github.schntgaispock.gastronomicon.core.slimefun.GastroStacks;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResearchSetup {

    public static void setup() {
        GastroResearch.WOODEN_TOOLS
            .addItems(
                GastroStacks.ROLLING_PIN.item(),
                GastroStacks.MORTAR_AND_PESTLE.item())
            .register();
        GastroResearch.STEEL_TOOLS
            .addItems(
                GastroStacks.KITCHEN_KNIFE.item(),
                GastroStacks.BLENDER.item(),
                GastroStacks.PEELER.item(),
                GastroStacks.BAKING_TRAY.item(),
                GastroStacks.FRYING_PAN.item(),
                GastroStacks.STEEL_POT.item(),
                GastroStacks.STEEL_BOWL.item(),
                GastroStacks.WHISK.item())
            .register();
        GastroResearch.CULINARY_WORKBENCH.addItems(GastroStacks.CULINARY_WORKBENCH.item()).register();
        GastroResearch.MULTI_STOVE.addItems(GastroStacks.MULTI_STOVE.item()).register();
        GastroResearch.GRAIN_MILL.addItems(GastroStacks.MILL.item()).register();
        GastroResearch.REFRIGERATOR.addItems(GastroStacks.REFRIGERATOR.item()).register();
        GastroResearch.FERMENTER.addItems(GastroStacks.FERMENTER.item(), GastroStacks.LARGE_FERMENTER.item()).register();
        GastroResearch.CHEFS_HAT.addItems(GastroStacks.CHEFS_HAT.item()).register();
        GastroResearch.TRAPS
            .addItems(
                GastroStacks.STEEL_WIRE.item(),
                GastroStacks.STEEL_SPRING.item(),
                GastroStacks.CRAB_TRAP.item(),
                GastroStacks.HUNTING_TRAP.item())
            .register();
        GastroResearch.FISHING_NETS
            .addItems(
                GastroStacks.FISHING_NET_I.item(),
                GastroStacks.FISHING_NET_II.item(),
                GastroStacks.FISHING_NET_III.item())
            .register();
        GastroResearch.CHEF_ANDROID
            .addItems(
                GastroStacks.CHEF_ANDROID.item(),
                GastroStacks.CHEF_ANDROID_TRAINER.item())
            .register();
        GastroResearch.ELECTRIC_KITCHEN
            .addItems(
                GastroStacks.ELECTRIC_KITCHEN_I.item(),
                GastroStacks.ELECTRIC_KITCHEN_II.item(),
                GastroStacks.ELECTRIC_KITCHEN_III.item())
            .register();
        GastroResearch.SICKLES
            .addItems(
                GastroStacks.WOODEN_SICKLE.item(),
                GastroStacks.STEEL_SICKLE.item(),
                GastroStacks.REINFORCED_SICKLE.item())
            .register();
        GastroResearch.RAW_INGREDIENTS.register();
        GastroResearch.PROCESSED_INGREDIENTS.register();
        GastroResearch.FOOD.register();
    }

}
